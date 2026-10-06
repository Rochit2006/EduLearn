package com.example.edulearn

import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend

object GeminiQuizGenerator {

    suspend fun generateQuiz(
        lessonTitle: String,
        lessonContent: String
    ): String {

        val model = Firebase.ai(
            backend = GenerativeBackend.googleAI()
        ).generativeModel(
            "gemini-3.5-flash-lite"
        )

        val prompt = """
            You are an educational quiz generator.

            Create exactly 5 multiple-choice questions from the lesson below.

            Lesson Title:
            $lessonTitle

            Lesson Content:
            $lessonContent

            Rules:
            1. Questions must be based only on the provided lesson content.
            2. Each question must have exactly 4 options.
            3. Only one option must be correct.
            4. Keep questions suitable for college students.
            5. Do not add explanations.
            6. Return ONLY the following format.

            QUESTION 1:
            Question text

            A) Option A
            B) Option B
            C) Option C
            D) Option D

            ANSWER:
            A

            QUESTION 2:
            Question text

            A) Option A
            B) Option B
            C) Option C
            D) Option D

            ANSWER:
            B

            Continue the same format until QUESTION 5.
        """.trimIndent()

        val response = model.generateContent(prompt)

        return response.text ?: ""
    }
}