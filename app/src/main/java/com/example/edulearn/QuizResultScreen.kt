package com.example.edulearn

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

@Composable
fun QuizResultScreen(
    score: Int,
    lessonId: Int,
    userId: Int,
    onRetryQuiz: () -> Unit,
    onBackToSubjects: () -> Unit,
    onBackToHome: () -> Unit
) {
    val context =
        androidx.compose.ui.platform.LocalContext.current

    var totalQuestions by remember {
        mutableStateOf(0)
    }

    var lessonTitle by remember {
        mutableStateOf("Quiz Result")
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(lessonId) {

        val database =
            EduLearnDatabase.getDatabase(context)

        val dao =
            database.eduLearnDao()

        val questions =
            dao.getQuestionsByLesson(lessonId)

        totalQuestions =
            questions.size

        val lesson =
            dao.getLessonById(lessonId)

        if (lesson != null) {
            lessonTitle =
                lesson.title
        }

        isLoading = false
    }

    if (isLoading) {

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(
                        Color(0xFFF7F9FC)
                    ),

            horizontalAlignment =
                Alignment.CenterHorizontally,

            verticalArrangement =
                Arrangement.Center
        ) {

            Text(
                text = "🎉 Loading result...",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        }

        return
    }

    val percentage =
        if (totalQuestions > 0) {
            (score * 100) /
                    totalQuestions
        } else {
            0
        }

    val progress =
        if (totalQuestions > 0) {
            score.toFloat() /
                    totalQuestions.toFloat()
        } else {
            0f
        }

    val incorrectAnswers =
        totalQuestions - score

    val performanceMessage =
        when {

            percentage == 100 ->
                "🏆 Perfect Score!\nExcellent work!"

            percentage >= 75 ->
                "🌟 Great job!\nKeep learning!"

            percentage >= 50 ->
                "👍 Good effort!\nYou can improve!"

            else ->
                "📚 Keep practicing!\nTry the quiz again!"
        }

    val performanceColor =
        when {

            percentage == 100 ->
                Color(0xFF2E7D32)

            percentage >= 75 ->
                Color(0xFF1565C0)

            percentage >= 50 ->
                Color(0xFFEF6C00)

            else ->
                Color(0xFFC62828)
        }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(
                    Color(0xFFF7F9FC)
                )
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(18.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.spacedBy(14.dp)
    ) {

        // ==========================================
        // HEADER
        // ==========================================

        Text(
            text = "🎉 Quiz Completed!",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = lessonTitle,
            fontSize = 16.sp,
            color = Color.Gray
        )

        Spacer(
            modifier =
                Modifier.height(4.dp)
        )

        // ==========================================
        // SCORE CARD
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
                    Modifier.padding(24.dp),

                horizontalAlignment =
                    Alignment.CenterHorizontally,

                verticalArrangement =
                    Arrangement.spacedBy(10.dp)
            ) {

                Text(
                    text = "⭐ Your Score",

                    color = Color.White,

                    fontSize = 20.sp,

                    fontWeight =
                        FontWeight.Bold
                )

                Text(
                    text =
                        "$score / $totalQuestions",

                    color = Color.White,

                    fontSize = 42.sp,

                    fontWeight =
                        FontWeight.Bold
                )

                Text(
                    text = "$percentage%",

                    color =
                        Color.White.copy(
                            alpha = 0.9f
                        ),

                    fontSize = 23.sp,

                    fontWeight =
                        FontWeight.Medium
                )

                LinearProgressIndicator(

                    progress = {
                        progress
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
        // PERFORMANCE CARD
        // ==========================================

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
                    Modifier
                        .fillMaxWidth()
                        .padding(20.dp),

                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Text(
                    text =
                        performanceMessage,

                    fontSize = 20.sp,

                    lineHeight = 28.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        performanceColor
                )
            }
        }

        // ==========================================
        // RESULT SUMMARY
        // ==========================================

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

                verticalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {

                Text(
                    text = "📊 Result Summary",

                    fontSize = 20.sp,

                    fontWeight =
                        FontWeight.Bold
                )

                ResultRow(
                    label = "Questions",
                    value =
                        totalQuestions.toString()
                )

                ResultRow(
                    label = "Correct Answers",
                    value =
                        score.toString()
                )

                ResultRow(
                    label = "Incorrect Answers",
                    value =
                        incorrectAnswers.toString()
                )

                ResultRow(
                    label = "Points Earned",
                    value =
                        "⭐ $score"
                )
            }
        }

        // ==========================================
        // ACTION BUTTONS
        // ==========================================

        Button(
            onClick = onRetryQuiz,

            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(52.dp),

            shape =
                RoundedCornerShape(14.dp)
        ) {

            Text(
                text = "🔄 Retry Quiz",

                fontSize = 16.sp,

                fontWeight =
                    FontWeight.Bold
            )
        }

        OutlinedButton(
            onClick =
                onBackToSubjects,

            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(52.dp),

            shape =
                RoundedCornerShape(14.dp)
        ) {

            Text(
                text = "📚 Back to Subjects",

                fontSize = 16.sp,

                fontWeight =
                    FontWeight.Medium
            )
        }

        OutlinedButton(
            onClick =
                onBackToHome,

            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(52.dp),

            shape =
                RoundedCornerShape(14.dp)
        ) {

            Text(
                text = "🏠 Back to Home",

                fontSize = 16.sp,

                fontWeight =
                    FontWeight.Medium
            )
        }

        Spacer(
            modifier =
                Modifier.height(8.dp)
        )
    }
}

@Composable
private fun ResultRow(
    label: String,
    value: String
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
            text = label,

            fontSize = 15.sp,

            color = Color.Gray
        )

        Text(
            text = value,

            fontSize = 16.sp,

            fontWeight =
                FontWeight.Bold
        )
    }
}