package com.example.edulearn

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.edulearn.database.EduLearnDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun LoginScreen(
    onLoginSuccess: (Int) -> Unit,
    onRegisterClick: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val firebaseAuth = remember {
        FirebaseAuth.getInstance()
    }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Image(
            painter = painterResource(
                id = R.drawable.edulearn_logo
            ),
            contentDescription = "EduLearn Logo",
            modifier = Modifier.size(110.dp)
        )

        Text(
            text = "EduLearn",
            fontSize = 34.sp
        )

        Text(
            text = "Welcome Back!",
            fontSize = 23.sp
        )

        Text(
            text = "Login to continue your learning journey",
            fontSize = 16.sp
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp
            )
        ) {

            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                Text(
                    text = "🔐 Account Login",
                    fontSize = 21.sp
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        message = ""
                    },
                    label = {
                        Text("📧 Email")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        message = ""
                    },
                    label = {
                        Text("🔒 Password")
                    },
                    visualTransformation =
                        if (showPassword)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                    trailingIcon = {
                        TextButton(
                            onClick = {
                                showPassword = !showPassword
                            }
                        ) {
                            Text(
                                if (showPassword) "Hide" else "Show"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                if (message.isNotEmpty()) {
                    Text(
                        text = message,
                        fontSize = 15.sp
                    )
                }

                Button(
                    onClick = {

                        if (email.isBlank() || password.isBlank()) {
                            message =
                                "⚠️ Please enter email and password."
                            return@Button
                        }

                        if (!android.util.Patterns.EMAIL_ADDRESS
                                .matcher(email.trim())
                                .matches()
                        ) {
                            message =
                                "⚠️ Please enter a valid email."
                            return@Button
                        }

                        if (password.length < 6) {
                            message =
                                "⚠️ Password must contain at least 6 characters."
                            return@Button
                        }

                        scope.launch {

                            try {

                                val result =
                                    firebaseAuth
                                        .signInWithEmailAndPassword(
                                            email.trim(),
                                            password
                                        )
                                        .await()

                                val firebaseUser =
                                    result.user

                                if (firebaseUser == null) {
                                    message =
                                        "❌ Login failed."
                                    return@launch
                                }

                                if (!firebaseUser.isEmailVerified) {

                                    message =
                                        "⚠️ Please verify your email first."

                                    firebaseUser
                                        .sendEmailVerification()
                                        .await()

                                    return@launch
                                }

                                val database =
                                    EduLearnDatabase.getDatabase(context)

                                val user =
                                    database.eduLearnDao()
                                        .getUserByEmail(
                                            email.trim()
                                        )

                                if (user != null) {

                                    message =
                                        "✅ Login successful!"

                                    onLoginSuccess(user.id)

                                } else {

                                    message =
                                        "❌ Firebase account exists, but Student profile was not found."
                                }

                            } catch (e: FirebaseAuthException) {

                                message = when (e.errorCode) {

                                    "ERROR_INVALID_EMAIL" ->
                                        "❌ Invalid email address."

                                    "ERROR_WRONG_PASSWORD" ->
                                        "❌ Incorrect password."

                                    "ERROR_USER_NOT_FOUND" ->
                                        "❌ No account found with this email."

                                    "ERROR_USER_DISABLED" ->
                                        "❌ This account has been disabled."

                                    "ERROR_TOO_MANY_REQUESTS" ->
                                        "⚠️ Too many attempts. Try again later."

                                    "ERROR_NETWORK_REQUEST_FAILED" ->
                                        "❌ Network error. Check your internet."

                                    else ->
                                        "❌ Firebase error: ${e.errorCode}"
                                }

                            } catch (e: Exception) {

                                message =
                                    "❌ Error: ${e.message}"
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "🔐 Login",
                        fontSize = 18.sp
                    )
                }
            }
        }

        Text(
            text = "Don't have an account?",
            fontSize = 15.sp
        )

        TextButton(
            onClick = onRegisterClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "📝 Create New Account",
                fontSize = 17.sp
            )
        }
    }
}