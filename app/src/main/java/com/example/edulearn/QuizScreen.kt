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
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.edulearn.database.EduLearnDatabase
import com.example.edulearn.database.Progress
import kotlinx.coroutines.launch

@Composable
fun QuizScreen(
    lessonId: Int,
    userId: Int,
    onQuizFinished: (Int) -> Unit
) {
    val context =
        androidx.compose.ui.platform.LocalContext.current

    val scope =
        rememberCoroutineScope()

    var questions by remember {
        mutableStateOf(
            emptyList<com.example.edulearn.database.Question>()
        )
    }

    var lessonTitle by remember {
        mutableStateOf("Quiz")
    }

    var currentQuestionIndex by remember {
        mutableStateOf(0)
    }

    var selectedAnswer by remember {
        mutableStateOf<String?>(null)
    }

    var score by remember {
        mutableStateOf(0)
    }

    var message by remember {
        mutableStateOf("")
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(lessonId) {

        val database =
            EduLearnDatabase.getDatabase(context)

        val dao =
            database.eduLearnDao()

        questions =
            dao.getQuestionsByLesson(lessonId)

        val lesson =
            dao.getLessonById(lessonId)

        if (lesson != null) {
            lessonTitle = lesson.title
        }

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
                text = "📝 Loading quiz...",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        }

        return
    }

    if (questions.isEmpty()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF7F9FC))
                .padding(24.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally,

            verticalArrangement =
                Arrangement.Center
        ) {

            Text(
                text = "❌ No questions found",
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text =
                    "There are no quiz questions available for this lesson.",

                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        return
    }

    val question =
        questions[currentQuestionIndex]

    val totalQuestions =
        questions.size

    val questionNumber =
        currentQuestionIndex + 1

    val progress =
        questionNumber.toFloat() /
                totalQuestions.toFloat()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FC))
            .verticalScroll(
                rememberScrollState()
            )
            .padding(18.dp),

        verticalArrangement =
            Arrangement.spacedBy(14.dp)
    ) {

        // ==========================================
        // HEADER
        // ==========================================

        Text(
            text = "📝 Quiz",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = lessonTitle,
            fontSize = 15.sp,
            color = Color.Gray
        )

        // ==========================================
        // PROGRESS CARD
        // ==========================================

        Card(
            modifier = Modifier.fillMaxWidth(),

            shape =
                RoundedCornerShape(22.dp),

            colors =
                CardDefaults.cardColors(
                    containerColor =
                        Color(0xFF1E3A8A)
                ),

            elevation =
                CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
        ) {

            Column(
                modifier =
                    Modifier.padding(20.dp),

                verticalArrangement =
                    Arrangement.spacedBy(10.dp)
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
                            "Question $questionNumber of $totalQuestions",

                        color =
                            Color.White,

                        fontSize = 16.sp,

                        fontWeight =
                            FontWeight.Medium
                    )

                    Text(
                        text =
                            "⭐ $score",

                        color =
                            Color.White,

                        fontSize = 18.sp,

                        fontWeight =
                            FontWeight.Bold
                    )
                }

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

                Text(
                    text =
                        "${(progress * 100).toInt()}% completed",

                    color =
                        Color.White.copy(
                            alpha = 0.8f
                        ),

                    fontSize = 13.sp
                )
            }
        }

        // ==========================================
        // QUESTION CARD
        // ==========================================

        Card(
            modifier =
                Modifier.fillMaxWidth(),

            shape =
                RoundedCornerShape(22.dp),

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
                    Modifier.padding(20.dp)
            ) {

                Text(
                    text =
                        "Question $questionNumber",

                    fontSize = 14.sp,

                    color =
                        Color(0xFF1E3A8A),

                    fontWeight =
                        FontWeight.Bold
                )

                Spacer(
                    modifier =
                        Modifier.height(10.dp)
                )

                Text(
                    text =
                        question.question,

                    fontSize = 20.sp,

                    lineHeight = 29.sp,

                    fontWeight =
                        FontWeight.SemiBold
                )
            }
        }

        // ==========================================
        // OPTIONS
        // ==========================================

        Text(
            text = "Choose your answer",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        QuizOption(
            text = question.optionA,

            selected =
                selectedAnswer ==
                        question.optionA,

            optionLabel = "A",

            onClick = {
                selectedAnswer =
                    question.optionA

                message = ""
            }
        )

        QuizOption(
            text = question.optionB,

            selected =
                selectedAnswer ==
                        question.optionB,

            optionLabel = "B",

            onClick = {
                selectedAnswer =
                    question.optionB

                message = ""
            }
        )

        QuizOption(
            text = question.optionC,

            selected =
                selectedAnswer ==
                        question.optionC,

            optionLabel = "C",

            onClick = {
                selectedAnswer =
                    question.optionC

                message = ""
            }
        )

        QuizOption(
            text = question.optionD,

            selected =
                selectedAnswer ==
                        question.optionD,

            optionLabel = "D",

            onClick = {
                selectedAnswer =
                    question.optionD

                message = ""
            }
        )

        // ==========================================
        // MESSAGE
        // ==========================================

        if (message.isNotEmpty()) {

            Card(
                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(14.dp),

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            Color(0xFFFFF3CD)
                    )
            ) {

                Text(
                    text = message,

                    modifier =
                        Modifier.padding(14.dp),

                    fontSize = 14.sp,

                    color =
                        Color(0xFF664D03)
                )
            }
        }

        // ==========================================
        // NEXT / FINISH BUTTON
        // ==========================================

        Button(
            onClick = {

                if (selectedAnswer == null) {

                    message =
                        "⚠️ Please select an answer."

                    return@Button
                }

                val newScore =
                    if (
                        selectedAnswer ==
                        question.correctAnswer
                    ) {
                        score + 1
                    } else {
                        score
                    }

                if (
                    currentQuestionIndex <
                    questions.size - 1
                ) {

                    score =
                        newScore

                    currentQuestionIndex++

                    selectedAnswer =
                        null

                    message = ""

                } else {

                    score =
                        newScore

                    scope.launch {

                        val database =
                            EduLearnDatabase
                                .getDatabase(context)

                        val dao =
                            database.eduLearnDao()

                        val lesson =
                            dao.getLessonById(
                                lessonId
                            )

                        if (lesson != null) {

                            dao.deleteProgressByUserAndLesson(
                                userId = userId,
                                lessonId = lessonId
                            )

                            dao.saveProgress(
                                Progress(
                                    userId =
                                        userId,

                                    subjectId =
                                        lesson.subjectId,

                                    lessonId =
                                        lessonId,

                                    completed =
                                        true,

                                    score =
                                        newScore
                                )
                            )
                        }

                        onQuizFinished(
                            newScore
                        )
                    }
                }
            },

            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(54.dp),

            shape =
                RoundedCornerShape(14.dp)
        ) {

            Text(
                text =
                    if (
                        currentQuestionIndex ==
                        questions.size - 1
                    )
                        "🏁 Finish Quiz"
                    else
                        "➡️ Next Question",

                fontSize = 17.sp,

                fontWeight =
                    FontWeight.Bold
            )
        }

        Text(
            text =
                "💡 Select one answer and continue to the next question.",

            fontSize = 13.sp,

            color = Color.Gray,

            modifier =
                Modifier.padding(
                    start = 4.dp,
                    end = 4.dp
                )
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )
    }
}

@Composable
private fun QuizOption(
    text: String,
    selected: Boolean,
    optionLabel: String,
    onClick: () -> Unit
) {

    Card(
        onClick = onClick,

        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(16.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    if (selected)
                        Color(0xFFE8EEF9)
                    else
                        Color.White
            ),

        border =
            if (selected)
                androidx.compose.foundation.BorderStroke(
                    2.dp,
                    Color(0xFF1E3A8A)
                )
            else
                null,

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
    ) {

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(12.dp),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Card(
                modifier =
                    Modifier.padding(2.dp),

                shape =
                    RoundedCornerShape(10.dp),

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            if (selected)
                                Color(0xFF1E3A8A)
                            else
                                Color(0xFFE8EEF9)
                    )
            ) {

                Text(
                    text = optionLabel,

                    modifier =
                        Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 7.dp
                        ),

                    color =
                        if (selected)
                            Color.White
                        else
                            Color(0xFF1E3A8A),

                    fontWeight =
                        FontWeight.Bold,

                    fontSize = 14.sp
                )
            }

            RadioButton(
                selected = selected,
                onClick = onClick
            )

            Text(
                text = text,

                fontSize = 16.sp,

                lineHeight = 23.sp,

                modifier =
                    Modifier
                        .weight(1f)
                        .padding(
                            start = 4.dp,
                            end = 4.dp
                        )
            )
        }
    }
}