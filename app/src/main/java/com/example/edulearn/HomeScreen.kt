package com.example.edulearn

import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import com.example.edulearn.database.EduLearnDatabase
import com.example.edulearn.database.Lesson
import com.example.edulearn.database.Progress
import com.example.edulearn.database.User

@Composable
fun HomeScreen(
    userId: Int,
    onSubjectsClick: () -> Unit,
    onContinueLearning: (Int, Int) -> Unit,
    onProgressClick: () -> Unit,
    onAchievementsClick: () -> Unit,
    onProfileClick: () -> Unit,
    onLogoutClick: () -> Unit
) {

    val context =
        androidx.compose.ui.platform.LocalContext.current

    var user by remember {
        mutableStateOf<User?>(null)
    }

    var progressList by remember {
        mutableStateOf(emptyList<Progress>())
    }

    var allLessons by remember {
        mutableStateOf(emptyList<Lesson>())
    }

    var showLogoutDialog by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(userId) {

        val database =
            EduLearnDatabase.getDatabase(context)

        val dao =
            database.eduLearnDao()

        user = dao.getUserById(userId)

        progressList =
            dao.getProgressByUser(userId)

        val subjects =
            dao.getAllSubjects()

        val lessons =
            mutableListOf<Lesson>()

        subjects.forEach { subject ->
            lessons.addAll(
                dao.getLessonsBySubject(subject.id)
            )
        }

        allLessons = lessons
    }

    val completedLessons =
        progressList.count { it.completed }

    val totalLessons =
        allLessons.size

    val progressPercentage =
        if (totalLessons > 0)
            (completedLessons * 100) / totalLessons
        else
            0

    val progressValue =
        if (totalLessons > 0)
            completedLessons.toFloat() /
                    totalLessons.toFloat()
        else
            0f

    val totalPoints =
        progressList.sumOf { it.score }

    val perfectScores =
        progressList.count { it.score == 3 }

    val achievementsUnlocked =
        listOf(
            completedLessons >= 1,
            completedLessons >= 5,
            completedLessons >= 10,
            completedLessons >= 15,
            perfectScores > 0,
            totalPoints >= 10
        ).count { it }

    val firstIncompleteLesson =
        allLessons.firstOrNull { lesson ->

            progressList.none {
                it.lessonId == lesson.id &&
                        it.completed
            }
        }

    Scaffold(

        bottomBar = {

            EduLearnBottomNavigation(

                selectedTab = 0,

                onHomeClick = {
                },

                onLearnClick = {
                    onSubjectsClick()
                },

                onProgressClick = {
                    onProgressClick()
                },

                onProfileClick = {
                    onProfileClick()
                }
            )
        }

    ) { innerPadding ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .background(
                    Color(0xFFF7F9FC)
                )
                .padding(innerPadding)
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(18.dp),

            verticalArrangement =
                Arrangement.spacedBy(16.dp)
        ) {

            // HEADER

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "🎓 EduLearn",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = "Your learning journey starts here",
                        fontSize = 14.sp
                    )
                }

                OutlinedButton(
                    onClick = onProfileClick,
                    shape = RoundedCornerShape(14.dp)
                ) {

                    Text(
                        text = "👤",
                        fontSize = 20.sp
                    )
                }
            }


            // WELCOME CARD

            Card(

                modifier = Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(24.dp),

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            Color(0xFF1E3A8A)
                    )
            ) {

                Column(
                    modifier =
                        Modifier.padding(22.dp),

                    verticalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {

                    Text(
                        text =
                            if (user != null)
                                "Welcome back, ${user!!.name}! 👋"
                            else
                                "Welcome back! 👋",

                        color = Color.White,

                        fontSize = 23.sp,

                        fontWeight =
                            FontWeight.Bold
                    )

                    Text(
                        text =
                            "Keep learning and reach your goals.",

                        color =
                            Color.White.copy(
                                alpha = 0.85f
                            ),

                        fontSize = 15.sp
                    )

                    Spacer(
                        modifier =
                            Modifier.height(6.dp)
                    )

                    Button(

                        onClick =
                            onSubjectsClick,

                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor =
                                    Color.White,
                                contentColor =
                                    Color(0xFF1E3A8A)
                            ),

                        shape =
                            RoundedCornerShape(12.dp)
                    ) {

                        Text(
                            text = "📚 Start Learning",
                            fontWeight =
                                FontWeight.Bold
                        )
                    }
                }
            }


            // PROGRESS CARD

            Card(

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            onProgressClick()
                        },

                shape =
                    RoundedCornerShape(22.dp),

                elevation =
                    CardDefaults.cardElevation(
                        defaultElevation = 3.dp
                    )
            ) {

                Column(
                    modifier =
                        Modifier.padding(20.dp),

                    verticalArrangement =
                        Arrangement.spacedBy(12.dp)
                ) {

                    Row(
                        modifier =
                            Modifier.fillMaxWidth(),
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Text(
                            text = "📈",
                            fontSize = 28.sp
                        )

                        Spacer(
                            modifier =
                                Modifier.width(10.dp)
                        )

                        Column {

                            Text(
                                text =
                                    "Overall Progress",

                                fontSize =
                                    20.sp,

                                fontWeight =
                                    FontWeight.Bold
                            )

                            Text(
                                text =
                                    "$completedLessons of $totalLessons lessons completed",

                                fontSize = 14.sp
                            )
                        }
                    }

                    LinearProgressIndicator(

                        progress = {
                            progressValue
                        },

                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(
                                    RoundedCornerShape(10.dp)
                                )
                    )

                    Text(
                        text =
                            "$progressPercentage% completed",

                        fontSize = 15.sp,

                        fontWeight =
                            FontWeight.SemiBold
                    )
                }
            }


            // STATISTICS

            Text(
                text = "Your Statistics",
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.spacedBy(10.dp)
            ) {

                StatisticCard(
                    modifier =
                        Modifier
                            .weight(1f)
                            .clickable {
                                onProgressClick()
                            },
                    emoji = "📚",
                    value =
                        completedLessons.toString(),
                    title = "Lessons"
                )

                StatisticCard(
                    modifier =
                        Modifier
                            .weight(1f)
                            .clickable {
                                onProgressClick()
                            },
                    emoji = "⭐",
                    value =
                        totalPoints.toString(),
                    title = "Points"
                )

                StatisticCard(
                    modifier =
                        Modifier
                            .weight(1f)
                            .clickable {
                                onProgressClick()
                            },
                    emoji = "🏆",
                    value =
                        achievementsUnlocked.toString(),
                    title = "Badges"
                )
            }


            // CONTINUE LEARNING

            Card(

                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(22.dp),

                elevation =
                    CardDefaults.cardElevation(
                        defaultElevation = 3.dp
                    )
            ) {

                Column(
                    modifier =
                        Modifier.padding(20.dp),

                    verticalArrangement =
                        Arrangement.spacedBy(10.dp)
                ) {

                    Text(
                        text =
                            "🚀 Continue Learning",

                        fontSize = 21.sp,

                        fontWeight =
                            FontWeight.Bold
                    )

                    Text(
                        text =
                            if (firstIncompleteLesson != null)
                                "Continue with ${firstIncompleteLesson.title}"
                            else
                                "🎉 You completed all available lessons!",

                        fontSize = 15.sp
                    )

                    Spacer(
                        modifier =
                            Modifier.height(4.dp)
                    )

                    Button(

                        onClick = {

                            if (
                                firstIncompleteLesson != null
                            ) {

                                onContinueLearning(
                                    firstIncompleteLesson.subjectId,
                                    firstIncompleteLesson.id
                                )

                            } else {

                                onSubjectsClick()
                            }
                        },

                        modifier =
                            Modifier.fillMaxWidth(),

                        shape =
                            RoundedCornerShape(12.dp)
                    ) {

                        Text(
                            text =
                                if (
                                    firstIncompleteLesson != null
                                )
                                    "▶ Continue Learning"
                                else
                                    "🔄 Review Lessons",

                            fontSize = 16.sp
                        )
                    }
                }
            }


            // ACHIEVEMENTS

            Card(

                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(22.dp),

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            Color(0xFFFFF8E1)
                    )
            ) {

                Row(

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(18.dp),

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Text(
                        text = "🏆",
                        fontSize = 34.sp
                    )

                    Spacer(
                        modifier =
                            Modifier.width(14.dp)
                    )

                    Column(
                        modifier =
                            Modifier.weight(1f)
                    ) {

                        Text(
                            text =
                                "Achievements",

                            fontSize = 18.sp,

                            fontWeight =
                                FontWeight.Bold
                        )

                        Text(
                            text =
                                "$achievementsUnlocked achievements unlocked",

                            fontSize = 13.sp
                        )
                    }

                    Button(
                        onClick =
                            onAchievementsClick,

                        shape =
                            RoundedCornerShape(12.dp)
                    ) {

                        Text("View")
                    }
                }
            }


            // LOGOUT

            OutlinedButton(

                onClick = {
                    showLogoutDialog = true
                },

                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(12.dp)
            ) {

                Text(
                    text = "🚪 Logout",
                    fontSize = 16.sp
                )
            }

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )
        }
    }


    // LOGOUT DIALOG

    if (showLogoutDialog) {

        AlertDialog(

            onDismissRequest = {
                showLogoutDialog = false
            },

            title = {
                Text("🚪 Logout")
            },

            text = {
                Text(
                    "Are you sure you want to logout?"
                )
            },

            confirmButton = {

                Button(

                    onClick = {

                        showLogoutDialog = false

                        onLogoutClick()
                    }
                ) {

                    Text("Logout")
                }
            },

            dismissButton = {

                TextButton(

                    onClick = {
                        showLogoutDialog = false
                    }
                ) {

                    Text("Cancel")
                }
            }
        )
    }
}


@Composable
fun StatisticCard(
    modifier: Modifier,
    emoji: String,
    value: String,
    title: String
) {

    Card(

        modifier = modifier,

        shape =
            RoundedCornerShape(18.dp),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
    ) {

        Column(

            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(14.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally,

            verticalArrangement =
                Arrangement.spacedBy(4.dp)
        ) {

            Text(
                text = emoji,
                fontSize = 25.sp
            )

            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = title,
                fontSize = 12.sp
            )
        }
    }
}