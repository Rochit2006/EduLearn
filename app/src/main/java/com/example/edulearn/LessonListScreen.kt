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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
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
fun LessonListScreen(
    subjectId: Int,
    userId: Int,
    onLessonClick: (Lesson) -> Unit,
    onBackClick: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current

    var lessons by remember {
        mutableStateOf(emptyList<Lesson>())
    }

    var progressList by remember {
        mutableStateOf(emptyList<Progress>())
    }

    var subject by remember {
        mutableStateOf<Subject?>(null)
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(subjectId, userId) {

        val database = EduLearnDatabase.getDatabase(context)
        val dao = database.eduLearnDao()

        lessons = dao.getLessonsBySubject(subjectId)

        progressList = dao.getProgressByUser(userId)

        subject = dao.getAllSubjects()
            .find { it.id == subjectId }

        isLoading = false
    }

    if (isLoading) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF7F9FC)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "📚 Loading lessons...",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        }

        return
    }

    val completedLessons = lessons.count { lesson ->

        progressList.any {
            it.lessonId == lesson.id &&
                    it.completed
        }
    }

    val totalLessons = lessons.size

    val progress =
        if (totalLessons > 0)
            completedLessons.toFloat() /
                    totalLessons.toFloat()
        else
            0f

    val percentage =
        if (totalLessons > 0)
            (completedLessons * 100) /
                    totalLessons
        else
            0

    val subjectName =
        subject?.name ?: "📚 Lessons"

    val subjectEmoji =
        when (subjectId) {
            1 -> "🐍"
            2 -> "💻"
            3 -> "🧠"
            else -> "📚"
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FC))
            .padding(18.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "$subjectEmoji $subjectName",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = "Learn step by step and build your skills.",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
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
                    horizontalArrangement =
                        Arrangement.SpaceBetween,
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Text(
                        text = "📈 Your Progress",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "$percentage%",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text =
                        "$completedLessons of $totalLessons lessons completed",

                    color =
                        Color.White.copy(alpha = 0.85f),

                    fontSize = 14.sp
                )

                LinearProgressIndicator(
                    progress = {
                        progress
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(9.dp),

                    color = Color.White,

                    trackColor =
                        Color.White.copy(alpha = 0.25f)
                )
            }
        }

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "📖 Lessons",
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "$completedLessons/$totalLessons completed",
                fontSize = 13.sp,
                color = Color.Gray
            )
        }

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            itemsIndexed(lessons) { index, lesson ->

                val lessonProgress =
                    progressList.find {
                        it.lessonId == lesson.id
                    }

                val completed =
                    lessonProgress?.completed == true

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onLessonClick(lesson)
                        },

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
                            Arrangement.spacedBy(12.dp)
                    ) {

                        Row(
                            modifier =
                                Modifier.fillMaxWidth(),

                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {

                            Card(
                                modifier =
                                    Modifier.size(50.dp),

                                shape =
                                    RoundedCornerShape(15.dp),

                                colors =
                                    CardDefaults.cardColors(
                                        containerColor =
                                            if (completed)
                                                Color(0xFFE8F5E9)
                                            else
                                                Color(0xFFE8EEF9)
                                    )
                            ) {

                                Column(
                                    modifier =
                                        Modifier.fillMaxSize(),

                                    horizontalAlignment =
                                        Alignment.CenterHorizontally,

                                    verticalArrangement =
                                        Arrangement.Center
                                ) {

                                    Text(
                                        text =
                                            if (completed)
                                                "✓"
                                            else
                                                "${index + 1}",

                                        fontSize = 21.sp,

                                        fontWeight =
                                            FontWeight.Bold,

                                        color =
                                            if (completed)
                                                Color(0xFF2E7D32)
                                            else
                                                Color(0xFF1E3A8A)
                                    )
                                }
                            }

                            Spacer(
                                modifier =
                                    Modifier.size(14.dp)
                            )

                            Column(
                                modifier =
                                    Modifier.weight(1f)
                            ) {

                                Text(
                                    text =
                                        lesson.title,

                                    fontSize = 18.sp,

                                    fontWeight =
                                        FontWeight.Bold
                                )

                                Spacer(
                                    modifier =
                                        Modifier.height(3.dp)
                                )

                                Text(
                                    text =
                                        if (completed)
                                            "✅ Completed"
                                        else
                                            "▶️ Ready to learn",

                                    fontSize = 13.sp,

                                    color =
                                        if (completed)
                                            Color(0xFF2E7D32)
                                        else
                                            Color.Gray
                                )
                            }
                        }

                        if (completed) {

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
                                        "⭐ Quiz Score",

                                    fontSize = 14.sp,

                                    fontWeight =
                                        FontWeight.Medium
                                )

                                Text(
                                    text =
                                        "${lessonProgress?.score ?: 0}/3",

                                    fontSize = 15.sp,

                                    fontWeight =
                                        FontWeight.Bold
                                )
                            }
                        }

                        Button(
                            onClick = {
                                onLessonClick(lesson)
                            },

                            modifier =
                                Modifier.fillMaxWidth(),

                            shape =
                                RoundedCornerShape(12.dp)
                        ) {

                            Text(
                                text =
                                    if (completed)
                                        "🔄 Review Lesson"
                                    else
                                        "▶️ Start Lesson",

                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        OutlinedButton(
            onClick = onBackClick,

            modifier =
                Modifier.fillMaxWidth(),

            shape =
                RoundedCornerShape(12.dp)
        ) {

            Text(
                text = "⬅️ Back to Subjects",
                fontSize = 15.sp
            )
        }
    }
}