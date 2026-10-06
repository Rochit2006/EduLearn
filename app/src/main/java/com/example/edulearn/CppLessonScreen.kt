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
fun CppLessonScreen(
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
            text = "💻 Introduction to C++",
            fontSize = 28.sp
        )

        Text(
            text = "What is C++?",
            fontSize = 22.sp
        )

        Text(
            text = "C++ is a general-purpose programming language " +
                    "used to develop software, games, operating systems " +
                    "and many other applications.",
            fontSize = 16.sp
        )

        Text(
            text = "⭐ Features of C++",
            fontSize = 22.sp
        )

        Text(
            text = "• Fast and efficient\n" +
                    "• Object-oriented programming\n" +
                    "• Supports procedural programming\n" +
                    "• Provides powerful libraries\n" +
                    "• Widely used for competitive programming",
            fontSize = 16.sp
        )

        Text(
            text = "💻 Simple Example",
            fontSize = 22.sp
        )

        Text(
            text = "```cpp\n" +
                    "#include <iostream>\n\n" +
                    "using namespace std;\n\n" +
                    "int main() {\n" +
                    "    cout << \"Hello, World!\";\n" +
                    "    return 0;\n" +
                    "}",
            fontSize = 16.sp
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