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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.edulearn.database.Lesson

@Composable
fun LessonScreen(
    lesson: Lesson,
    onQuizClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FC))
            .verticalScroll(rememberScrollState())
            .padding(18.dp),

        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // ==========================================
        // HEADER
        // ==========================================

        Text(
            text = "📖 Lesson",
            fontSize = 16.sp,
            color = Color(0xFF1E3A8A),
            fontWeight = FontWeight.Medium
        )

        Text(
            text = lesson.title,
            fontSize = 28.sp,
            lineHeight = 34.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Read and understand the concepts before attempting the quiz.",
            fontSize = 14.sp,
            color = Color.Gray
        )

        // ==========================================
        // LESSON CONTENT CARD
        // ==========================================

        Card(
            modifier = Modifier.fillMaxWidth(),

            shape = RoundedCornerShape(22.dp),

            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),

            elevation = CardDefaults.cardElevation(
                defaultElevation = 3.dp
            )
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "📚",
                        fontSize = 26.sp
                    )

                    Spacer(
                        modifier = Modifier.padding(6.dp)
                    )

                    Text(
                        text = "Lesson Content",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(
                    modifier = Modifier.height(14.dp)
                )

                Text(
                    text = lesson.content,
                    fontSize = 17.sp,
                    lineHeight = 28.sp,
                    color = Color(0xFF263238)
                )
            }
        }

        // ==========================================
        // QUIZ INFORMATION
        // ==========================================

        Card(
            modifier = Modifier.fillMaxWidth(),

            shape = RoundedCornerShape(18.dp),

            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE8EEF9)
            )
        ) {

            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {

                Text(
                    text = "📝 Ready for the Quiz?",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E3A8A)
                )

                Text(
                    text = "Test your understanding and earn points by completing the quiz.",
                    fontSize = 14.sp,
                    color = Color(0xFF374151)
                )
            }
        }

        // ==========================================
        // QUIZ BUTTON
        // ==========================================

        Button(
            onClick = onQuizClick,

            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),

            shape = RoundedCornerShape(14.dp)
        ) {

            Text(
                text = "📝 Take Quiz",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // ==========================================
        // TIP
        // ==========================================

        Text(
            text = "💡 Tip: Read the lesson carefully before starting the quiz.",
            fontSize = 13.sp,
            color = Color.Gray,
            modifier = Modifier.padding(
                start = 4.dp,
                end = 4.dp
            )
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )
    }
}