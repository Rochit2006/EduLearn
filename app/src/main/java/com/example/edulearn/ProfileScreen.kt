package com.example.edulearn

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.edulearn.database.EduLearnDatabase
import com.example.edulearn.database.User
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userId: Int,
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onSubjectsClick: () -> Unit,
    onProgressClick: () -> Unit,
    onManageContentClick: () -> Unit,
    isDarkMode: Boolean = false,
    onDarkModeChange: (Boolean) -> Unit = {}
) {
    val context = LocalContext.current
    val db = remember { EduLearnDatabase.getDatabase(context) }
    val scope = rememberCoroutineScope()

    var user by remember { mutableStateOf<User?>(null) }
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }

    var completedLessons by remember { mutableStateOf(0) }
    var totalPoints by remember { mutableStateOf(0) }

    var isEditing by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }

    var editName by remember { mutableStateOf("") }
    var editCourse by remember { mutableStateOf("") }
    var editBranch by remember { mutableStateOf("") }
    var editCollege by remember { mutableStateOf("") }
    var editYear by remember { mutableStateOf("") }

    var courseExpanded by remember { mutableStateOf(false) }
    var branchExpanded by remember { mutableStateOf(false) }
    var yearExpanded by remember { mutableStateOf(false) }

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

    val backgroundColor = if (isDarkMode) {
        Color(0xFF0F172A)
    } else {
        Color(0xFFF7F9FC)
    }

    val cardColor = if (isDarkMode) {
        Color(0xFF1E293B)
    } else {
        Color.White
    }

    val primaryText = if (isDarkMode) {
        Color.White
    } else {
        Color(0xFF172554)
    }

    val secondaryText = if (isDarkMode) {
        Color(0xFFCBD5E1)
    } else {
        Color.Gray
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->

        if (uri != null) {

            try {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: Exception) {
            }

            profileImageUri = uri

            context.getSharedPreferences(
                "edulearn_profile",
                Context.MODE_PRIVATE
            ).edit()
                .putString(
                    "profile_image_$userId",
                    uri.toString()
                )
                .apply()
        }
    }

    LaunchedEffect(userId) {

        user = db.eduLearnDao().getUserById(userId)

        val currentUser = user

        if (currentUser != null) {
            editName = currentUser.name
            editCourse = currentUser.course
            editBranch = currentUser.branch
            editCollege = currentUser.college
            editYear = currentUser.year
        }

        val savedUri = context.getSharedPreferences(
            "edulearn_profile",
            Context.MODE_PRIVATE
        ).getString(
            "profile_image_$userId",
            null
        )

        if (savedUri != null) {
            profileImageUri = Uri.parse(savedUri)
        }

        val progress =
            db.eduLearnDao().getProgressByUser(userId)

        completedLessons =
            progress.count { it.completed }

        totalPoints =
            progress.sumOf { it.score }
    }

    val currentUser = user

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .verticalScroll(rememberScrollState())
    ) {

        // HEADER
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 20.dp,
                    vertical = 18.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = onBackClick
            ) {
                Text(
                    text = "‹",
                    fontSize = 34.sp,
                    color = if (isDarkMode) {
                        Color(0xFF93C5FD)
                    } else {
                        Color(0xFF1E3A8A)
                    }
                )
            }

            Text(
                text = "My Profile",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = primaryText,
                modifier = Modifier.weight(1f)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {

            // PROFILE CARD
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = cardColor
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .background(
                                if (isDarkMode) {
                                    Color(0xFF334155)
                                } else {
                                    Color(0xFFE0E7FF)
                                }
                            )
                            .clickable {
                                imagePicker.launch(
                                    arrayOf("image/*")
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {

                        if (profileImageUri != null) {

                            val bitmap =
                                remember(profileImageUri) {
                                    try {
                                        context.contentResolver
                                            .openInputStream(
                                                profileImageUri!!
                                            )
                                            ?.use {
                                                BitmapFactory.decodeStream(it)
                                            }
                                    } catch (_: Exception) {
                                        null
                                    }
                                }

                            if (bitmap != null) {

                                Image(
                                    bitmap = bitmap.asImageBitmap(),
                                    contentDescription = "Profile Picture",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )

                            } else {

                                Text(
                                    text = "👤",
                                    fontSize = 48.sp
                                )
                            }

                        } else {

                            Text(
                                text = "👤",
                                fontSize = 48.sp
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Text(
                        text = currentUser?.name ?: "Student",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryText
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = currentUser?.email ?: "",
                        fontSize = 14.sp,
                        color = secondaryText
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    TextButton(
                        onClick = {
                            imagePicker.launch(
                                arrayOf("image/*")
                            )
                        }
                    ) {

                        Text(
                            text = if (profileImageUri == null) {
                                "Add Profile Picture"
                            } else {
                                "Change Profile Picture"
                            },
                            color = Color(0xFF60A5FA),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            // STATISTICS
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                ProfileStatCard(
                    title = "Lessons",
                    value = completedLessons.toString(),
                    emoji = "📚",
                    modifier = Modifier.weight(1f),
                    isDarkMode = isDarkMode
                )

                ProfileStatCard(
                    title = "Points",
                    value = totalPoints.toString(),
                    emoji = "⭐",
                    modifier = Modifier.weight(1f),
                    isDarkMode = isDarkMode
                )
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            // PERSONAL INFORMATION
            Text(
                text = "Personal Information",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = primaryText
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = cardColor
                )
            ) {

                Column(
                    modifier = Modifier.padding(18.dp)
                ) {

                    if (!isEditing) {

                        ProfileInfoRow(
                            label = "Name",
                            value = currentUser?.name
                                ?: "Not available",
                            isDarkMode = isDarkMode
                        )

                        ProfileInfoRow(
                            label = "Email",
                            value = currentUser?.email
                                ?: "Not available",
                            isDarkMode = isDarkMode
                        )

                        ProfileInfoRow(
                            label = "Course",
                            value = currentUser?.course
                                ?: "Not available",
                            isDarkMode = isDarkMode
                        )

                        ProfileInfoRow(
                            label = "Branch",
                            value = currentUser?.branch
                                ?: "Not available",
                            isDarkMode = isDarkMode
                        )

                        ProfileInfoRow(
                            label = "College",
                            value = currentUser?.college
                                ?: "Not available",
                            isDarkMode = isDarkMode
                        )

                        ProfileInfoRow(
                            label = "Year",
                            value = currentUser?.year
                                ?: "Not available",
                            isDarkMode = isDarkMode
                        )

                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )

                        Button(
                            onClick = {

                                editName =
                                    currentUser?.name ?: ""

                                editCourse =
                                    currentUser?.course ?: ""

                                editBranch =
                                    currentUser?.branch ?: ""

                                editCollege =
                                    currentUser?.college ?: ""

                                editYear =
                                    currentUser?.year ?: ""

                                message = ""
                                isEditing = true
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "✏️ Edit Profile",
                                fontSize = 16.sp
                            )
                        }

                    } else {

                        Text(
                            text = "Edit Profile",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryText
                        )

                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )

                        OutlinedTextField(
                            value = editName,
                            onValueChange = {
                                editName = it
                                message = ""
                            },
                            label = {
                                Text("👤 Full Name")
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(
                            modifier = Modifier.height(10.dp)
                        )

                        OutlinedTextField(
                            value = currentUser?.email ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = {
                                Text("📧 Email")
                            },
                            supportingText = {
                                Text("Email cannot be changed")
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(
                            modifier = Modifier.height(10.dp)
                        )

                        ExposedDropdownMenuBox(
                            expanded = courseExpanded,
                            onExpandedChange = {
                                courseExpanded = !courseExpanded
                            }
                        ) {

                            OutlinedTextField(
                                value = editCourse,
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
                                            editCourse =
                                                selectedCourse
                                            courseExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(
                            modifier = Modifier.height(10.dp)
                        )

                        ExposedDropdownMenuBox(
                            expanded = branchExpanded,
                            onExpandedChange = {
                                branchExpanded =
                                    !branchExpanded
                            }
                        ) {

                            OutlinedTextField(
                                value = editBranch,
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
                                            editBranch =
                                                selectedBranch
                                            branchExpanded =
                                                false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(
                            modifier = Modifier.height(10.dp)
                        )

                        OutlinedTextField(
                            value = editCollege,
                            onValueChange = {
                                editCollege = it
                                message = ""
                            },
                            label = {
                                Text("🏫 College")
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(
                            modifier = Modifier.height(10.dp)
                        )

                        ExposedDropdownMenuBox(
                            expanded = yearExpanded,
                            onExpandedChange = {
                                yearExpanded = !yearExpanded
                            }
                        ) {

                            OutlinedTextField(
                                value = editYear,
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
                                            editYear =
                                                selectedYear
                                            yearExpanded =
                                                false
                                        }
                                    )
                                }
                            }
                        }

                        if (message.isNotEmpty()) {

                            Spacer(
                                modifier = Modifier.height(10.dp)
                            )

                            Text(
                                text = message,
                                fontSize = 14.sp,
                                color = Color(0xFF16A34A)
                            )
                        }

                        Spacer(
                            modifier = Modifier.height(14.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement =
                                Arrangement.spacedBy(10.dp)
                        ) {

                            Button(
                                onClick = {

                                    if (
                                        editName.isBlank() ||
                                        editCourse.isBlank() ||
                                        editBranch.isBlank() ||
                                        editCollege.isBlank() ||
                                        editYear.isBlank()
                                    ) {
                                        message =
                                            "⚠️ Please fill all fields."
                                        return@Button
                                    }

                                    scope.launch {

                                        val oldUser =
                                            db.eduLearnDao()
                                                .getUserById(userId)

                                        if (oldUser != null) {

                                            val updatedUser =
                                                oldUser.copy(
                                                    name =
                                                        editName.trim(),
                                                    course =
                                                        editCourse.trim(),
                                                    branch =
                                                        editBranch.trim(),
                                                    college =
                                                        editCollege.trim(),
                                                    year =
                                                        editYear.trim()
                                                )

                                            db.eduLearnDao()
                                                .updateUser(
                                                    updatedUser
                                                )

                                            user = updatedUser

                                            message =
                                                "✅ Profile updated successfully."

                                            isEditing = false
                                        }
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("💾 Save")
                            }

                            Button(
                                onClick = {
                                    isEditing = false
                                    message = ""
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor =
                                        if (isDarkMode) {
                                            Color(0xFF475569)
                                        } else {
                                            Color(0xFF64748B)
                                        }
                                )
                            ) {
                                Text("Cancel")
                            }
                        }
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            // SETTINGS
            Text(
                text = "Settings",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = primaryText
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = cardColor
                )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onDarkModeChange(!isDarkMode)
                        }
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "🌙",
                        fontSize = 28.sp
                    )

                    Spacer(
                        modifier = Modifier.width(14.dp)
                    )

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = "Dark Mode",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = primaryText
                        )

                        Text(
                            text = if (isDarkMode) {
                                "Dark theme is enabled"
                            } else {
                                "Use a darker appearance"
                            },
                            fontSize = 13.sp,
                            color = secondaryText
                        )
                    }

                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = {
                            onDarkModeChange(it)
                        }
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            // QUICK ACTIONS
            Text(
                text = "Quick Actions",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = primaryText
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            // MANAGE CONTENT
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onManageContentClick()
                    },
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = cardColor
                )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "⚙️",
                        fontSize = 28.sp
                    )

                    Spacer(
                        modifier = Modifier.width(14.dp)
                    )

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = "Manage Content",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = primaryText
                        )

                        Text(
                            text = "Add subjects and lessons",
                            fontSize = 13.sp,
                            color = secondaryText
                        )
                    }

                    Text(
                        text = "›",
                        fontSize = 28.sp,
                        color = secondaryText
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            // BROWSE SUBJECTS
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onSubjectsClick()
                    },
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = cardColor
                )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "📚",
                        fontSize = 28.sp
                    )

                    Spacer(
                        modifier = Modifier.width(14.dp)
                    )

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = "Browse Subjects",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = primaryText
                        )

                        Text(
                            text = "Continue learning",
                            fontSize = 13.sp,
                            color = secondaryText
                        )
                    }

                    Text(
                        text = "›",
                        fontSize = 28.sp,
                        color = secondaryText
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            // VIEW PROGRESS
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onProgressClick()
                    },
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = cardColor
                )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "📊",
                        fontSize = 28.sp
                    )

                    Spacer(
                        modifier = Modifier.width(14.dp)
                    )

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = "View Progress",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = primaryText
                        )

                        Text(
                            text = "Check your learning progress",
                            fontSize = 13.sp,
                            color = secondaryText
                        )
                    }

                    Text(
                        text = "›",
                        fontSize = 28.sp,
                        color = secondaryText
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            // LOGOUT
            Button(
                onClick = onLogoutClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFDC2626)
                )
            ) {

                Text(
                    text = "Logout",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )
        }
    }
}

@Composable
fun ProfileStatCard(
    title: String,
    value: String,
    emoji: String,
    modifier: Modifier = Modifier,
    isDarkMode: Boolean = false
) {

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDarkMode) {
                Color(0xFF1E293B)
            } else {
                Color.White
            }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = emoji,
                fontSize = 26.sp
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF60A5FA)
            )

            Text(
                text = title,
                fontSize = 13.sp,
                color = if (isDarkMode) {
                    Color(0xFFCBD5E1)
                } else {
                    Color.Gray
                }
            )
        }
    }
}

@Composable
fun ProfileInfoRow(
    label: String,
    value: String,
    isDarkMode: Boolean = false
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 7.dp)
    ) {

        Text(
            text = label,
            fontSize = 12.sp,
            color = if (isDarkMode) {
                Color(0xFF94A3B8)
            } else {
                Color.Gray
            }
        )

        Spacer(
            modifier = Modifier.height(2.dp)
        )

        Text(
            text = value,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = if (isDarkMode) {
                Color.White
            } else {
                Color(0xFF172554)
            }
        )
    }
}