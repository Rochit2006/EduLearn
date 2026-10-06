package com.example.edulearn

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.edulearn.database.EduLearnDatabase
import com.example.edulearn.database.Lesson
import com.example.edulearn.database.Progress
import com.example.edulearn.database.Subject

@Composable
fun SubjectsScreen(
    userId: Int,
    onPythonClick: () -> Unit,
    onCppClick: () -> Unit,
    onDataStructuresClick: () -> Unit,
    onSubjectClick: (Int) -> Unit = {},
    onBackClick: () -> Unit,
    onProgressClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    val context = androidx.compose.ui.platform.LocalContext.current

    var subjects by remember {
        mutableStateOf(emptyList<Subject>())
    }

    var lessons by remember {
        mutableStateOf(emptyList<Lesson>())
    }

    var progressList by remember {
        mutableStateOf(emptyList<Progress>())
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(userId) {
        val database = EduLearnDatabase.getDatabase(context)
        val dao = database.eduLearnDao()

        subjects = dao.getAllSubjects()

        val allLessons = mutableListOf<Lesson>()

        subjects.forEach { subject ->
            allLessons.addAll(
                dao.getLessonsBySubject(subject.id)
            )
        }

        lessons = allLessons
        progressList = dao.getProgressByUser(userId)
        isLoading = false
    }

    if (isLoading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "📚 Loading subjects...",
                fontSize = 20.sp
            )
        }

        return
    }

    val totalCompleted = subjects.sumOf { subject ->
        lessons
            .filter { it.subjectId == subject.id }
            .count { lesson ->
                progressList.any {
                    it.lessonId == lesson.id && it.completed
                }
            }
    }

    val totalLessons = lessons.size

    val overallProgress =
        if (totalLessons > 0) {
            totalCompleted.toFloat() / totalLessons.toFloat()
        } else {
            0f
        }

    val overallPercentage =
        if (totalLessons > 0) {
            (totalCompleted * 100) / totalLessons
        } else {
            0
        }

    Scaffold(
        bottomBar = {
            EduLearnBottomNavigation(
                selectedTab = 1,

                onHomeClick = {
                    onBackClick()
                },

                onLearnClick = {
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
                .background(Color(0xFFF7F9FC))
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(18.dp),

            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "📚 Learn",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = "Choose a subject and continue your learning journey.",
                        fontSize = 15.sp
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

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onProgressClick()
                    },

                shape = RoundedCornerShape(22.dp),

                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1E3A8A)
                ),

                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
            ) {

                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = "📈 Overall Progress",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "$overallPercentage%",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "$totalCompleted of $totalLessons lessons completed",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 14.sp
                    )

                    LinearProgressIndicator(
                        progress = {
                            overallProgress
                        },

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(9.dp),

                        color = Color.White,

                        trackColor = Color.White.copy(
                            alpha = 0.25f
                        )
                    )

                    Text(
                        text = "Tap to view detailed progress →",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 13.sp
                    )
                }
            }

            Text(
                text = "Choose a Subject",
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold
            )

            subjects.forEach { subject ->

                val subjectLessons =
                    lessons.filter {
                        it.subjectId == subject.id
                    }

                val completedLessons =
                    subjectLessons.count { lesson ->

                        progressList.any {
                            it.lessonId == lesson.id &&
                                    it.completed
                        }
                    }

                val emoji =
                    when (subject.id) {
                        1 -> "🐍"
                        2 -> "💻"
                        3 -> "🧠"
                        else -> "📚"
                    }

                val subtitle =
                    when (subject.id) {
                        1 -> "Learn Python programming from basics"
                        2 -> "Build programming fundamentals with C++"
                        3 -> "Master arrays, linked lists, stacks and trees"
                        else -> "Learn concepts and practice with quizzes"
                    }

                SubjectCard(
                    title = subject.name,
                    subtitle = subtitle,
                    completed = completedLessons,
                    total = subjectLessons.size,
                    emoji = emoji,

                    onClick = {

                        when (subject.id) {

                            1 -> {
                                onPythonClick()
                            }

                            2 -> {
                                onCppClick()
                            }

                            3 -> {
                                onDataStructuresClick()
                            }

                            else -> {
                                onSubjectClick(subject.id)
                            }
                        }
                    }
                )
            }

            Button(
                onClick = onBackClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {

                Text(
                    text = "⬅️ Back to Home",
                    fontSize = 16.sp
                )
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )
        }
    }
}

@Composable
private fun SubjectCard(
    title: String,
    subtitle: String,
    completed: Int,
    total: Int,
    emoji: String,
    onClick: () -> Unit
) {

    val progress =
        if (total > 0) {
            completed.toFloat() / total.toFloat()
        } else {
            0f
        }

    val percentage =
        if (total > 0) {
            (completed * 100) / total
        } else {
            0
        }

    val buttonText =
        when {
            completed == 0 ->
                "▶️ Start Learning"

            completed == total ->
                "🔄 Review Lessons"

            else ->
                "▶️ Continue Learning"
        }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },

        shape = RoundedCornerShape(22.dp),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Card(
                    modifier = Modifier.size(52.dp),

                    shape = RoundedCornerShape(16.dp),

                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE8EEF9)
                    )
                ) {

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Text(
                            text = emoji,
                            fontSize = 25.sp
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.size(12.dp)
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = subtitle,
                        fontSize = 13.sp
                    )
                }

                Text(
                    text = "$percentage%",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "$completed / $total lessons completed",
                fontSize = 14.sp
            )

            LinearProgressIndicator(
                progress = {
                    progress
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )

            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {

                Text(
                    text = buttonText,
                    fontSize = 16.sp
                )
            }
        }
    }
}