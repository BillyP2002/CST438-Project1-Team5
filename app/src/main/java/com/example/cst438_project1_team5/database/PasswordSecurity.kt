package com.example.cst438_project1_team5.database

import android.util.Base64
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

object PasswordSecurity {
    const val DEFAULT_ITERATIONS = 120_000

    fun generateSalt(): ByteArray {
        val random = SecureRandom()
        val salt = ByteArray(16)
        random.nextBytes(salt)
        return salt
    }

    fun hashPassword(
        password: String,
        salt: ByteArray,
        iterations: Int = DEFAULT_ITERATIONS
    ): String {
        val keySpec = PBEKeySpec(password.toCharArray(), salt, iterations, 256)
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val hashed = factory.generateSecret(keySpec).encoded
        return Base64.encodeToString(hashed, Base64.NO_WRAP)
    }
}
