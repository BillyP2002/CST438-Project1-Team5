package com.example.cst438_project1_team5

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cst438_project1_team5.database.MusicDatabaseHelper
import com.example.cst438_project1_team5.ui.theme.CST438Project1Team5Theme
import androidx.core.content.edit

private const val AUTH_PREFS_NAME = "music_auth_prefs"
private const val PREF_LOGGED_IN_USER_ID = "logged_in_user_id"
private const val PREF_LOGGED_IN_USERNAME = "logged_in_username"

enum class AuthMode {
    SignIn,
    SignUp
}

class MainActivity : ComponentActivity() {
    private lateinit var databaseHelper: MusicDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databaseHelper = MusicDatabaseHelper(applicationContext)
        enableEdgeToEdge()
        setContent {
            CST438Project1Team5Theme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color.Transparent
                ) { innerPadding ->
                    AuthScreen(
                        modifier = Modifier.padding(innerPadding),
                        databaseHelper = databaseHelper
                    )
                }
            }
        }
    }
}

@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    databaseHelper: MusicDatabaseHelper
) {
    var currentMode by rememberSaveable { mutableStateOf(AuthMode.SignIn) }

    when (currentMode) {
        AuthMode.SignIn -> SignInScreen(
            modifier = modifier,
            databaseHelper = databaseHelper,
            onCreateAccountClick = { currentMode = AuthMode.SignUp }
        )

        AuthMode.SignUp -> SignUpScreen(
            modifier = modifier,
            databaseHelper = databaseHelper,
            onAlreadyHaveAccountClick = { currentMode = AuthMode.SignIn }
        )
    }
}

private fun getRememberedUserPrefs(context: Context): SharedPreferences {
    return context.getSharedPreferences(AUTH_PREFS_NAME, Context.MODE_PRIVATE)
}

private fun getLoggedInUserId(context: Context): Long? {
    val userId = getRememberedUserPrefs(context).getLong(PREF_LOGGED_IN_USER_ID, -1L)
    return userId.takeIf { it != -1L }
}

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    databaseHelper: MusicDatabaseHelper,
    onCreateAccountClick: () -> Unit = {}
) {
    val context = LocalContext.current
    var email by rememberSaveable { mutableStateOf(getRememberedUserPrefs(context).getString("remembered_email", "") ?: "") }
    var password by rememberSaveable { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    var authError by rememberSaveable { mutableStateOf("") }
    var isSigningIn by remember { mutableStateOf(false) }

    ScreenBackground {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFDDE7FF)
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.sign_in_subtitle),
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFFC7D2FE)
            )

            Spacer(modifier = Modifier.height(28.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF111827).copy(alpha = 0.9f)
                ),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.12f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Text(
                        text = stringResource(R.string.sign_in_title),
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(R.string.sign_in_prompt),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFFCBD5E1)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(stringResource(R.string.email_label)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = null,
                                tint = Color(0xFF7DD3FC)
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color(0xFF7DD3FC),
                            unfocusedIndicatorColor = Color(0xFF475569),
                            focusedLabelColor = Color(0xFF7DD3FC),
                            unfocusedLabelColor = Color(0xFFCBD5E1)
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(stringResource(R.string.password_label)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = Color(0xFF7DD3FC)
                            )
                        },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        shape = RoundedCornerShape(16.dp),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color(0xFF7DD3FC),
                            unfocusedIndicatorColor = Color(0xFF475569),
                            focusedLabelColor = Color(0xFF7DD3FC),
                            unfocusedLabelColor = Color(0xFFCBD5E1)
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = rememberMe,
                                onCheckedChange = { rememberMe = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Color(0xFF7DD3FC),
                                    uncheckedColor = Color(0xFF94A3B8)
                                )
                            )
                            Text(
                                text = stringResource(R.string.remember_me),
                                color = Color(0xFFE2E8F0)
                            )
                        }

                        TextButton(onClick = {}) {
                            Text(
                                text = stringResource(R.string.forgot_password),
                                color = Color(0xFF7DD3FC)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (authError.isNotBlank()) {
                        Text(
                            text = authError,
                            color = Color(0xFFFCA5A5),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Button(
                        onClick = {
                            if (email.isBlank() || password.isBlank()) {
                                authError = "Please enter both email and password."
                                return@Button
                            }

                            isSigningIn = true
                            authError = ""

                            try {
                                val account = databaseHelper.authenticateUser(email, password)
                                if (account != null) {
                                    getRememberedUserPrefs(context).edit {
                                        putLong(PREF_LOGGED_IN_USER_ID, account.id)
                                            .putString(PREF_LOGGED_IN_USERNAME, account.username)
                                    }

                                    if (rememberMe) {
                                        getRememberedUserPrefs(context).edit {
                                            putString("remembered_email", email)
                                        }
                                    } else {
                                        getRememberedUserPrefs(context).edit {
                                            remove("remembered_email")
                                        }
                                    }
                                    Toast.makeText(context, "Signed in successfully!", Toast.LENGTH_SHORT).show()
                                } else {
                                    authError = "Invalid credentials or account is locked."
                                }
                            } catch (e: Exception) {
                                authError = e.message ?: "Unable to sign in."
                            } finally {
                                isSigningIn = false
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(16.dp),
                        enabled = !isSigningIn,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF7C3AED)
                        )
                    ) {
                        Text(
                            text = if (isSigningIn) "Signing in..." else stringResource(R.string.sign_in_button),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = stringResource(R.string.no_account_prompt),
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = Color(0xFFCBD5E1)
                    )

                    TextButton(
                        onClick = onCreateAccountClick,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = stringResource(R.string.sign_up_text),
                            color = Color(0xFF7DD3FC),
                            fontWeight = FontWeight.Bold
                        )
                    }
                    TextButton(
                        onClick = {

                                  },

                            modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = stringResource(R.string.sound_test),
                            color = Color(0xFF7DD3FC),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    databaseHelper: MusicDatabaseHelper,
    onAlreadyHaveAccountClick: () -> Unit = {}
) {
    val context = LocalContext.current
    var fullName by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var authError by rememberSaveable { mutableStateOf("") }
    var isSigningUp by remember { mutableStateOf(false) }

    ScreenBackground {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFDDE7FF)
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.sign_up_subtitle),
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFFC7D2FE)
            )

            Spacer(modifier = Modifier.height(28.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF111827).copy(alpha = 0.9f)
                ),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.12f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Text(
                        text = stringResource(R.string.sign_up_title),
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(R.string.sign_up_prompt),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFFCBD5E1)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(stringResource(R.string.full_name_label)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = Color(0xFF7DD3FC)
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color(0xFF7DD3FC),
                            unfocusedIndicatorColor = Color(0xFF475569),
                            focusedLabelColor = Color(0xFF7DD3FC),
                            unfocusedLabelColor = Color(0xFFCBD5E1)
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(stringResource(R.string.email_label)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = null,
                                tint = Color(0xFF7DD3FC)
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color(0xFF7DD3FC),
                            unfocusedIndicatorColor = Color(0xFF475569),
                            focusedLabelColor = Color(0xFF7DD3FC),
                            unfocusedLabelColor = Color(0xFFCBD5E1)
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(stringResource(R.string.password_label)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = Color(0xFF7DD3FC)
                            )
                        },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        shape = RoundedCornerShape(16.dp),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color(0xFF7DD3FC),
                            unfocusedIndicatorColor = Color(0xFF475569),
                            focusedLabelColor = Color(0xFF7DD3FC),
                            unfocusedLabelColor = Color(0xFFCBD5E1)
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(stringResource(R.string.confirm_password_label)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = Color(0xFF7DD3FC)
                            )
                        },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        shape = RoundedCornerShape(16.dp),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color(0xFF7DD3FC),
                            unfocusedIndicatorColor = Color(0xFF475569),
                            focusedLabelColor = Color(0xFF7DD3FC),
                            unfocusedLabelColor = Color(0xFFCBD5E1)
                        )
                    )

                    Spacer(modifier = Modifier.height(22.dp))

                    if (authError.isNotBlank()) {
                        Text(
                            text = authError,
                            color = Color(0xFFFCA5A5),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Button(
                        onClick = {
                            if (fullName.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                                authError = "Please fill in all fields."
                                return@Button
                            }
                            if (password != confirmPassword) {
                                authError = "Passwords do not match."
                                return@Button
                            }
                            if (password.length < 12) {
                                authError = "Password must be at least 12 characters long."
                                return@Button
                            }

                            isSigningUp = true
                            authError = ""

                            try {
                                databaseHelper.registerUser(fullName, email, password)
                                Toast.makeText(context, "Account created successfully!", Toast.LENGTH_SHORT).show()
                                fullName = ""
                                email = ""
                                password = ""
                                confirmPassword = ""
                                onAlreadyHaveAccountClick()
                            } catch (e: IllegalArgumentException) {
                                authError = e.message ?: "Unable to create account."
                            } catch (e: Exception) {
                                authError = e.message ?: "Unable to create account."
                            } finally {
                                isSigningUp = false
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        enabled = !isSigningUp,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF7C3AED)
                        )
                    ) {
                        Text(
                            text = if (isSigningUp) "Creating account..." else stringResource(R.string.sign_up_button),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = stringResource(R.string.already_have_account_prompt),
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = Color(0xFFCBD5E1)
                    )

                    TextButton(
                        onClick = onAlreadyHaveAccountClick,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = stringResource(R.string.sign_in_text),
                            color = Color(0xFF7DD3FC),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ScreenBackground(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0F172A),
                        Color(0xFF111827),
                        Color(0xFF020617)
                    )
                )
            )
    ) {
        content()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignInScreenPreview() {
    val context = LocalContext.current
    CST438Project1Team5Theme {
        SignInScreen(
            databaseHelper = MusicDatabaseHelper(context),
            onCreateAccountClick = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignUpScreenPreview() {
    val context = LocalContext.current
    CST438Project1Team5Theme {
        SignUpScreen(
            databaseHelper = MusicDatabaseHelper(context),
            onAlreadyHaveAccountClick = {}
        )
    }
}
