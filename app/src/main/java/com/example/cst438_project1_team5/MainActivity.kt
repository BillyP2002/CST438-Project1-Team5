package com.example.cst438_project1_team5

import android.os.Bundle
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cst438_project1_team5.ui.theme.CST438Project1Team5Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CST438Project1Team5Theme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color.Transparent
                ) { innerPadding ->
                    SignInScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun SignInScreen(modifier: Modifier = Modifier) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
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
        Column(
            modifier = Modifier
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
                        colors = androidx.compose.material3.TextFieldDefaults.colors(
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
                        colors = androidx.compose.material3.TextFieldDefaults.colors(
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
                                colors = androidx.compose.material3.CheckboxDefaults.colors(
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

                    Button(
                        onClick = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF7C3AED)
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.sign_in_button),
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
                        onClick = {},
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = stringResource(R.string.sign_up_text),
                            color = Color(0xFF7DD3FC),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignInScreenPreview() {
    CST438Project1Team5Theme {
        SignInScreen()
    }
}
