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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import com.example.edulearn.database.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegistrationSuccess: () -> Unit,
    onBackToLogin: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val firebaseAuth = remember {
        FirebaseAuth.getInstance()
    }

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var course by remember { mutableStateOf("") }
    var branch by remember { mutableStateOf("") }
    var college by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }

    var courseExpanded by remember { mutableStateOf(false) }
    var branchExpanded by remember { mutableStateOf(false) }
    var yearExpanded by remember { mutableStateOf(false) }

    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }

    val courses = listOf(
        "B.Tech",
        "B.E.",
        "BCA",
        "MCA"
    )

    val branches = listOf(
        "IT",
        "CSE",
        "ECE",
        "R&A",
        "E&TC"
    )

    val years = listOf(
        "1st Year",
        "2nd Year",
        "3rd Year",
        "4th Year"
    )

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
            modifier = Modifier.size(100.dp)
        )

        Text(
            text = "Create Account",
            fontSize = 30.sp
        )

        Text(
            text = "Join EduLearn and start learning",
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
                    text = "👤 Student Information",
                    fontSize = 21.sp
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        message = ""
                    },
                    label = {
                        Text("👤 Full Name")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                ExposedDropdownMenuBox(
                    expanded = courseExpanded,
                    onExpandedChange = {
                        courseExpanded = !courseExpanded
                    }
                ) {

                    OutlinedTextField(
                        value = course,
                        onValueChange = {},
                        readOnly = true,
                        label = {
                            Text("🎓 Course")
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = courseExpanded
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        singleLine = true
                    )

                    ExposedDropdownMenu(
                        expanded = courseExpanded,
                        onDismissRequest = {
                            courseExpanded = false
                        }
                    ) {

                        courses.forEach { selectedCourse ->

                            DropdownMenuItem(
                                text = {
                                    Text(selectedCourse)
                                },
                                onClick = {
                                    course = selectedCourse
                                    courseExpanded = false
                                    message = ""
                                }
                            )
                        }
                    }
                }

                ExposedDropdownMenuBox(
                    expanded = branchExpanded,
                    onExpandedChange = {
                        branchExpanded = !branchExpanded
                    }
                ) {

                    OutlinedTextField(
                        value = branch,
                        onValueChange = {},
                        readOnly = true,
                        label = {
                            Text("💻 Branch")
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = branchExpanded
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        singleLine = true
                    )

                    ExposedDropdownMenu(
                        expanded = branchExpanded,
                        onDismissRequest = {
                            branchExpanded = false
                        }
                    ) {

                        branches.forEach { selectedBranch ->

                            DropdownMenuItem(
                                text = {
                                    Text(selectedBranch)
                                },
                                onClick = {
                                    branch = selectedBranch
                                    branchExpanded = false
                                    message = ""
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = college,
                    onValueChange = {
                        college = it
                        message = ""
                    },
                    label = {
                        Text("🏫 College")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                ExposedDropdownMenuBox(
                    expanded = yearExpanded,
                    onExpandedChange = {
                        yearExpanded = !yearExpanded
                    }
                ) {

                    OutlinedTextField(
                        value = year,
                        onValueChange = {},
                        readOnly = true,
                        label = {
                            Text("📚 Year")
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = yearExpanded
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        singleLine = true
                    )

                    ExposedDropdownMenu(
                        expanded = yearExpanded,
                        onDismissRequest = {
                            yearExpanded = false
                        }
                    ) {

                        years.forEach { selectedYear ->

                            DropdownMenuItem(
                                text = {
                                    Text(selectedYear)
                                },
                                onClick = {
                                    year = selectedYear
                                    yearExpanded = false
                                    message = ""
                                }
                            )
                        }
                    }
                }

                Text(
                    text = "🔐 Account Information",
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

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        message = ""
                    },
                    label = {
                        Text("🔒 Confirm Password")
                    },
                    visualTransformation =
                        if (showConfirmPassword)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                    trailingIcon = {
                        TextButton(
                            onClick = {
                                showConfirmPassword =
                                    !showConfirmPassword
                            }
                        ) {
                            Text(
                                if (showConfirmPassword)
                                    "Hide"
                                else
                                    "Show"
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

                        if (
                            name.isBlank() ||
                            email.isBlank() ||
                            password.isBlank() ||
                            confirmPassword.isBlank() ||
                            course.isBlank() ||
                            branch.isBlank() ||
                            college.isBlank() ||
                            year.isBlank()
                        ) {
                            message =
                                "⚠️ Please fill all fields."
                            return@Button
                        }

                        if (
                            !android.util.Patterns.EMAIL_ADDRESS
                                .matcher(email.trim())
                                .matches()
                        ) {
                            message =
                                "⚠️ Please enter a valid email address."
                            return@Button
                        }

                        if (password.length < 6) {
                            message =
                                "⚠️ Password must contain at least 6 characters."
                            return@Button
                        }

                        if (password != confirmPassword) {
                            message =
                                "⚠️ Passwords do not match."
                            return@Button
                        }

                        scope.launch {

                            val database =
                                EduLearnDatabase.getDatabase(context)

                            val dao =
                                database.eduLearnDao()

                            try {

                                val existingRoomUser =
                                    dao.getUserByEmail(
                                        email.trim()
                                    )

                                if (existingRoomUser != null) {

                                    message =
                                        "❌ Student profile already exists. Please login."

                                    return@launch
                                }

                                message =
                                    "⏳ Creating your account..."

                                try {

                                    val authResult =
                                        firebaseAuth
                                            .createUserWithEmailAndPassword(
                                                email.trim(),
                                                password
                                            )
                                            .await()

                                    val firebaseUser =
                                        authResult.user

                                    if (firebaseUser == null) {

                                        message =
                                            "❌ Firebase account creation failed."

                                        return@launch
                                    }

                                    firebaseUser
                                        .sendEmailVerification()
                                        .await()

                                    val user = User(
                                        name = name.trim(),
                                        email = email.trim(),
                                        password = password,
                                        course = course.trim(),
                                        branch = branch.trim(),
                                        college = college.trim(),
                                        year = year.trim(),
                                        emailVerified = false
                                    )

                                    dao.insertUser(user)

                                    firebaseAuth.signOut()

                                    message =
                                        "✅ Account created! Verification email sent."

                                    onRegistrationSuccess()

                                } catch (e: FirebaseAuthException) {

                                    if (
                                        e.errorCode ==
                                        "ERROR_EMAIL_ALREADY_IN_USE"
                                    ) {

                                        message =
                                            "⏳ Firebase account already exists. Checking account..."

                                        try {

                                            firebaseAuth
                                                .signInWithEmailAndPassword(
                                                    email.trim(),
                                                    password
                                                )
                                                .await()

                                            val firebaseUser =
                                                firebaseAuth.currentUser

                                            if (firebaseUser == null) {

                                                message =
                                                    "❌ Could not access Firebase account."

                                                return@launch
                                            }

                                            val existingProfile =
                                                dao.getUserByEmail(
                                                    email.trim()
                                                )

                                            if (existingProfile == null) {

                                                val user = User(
                                                    name = name.trim(),
                                                    email = email.trim(),
                                                    password = password,
                                                    course = course.trim(),
                                                    branch = branch.trim(),
                                                    college = college.trim(),
                                                    year = year.trim(),
                                                    emailVerified =
                                                        firebaseUser.isEmailVerified
                                                )

                                                dao.insertUser(user)

                                                message =
                                                    "✅ Student profile created successfully."

                                            } else {

                                                message =
                                                    "⚠️ Student profile already exists. Please login."
                                            }

                                            firebaseAuth.signOut()

                                            onRegistrationSuccess()

                                        } catch (loginError: Exception) {

                                            message =
                                                "❌ Firebase account already exists, but the password is incorrect."
                                        }

                                    } else {

                                        message = when (e.errorCode) {

                                            "ERROR_INVALID_EMAIL" ->
                                                "⚠️ Please enter a valid email address."

                                            "ERROR_WEAK_PASSWORD" ->
                                                "⚠️ Password is too weak. Use at least 6 characters."

                                            "ERROR_OPERATION_NOT_ALLOWED" ->
                                                "❌ Email/Password authentication is not enabled in Firebase."

                                            "ERROR_NETWORK_REQUEST_FAILED" ->
                                                "❌ Network connection failed. Check your internet."

                                            "ERROR_API_NOT_AVAILABLE" ->
                                                "❌ Firebase Authentication API is not available."

                                            "ERROR_INVALID_API_KEY" ->
                                                "❌ Firebase API key is invalid."

                                            "ERROR_APP_NOT_AUTHORIZED" ->
                                                "❌ This Android app is not authorized in Firebase."

                                            else ->
                                                "❌ Firebase error: ${e.errorCode}"
                                        }
                                    }

                                }

                            } catch (e: Exception) {

                                message =
                                    "❌ Error: ${e.message ?: "Unknown error"}"
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {

                    Text(
                        text = "📝 Create Account",
                        fontSize = 18.sp
                    )
                }
            }
        }

        TextButton(
            onClick = onBackToLogin,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "← Back to Login",
                fontSize = 17.sp
            )
        }
    }
}