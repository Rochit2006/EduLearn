package com.example.edulearn

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.edulearn.database.EduLearnDatabase
import com.example.edulearn.database.Lesson
import com.example.edulearn.database.Progress

@Composable
fun LessonScreenLoader(
    lessonId: Int,
    userId: Int,
    onStartQuiz: () -> Unit,
    onBackClick: () -> Unit
) {
    val context =
        androidx.compose.ui.platform.LocalContext.current

    var lesson by remember {
        mutableStateOf<Lesson?>(null)
    }

    var progress by remember {
        mutableStateOf<Progress?>(null)
    }

    var allLessons by remember {
        mutableStateOf(emptyList<Lesson>())
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(lessonId, userId) {

        val database =
            EduLearnDatabase.getDatabase(context)

        val dao = database.eduLearnDao()

        lesson =
            dao.getLessonById(lessonId)

        progress =
            dao.getProgressByUserAndLesson(
                userId = userId,
                lessonId = lessonId
            )

        if (lesson != null) {

            allLessons =
                dao.getLessonsBySubject(
                    lesson!!.subjectId
                )
        }

        isLoading = false
    }

    if (isLoading) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                "Loading lesson...",
                fontSize = 20.sp,
                modifier = Modifier.padding(20.dp)
            )
        }

        return
    }

    if (lesson == null) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                "❌ Lesson not found.",
                fontSize = 20.sp,
                modifier = Modifier.padding(20.dp)
            )

            Button(
                onClick = onBackClick,
                modifier = Modifier.padding(20.dp)
            ) {

                Text("⬅️ Back")
            }
        }

        return
    }

    val currentLessonIndex =
        allLessons.indexOfFirst {
            it.id == lessonId
        }

    val lessonNumber =
        if (currentLessonIndex >= 0) {
            currentLessonIndex + 1
        } else {
            1
        }

    val totalLessons =
        allLessons.size

    val lessonProgress =
        if (totalLessons > 0) {
            lessonNumber.toFloat() /
                    totalLessons.toFloat()
        } else {
            0f
        }

    val percentage =
        if (totalLessons > 0) {
            (lessonNumber * 100) / totalLessons
        } else {
            0
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
            .padding(20.dp),
        verticalArrangement =
            Arrangement.spacedBy(16.dp)
    ) {

        Text(
            "📖 Lesson",
            fontSize = 18.sp
        )

        Text(
            lesson!!.title,
            fontSize = 30.sp
        )

        // Lesson progress

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp
            )
        ) {

            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement =
                    Arrangement.spacedBy(10.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.SpaceBetween
                ) {

                    Text(
                        "📚 Lesson $lessonNumber of $totalLessons",
                        fontSize = 17.sp
                    )

                    Text(
                        "$percentage%",
                        fontSize = 16.sp
                    )
                }

                LinearProgressIndicator(
                    progress = {
                        lessonProgress
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Completion status

        if (progress?.completed == true) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 5.dp
                )
            ) {

                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {

                    Text(
                        "✅ Lesson Completed",
                        fontSize = 19.sp
                    )

                    Text(
                        "⭐ Previous Quiz Score: " +
                                "${progress!!.score}/3",
                        fontSize = 16.sp
                    )
                }
            }
        }

        // Lesson content

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp
            )
        ) {

            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {

                Text(
                    "📚 Lesson Content",
                    fontSize = 21.sp
                )

                Text(
                    lesson!!.content,
                    fontSize = 18.sp,
                    lineHeight = 29.sp
                )
            }
        }

        // Quiz information

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp
            )
        ) {

            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {

                Text(
                    "📝 Ready for the quiz?",
                    fontSize = 20.sp
                )

                Text(
                    if (progress?.completed == true)
                        "You can retake the quiz to improve your score."
                    else
                        "Test what you learned from this lesson.",
                    fontSize = 16.sp
                )
            }
        }

        Button(
            onClick = onStartQuiz,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {

            Text(
                if (progress?.completed == true)
                    "🔄 Retake Quiz"
                else
                    "📝 Start Quiz",
                fontSize = 18.sp
            )
        }

        Button(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {

            Text(
                "⬅️ Back to Lessons",
                fontSize = 16.sp
            )
        }
    }
}