package com.example.edulearn

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
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
fun ProgressScreen(
    userId: Int,
    onBackClick: () -> Unit,
    onSubjectsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    val context =
        androidx.compose.ui.platform.LocalContext.current

    var progressList by remember {
        mutableStateOf(emptyList<Progress>())
    }

    var subjects by remember {
        mutableStateOf(emptyList<Subject>())
    }

    var lessons by remember {
        mutableStateOf(emptyList<Lesson>())
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(userId) {

        val database =
            EduLearnDatabase.getDatabase(context)

        val dao =
            database.eduLearnDao()

        progressList =
            dao.getProgressByUser(userId)

        subjects =
            dao.getAllSubjects()

        val allLessons =
            mutableListOf<Lesson>()

        subjects.forEach { subject ->

            allLessons.addAll(
                dao.getLessonsBySubject(subject.id)
            )
        }

        lessons = allLessons

        isLoading = false
    }

    if (isLoading) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF7F9FC)),

            horizontalAlignment =
                Alignment.CenterHorizontally,

            verticalArrangement =
                Arrangement.Center
        ) {

            Text(
                text = "📊 Loading progress...",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        }

        return
    }

    val completedLessons =
        progressList.count { it.completed }

    val totalLessons =
        lessons.size

    val totalPoints =
        progressList.sumOf { it.score }

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

    val pythonSubject =
        subjects.find { it.id == 1 }

    val cppSubject =
        subjects.find { it.id == 2 }

    val dataStructuresSubject =
        subjects.find { it.id == 3 }

    val pythonLessons =
        lessons.filter {
            it.subjectId == 1
        }

    val cppLessons =
        lessons.filter {
            it.subjectId == 2
        }

    val dataStructuresLessons =
        lessons.filter {
            it.subjectId == 3
        }

    val pythonCompleted =
        pythonLessons.count { lesson ->

            progressList.any {
                it.lessonId == lesson.id &&
                        it.completed
            }
        }

    val cppCompleted =
        cppLessons.count { lesson ->

            progressList.any {
                it.lessonId == lesson.id &&
                        it.completed
            }
        }

    val dataStructuresCompleted =
        dataStructuresLessons.count { lesson ->

            progressList.any {
                it.lessonId == lesson.id &&
                        it.completed
            }
        }

    Scaffold(

        bottomBar = {

            EduLearnBottomNavigation(

                selectedTab = 2,

                onHomeClick = {
                    onBackClick()
                },

                onLearnClick = {
                    onSubjectsClick()
                },

                onProgressClick = {
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

            // ==========================================
            // HEADER
            // ==========================================

            Text(
                text = "📊 My Progress",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text =
                    "Track your learning journey and achievements.",

                fontSize = 14.sp,

                color = Color.Gray
            )

            // ==========================================
            // OVERALL PROGRESS CARD
            // ==========================================

            Card(
                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(24.dp),

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            Color(0xFF1E3A8A)
                    ),

                elevation =
                    CardDefaults.cardElevation(
                        defaultElevation = 5.dp
                    )
            ) {

                Column(
                    modifier =
                        Modifier.padding(22.dp),

                    verticalArrangement =
                        Arrangement.spacedBy(12.dp)
                ) {

                    Row(
                        modifier =
                            Modifier.fillMaxWidth(),

                        horizontalArrangement =
                            Arrangement.SpaceBetween,

                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Text(
                            text =
                                "📈 Overall Progress",

                            color =
                                Color.White,

                            fontSize = 20.sp,

                            fontWeight =
                                FontWeight.Bold
                        )

                        Text(
                            text =
                                "$progressPercentage%",

                            color =
                                Color.White,

                            fontSize = 22.sp,

                            fontWeight =
                                FontWeight.Bold
                        )
                    }

                    Text(
                        text =
                            "$completedLessons of $totalLessons lessons completed",

                        color =
                            Color.White.copy(
                                alpha = 0.85f
                            ),

                        fontSize = 14.sp
                    )

                    LinearProgressIndicator(

                        progress = {
                            progressValue
                        },

                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(9.dp),

                        color = Color.White,

                        trackColor =
                            Color.White.copy(
                                alpha = 0.25f
                            )
                    )
                }
            }

            // ==========================================
            // STATISTICS
            // ==========================================

            Text(
                text = "📌 Learning Statistics",
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.spacedBy(10.dp)
            ) {

                ProgressStatCard(
                    modifier =
                        Modifier.weight(1f),

                    icon = "📚",

                    value =
                        completedLessons.toString(),

                    label =
                        "Completed"
                )

                ProgressStatCard(
                    modifier =
                        Modifier.weight(1f),

                    icon = "⭐",

                    value =
                        totalPoints.toString(),

                    label =
                        "Points"
                )
            }

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.spacedBy(10.dp)
            ) {

                ProgressStatCard(
                    modifier =
                        Modifier.weight(1f),

                    icon = "📖",

                    value =
                        totalLessons.toString(),

                    label =
                        "Total Lessons"
                )

                ProgressStatCard(
                    modifier =
                        Modifier.weight(1f),

                    icon = "🎯",

                    value =
                        progressPercentage.toString() + "%",

                    label =
                        "Progress"
                )
            }

            // ==========================================
            // SUBJECT PROGRESS
            // ==========================================

            Text(
                text = "📚 Subject Progress",
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold
            )

            SubjectProgressCard(
                emoji = "🐍",

                title =
                    pythonSubject?.name
                        ?: "🐍 Python",

                completed =
                    pythonCompleted,

                total =
                    pythonLessons.size
            )

            SubjectProgressCard(
                emoji = "💻",

                title =
                    cppSubject?.name
                        ?: "💻 C++",

                completed =
                    cppCompleted,

                total =
                    cppLessons.size
            )

            SubjectProgressCard(
                emoji = "🧠",

                title =
                    dataStructuresSubject?.name
                        ?: "🧠 Data Structures",

                completed =
                    dataStructuresCompleted,

                total =
                    dataStructuresLessons.size
            )

            // ==========================================
            // COMPLETED LESSONS
            // ==========================================

            Text(
                text = "✅ Completed Lessons",
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold
            )

            if (progressList.isEmpty()) {

                Card(
                    modifier =
                        Modifier.fillMaxWidth(),

                    shape =
                        RoundedCornerShape(20.dp),

                    colors =
                        CardDefaults.cardColors(
                            containerColor =
                                Color.White
                        ),

                    elevation =
                        CardDefaults.cardElevation(
                            defaultElevation = 3.dp
                        )
                ) {

                    Column(
                        modifier =
                            Modifier.padding(20.dp),

                        horizontalAlignment =
                            Alignment.CenterHorizontally
                    ) {

                        Text(
                            text =
                                "📚 No lessons completed yet.",

                            fontSize = 18.sp,

                            fontWeight =
                                FontWeight.Bold
                        )

                        Spacer(
                            modifier =
                                Modifier.height(6.dp)
                        )

                        Text(
                            text =
                                "Start learning to see your progress here.",

                            fontSize = 14.sp,

                            color = Color.Gray
                        )
                    }
                }

            } else {

                progressList.forEach { progress ->

                    val lesson =
                        lessons.find {
                            it.id ==
                                    progress.lessonId
                        }

                    val subject =
                        subjects.find {
                            it.id ==
                                    progress.subjectId
                        }

                    CompletedLessonCard(
                        lessonTitle =
                            lesson?.title
                                ?: "Lesson",

                        subjectName =
                            subject?.name
                                ?: "Subject",

                        score =
                            progress.score
                    )
                }
            }

            // ==========================================
            // BACK HOME
            // ==========================================

            Button(
                onClick =
                    onBackClick,

                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(14.dp)
            ) {

                Text(
                    text = "🏠 Back to Home",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )
        }
    }
}

@Composable
private fun ProgressStatCard(
    modifier: Modifier,
    icon: String,
    value: String,
    label: String
) {

    Card(
        modifier = modifier,

        shape =
            RoundedCornerShape(18.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color.White
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 3.dp
            )
    ) {

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally,

            verticalArrangement =
                Arrangement.spacedBy(5.dp)
        ) {

            Text(
                text = icon,
                fontSize = 25.sp
            )

            Text(
                text = value,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = label,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun SubjectProgressCard(
    emoji: String,
    title: String,
    completed: Int,
    total: Int
) {

    val progress =
        if (total > 0)
            completed.toFloat() /
                    total.toFloat()
        else
            0f

    val percentage =
        if (total > 0)
            (completed * 100) / total
        else
            0

    Card(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(20.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color.White
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 3.dp
            )
    ) {

        Column(
            modifier =
                Modifier.padding(18.dp),

            verticalArrangement =
                Arrangement.spacedBy(10.dp)
        ) {

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(
                    text = emoji,
                    fontSize = 26.sp
                )

                Spacer(
                    modifier =
                        Modifier.width(10.dp)
                )

                Text(
                    text = title,
                    modifier =
                        Modifier.weight(1f),

                    fontSize = 18.sp,

                    fontWeight =
                        FontWeight.Bold
                )

                Text(
                    text = "$percentage%",
                    fontSize = 16.sp,
                    fontWeight =
                        FontWeight.Bold
                )
            }

            Text(
                text =
                    "$completed / $total lessons completed",

                fontSize = 13.sp,

                color = Color.Gray
            )

            LinearProgressIndicator(
                progress = {
                    progress
                },

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(8.dp)
            )
        }
    }
}

@Composable
private fun CompletedLessonCard(
    lessonTitle: String,
    subjectName: String,
    score: Int
) {

    Card(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(20.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color.White
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 3.dp
            )
    ) {

        Column(
            modifier =
                Modifier.padding(18.dp),

            verticalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {

            Text(
                text =
                    "✅ $lessonTitle",

                fontSize = 18.sp,

                fontWeight =
                    FontWeight.Bold
            )

            Text(
                text =
                    "📚 $subjectName",

                fontSize = 14.sp,

                color = Color.Gray
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.SpaceBetween
            ) {

                Text(
                    text = "🎯 Completed",
                    fontSize = 14.sp
                )

                Text(
                    text = "⭐ Quiz Score: $score",
                    fontSize = 14.sp,
                    fontWeight =
                        FontWeight.Bold
                )
            }
        }
    }
}