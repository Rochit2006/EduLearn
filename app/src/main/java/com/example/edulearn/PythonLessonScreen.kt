package com.example.edulearn

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PythonLessonScreen(
    onQuizClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "🐍 Introduction to Python",
            fontSize = 28.sp
        )

        Text(
            text = "What is Python?",
            fontSize = 22.sp
        )

        Text(
            text = "Python is a high-level, interpreted programming language. " +
                    "It is easy to learn and is widely used in web development, " +
                    "data science, artificial intelligence and automation.",
            fontSize = 16.sp
        )

        Text(
            text = "⭐ Features of Python",
            fontSize = 22.sp
        )

        Text(
            text = "• Easy to learn\n" +
                    "• Simple and readable syntax\n" +
                    "• Open-source\n" +
                    "• Large number of libraries\n" +
                    "• Used in many different fields",
            fontSize = 16.sp
        )

        Text(
            text = "💻 Simple Example",
            fontSize = 22.sp
        )

        Text(
            text = "print(\"Hello, World!\")",
            fontSize = 18.sp
        )

        Text(
            text = "This program displays Hello, World! on the screen.",
            fontSize = 16.sp
        )

        Button(
            onClick = onQuizClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("📝 Take Quiz")
        }
    }
}