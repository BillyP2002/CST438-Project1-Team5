package com.example.cst438_project1_team5.database

import android.util.Base64
import com.example.cst438_project1_team5.database.dao.UserDao
import com.example.cst438_project1_team5.database.entities.UserEntity
import java.security.SecureRandom
import java.util.Locale
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

class UserRepository(private val userDao: UserDao) {

    suspend fun registerUser(username: String, email: String, password: String): Long {
        val cleanUsername = username.trim()
        val cleanEmail = email.trim()

        require(cleanUsername.isNotEmpty()) { "Username cannot be empty." }
        require(cleanEmail.isNotEmpty()) { "Email cannot be empty." }
        require(isValidEmail(cleanEmail)) { "Email format is invalid." }
        require(password.length >= MIN_PASSWORD_LENGTH) {
            "Password must be at least $MIN_PASSWORD_LENGTH characters long."
        }

        val normalizedUser = cleanUsername.lowercase(Locale.US)
        val normalizedEmail = cleanEmail.lowercase(Locale.US)

        if (userDao.getUserByEmail(normalizedEmail) != null) {
            throw IllegalArgumentException("An account with that email already exists.")
        }
        if (userDao.getUserByUsername(normalizedUser) != null) {
            throw IllegalArgumentException("That username is already taken.")
        }

        val salt = generateSalt()
        val hash = hashPassword(password, salt)
        val user = UserEntity(
            username = normalizedUser,
            email = normalizedEmail,
            passwordHash = hash,
            passwordSalt = Base64.encodeToString(salt, Base64.NO_WRAP),
            passwordIterations = DEFAULT_ITERATIONS,
            createdAt = System.currentTimeMillis()
        )

        return userDao.insertUser(user)
    }

    suspend fun authenticateUser(identifier: String, password: String): UserEntity? {
        val normalizedIdentifier = identifier.trim().lowercase(Locale.US)
        val user = userDao.getUserByUsername(normalizedIdentifier)
            ?: userDao.getUserByEmail(normalizedIdentifier)

        if (user == null || System.currentTimeMillis() < user.lockedUntil) {
            return null
        }

        val salt = Base64.decode(user.passwordSalt, Base64.NO_WRAP)
        val expectedHash = hashPassword(password, salt, user.passwordIterations)

        return updateLoginState(user, constantTimeEquals(expectedHash, user.passwordHash))
    }

    private suspend fun updateLoginState(user: UserEntity, isSuccess: Boolean): UserEntity? {
        return if (isSuccess) {
            val updatedUser = user.copy(
                lastLoginAt = System.currentTimeMillis(),
                failedAttempts = 0,
                lockedUntil = 0
            )
            userDao.updateUser(updatedUser)
            updatedUser
        } else {
            val failedAttempts = user.failedAttempts + 1
            val lockDuration = if (failedAttempts >= MAX_FAILED_ATTEMPTS) {
                LOCK_DURATION_MS
            } else {
                0L
            }

            val lockedUntil = if (lockDuration >
                0L
            ) {
                System.currentTimeMillis() + lockDuration
            } else {
                0L
            }

            val updatedUser = user.copy(
                failedAttempts = failedAttempts,
                lockedUntil = lockedUntil
            )
            userDao.updateUser(updatedUser)
            null
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return email.matches(emailRegex)
    }

    private fun constantTimeEquals(expected: String, actual: String): Boolean {
        if (expected.length != actual.length) {
            return false
        }
        var result = 0
        for (i in expected.indices) {
            result = result or expected[i].code.xor(actual[i].code)
        }
        return result == 0
    }

    private fun generateSalt(): ByteArray {
        val random = SecureRandom()
        val salt = ByteArray(16)
        random.nextBytes(salt)
        return salt
    }

    private fun hashPassword(
        password: String,
        salt: ByteArray,
        iterations: Int = DEFAULT_ITERATIONS
    ): String {
        val keySpec = PBEKeySpec(password.toCharArray(), salt, iterations, 256)
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val hashed = factory.generateSecret(keySpec).encoded
        return Base64.encodeToString(hashed, Base64.NO_WRAP)
    }

    companion object {
        const val DEFAULT_ITERATIONS = 120_000
        private const val MIN_PASSWORD_LENGTH = 12
        private const val MAX_FAILED_ATTEMPTS = 5
        private const val LOCK_DURATION_MS = 15L * 60L * 1000L
    }
}
