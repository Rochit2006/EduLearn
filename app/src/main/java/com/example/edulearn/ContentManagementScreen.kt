package com.example.edulearn

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.edulearn.database.EduLearnDatabase
import com.example.edulearn.database.Lesson
import com.example.edulearn.database.Question
import com.example.edulearn.database.Subject
import kotlinx.coroutines.launch

@Composable
fun ContentManagementScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var subjectName by remember { mutableStateOf("") }
    var lessonTitle by remember { mutableStateOf("") }
    var lessonContent by remember { mutableStateOf("") }

    var subjects by remember { mutableStateOf<List<Subject>>(emptyList()) }
    var selectedSubjectId by remember { mutableStateOf(0) }
    var selectedSubjectName by remember { mutableStateOf("") }
    var dropdownExpanded by remember { mutableStateOf(false) }

    var message by remember { mutableStateOf("") }
    var isGenerating by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        try {
            val db = EduLearnDatabase.getDatabase(context)
            val dao = db.eduLearnDao()

            subjects = dao.getAllSubjects()

            if (subjects.isNotEmpty()) {
                selectedSubjectId = subjects.first().id
                selectedSubjectName = subjects.first().name
            }
        } catch (e: Exception) {
            message = "❌ Failed to load subjects."
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "⚙️ Manage Learning Content",
            fontSize = 26.sp
        )

        Text(
            text = "Add subjects, lessons and generate quizzes using Gemini AI.",
            fontSize = 15.sp
        )

        // ADD SUBJECT
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Text(
                    text = "📚 Add New Subject",
                    fontSize = 21.sp
                )

                OutlinedTextField(
                    value = subjectName,
                    onValueChange = {
                        subjectName = it
                        message = ""
                    },
                    label = {
                        Text("Subject Name")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Button(
                    onClick = {

                        if (subjectName.isBlank()) {
                            message = "⚠️ Enter a subject name."
                            return@Button
                        }

                        scope.launch {

                            try {

                                val db = EduLearnDatabase.getDatabase(context)
                                val dao = db.eduLearnDao()

                                val existingSubjects = dao.getAllSubjects()

                                val existing = existingSubjects.firstOrNull {
                                    it.name.equals(
                                        subjectName.trim(),
                                        ignoreCase = true
                                    )
                                }

                                if (existing != null) {

                                    selectedSubjectId = existing.id
                                    selectedSubjectName = existing.name

                                    message =
                                        "⚠️ Subject already exists. It has been selected."

                                } else {

                                    dao.insertSubject(
                                        Subject(
                                            name = subjectName.trim()
                                        )
                                    )

                                    subjects = dao.getAllSubjects()

                                    val newSubject = subjects.lastOrNull()

                                    if (newSubject != null) {
                                        selectedSubjectId = newSubject.id
                                        selectedSubjectName = newSubject.name
                                    }

                                    subjectName = ""

                                    message =
                                        "✅ Subject added successfully."
                                }

                            } catch (e: Exception) {

                                message =
                                    "❌ Failed to add subject: ${e.message}"
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "➕ Add Subject",
                        fontSize = 17.sp
                    )
                }
            }
        }

        // SELECT SUBJECT
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {

            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Text(
                    text = "🎯 Select Subject",
                    fontSize = 21.sp
                )

                if (subjects.isEmpty()) {

                    Text(
                        text = "No subjects available. Add a subject first.",
                        fontSize = 14.sp
                    )

                } else {

                    OutlinedTextField(
                        value = selectedSubjectName,
                        onValueChange = {},
                        readOnly = true,
                        label = {
                            Text("Choose Subject")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                dropdownExpanded = true
                            },
                        singleLine = true
                    )

                    DropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = {
                            dropdownExpanded = false
                        }
                    ) {

                        subjects.forEach { subject ->

                            DropdownMenuItem(
                                text = {
                                    Text(subject.name)
                                },
                                onClick = {

                                    selectedSubjectId = subject.id
                                    selectedSubjectName = subject.name

                                    dropdownExpanded = false

                                    message =
                                        "✅ ${subject.name} selected."
                                }
                            )
                        }
                    }
                }
            }
        }

        // ADD LESSON
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {

            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Text(
                    text = "📖 Add Lesson Content",
                    fontSize = 21.sp
                )

                Text(
                    text = if (selectedSubjectName.isNotEmpty()) {
                        "Selected Subject: $selectedSubjectName"
                    } else {
                        "Select a subject first"
                    },
                    fontSize = 14.sp
                )

                OutlinedTextField(
                    value = lessonTitle,
                    onValueChange = {
                        lessonTitle = it
                        message = ""
                    },
                    label = {
                        Text("Lesson Title")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = lessonContent,
                    onValueChange = {
                        lessonContent = it
                        message = ""
                    },
                    label = {
                        Text("Lesson Content")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 8
                )

                // SAVE LESSON
                Button(
                    onClick = {

                        if (selectedSubjectId == 0) {
                            message = "⚠️ First select a subject."
                            return@Button
                        }

                        if (lessonTitle.isBlank()) {
                            message = "⚠️ Enter a lesson title."
                            return@Button
                        }

                        if (lessonContent.isBlank()) {
                            message = "⚠️ Enter lesson content."
                            return@Button
                        }

                        scope.launch {

                            try {

                                val db =
                                    EduLearnDatabase.getDatabase(context)

                                val dao = db.eduLearnDao()

                                dao.insertLesson(
                                    Lesson(
                                        subjectId = selectedSubjectId,
                                        title = lessonTitle.trim(),
                                        content = lessonContent.trim()
                                    )
                                )

                                message =
                                    "✅ Lesson added successfully to $selectedSubjectName."

                            } catch (e: Exception) {

                                message =
                                    "❌ Failed to add lesson: ${e.message}"
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {

                    Text(
                        text = "➕ Add Lesson",
                        fontSize = 17.sp
                    )
                }

                // GENERATE QUIZ
                Button(
                    onClick = {

                        if (lessonTitle.isBlank()) {
                            message = "⚠️ Enter lesson title first."
                            return@Button
                        }

                        if (lessonContent.isBlank()) {
                            message = "⚠️ Enter lesson content first."
                            return@Button
                        }

                        if (selectedSubjectId == 0) {
                            message = "⚠️ Select a subject first."
                            return@Button
                        }

                        scope.launch {

                            try {

                                isGenerating = true

                                message =
                                    "🤖 Gemini is generating your quiz..."

                                val db =
                                    EduLearnDatabase.getDatabase(context)

                                val dao = db.eduLearnDao()

                                // Save lesson first
                                val lessonId = dao.insertLesson(
                                    Lesson(
                                        subjectId = selectedSubjectId,
                                        title = lessonTitle.trim(),
                                        content = lessonContent.trim()
                                    )
                                ).toInt()

                                // Generate quiz using Gemini
                                val generatedQuiz =
                                    GeminiQuizGenerator.generateQuiz(
                                        lessonTitle = lessonTitle.trim(),
                                        lessonContent = lessonContent.trim()
                                    )

                                if (generatedQuiz.isBlank()) {

                                    message =
                                        "❌ Gemini returned an empty quiz."

                                    return@launch
                                }

                                val questions =
                                    parseGeneratedQuestions(
                                        generatedQuiz,
                                        lessonId
                                    )

                                if (questions.isEmpty()) {

                                    message =
                                        "❌ Quiz was generated, but questions could not be saved."

                                    return@launch
                                }

                                questions.forEach { question ->

                                    dao.insertQuestion(question)
                                }

                                message =
                                    "✅ Lesson saved and ${questions.size} quiz questions generated successfully!"

                                lessonTitle = ""
                                lessonContent = ""

                            } catch (e: Exception) {

                                message =
                                    "❌ Quiz generation failed: ${e.message}"

                            } finally {

                                isGenerating = false
                            }
                        }
                    },
                    enabled = !isGenerating,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {

                    Text(
                        text = if (isGenerating) {
                            "🤖 Generating Quiz..."
                        } else {
                            "🤖 Generate Quiz with Gemini"
                        },
                        fontSize = 17.sp
                    )
                }
            }
        }

        // MESSAGE
        if (message.isNotEmpty()) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            ) {

                Text(
                    text = message,
                    modifier = Modifier.padding(16.dp),
                    fontSize = 15.sp
                )
            }
        }

        // BACK
        Button(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {

            Text(
                text = "← Back",
                fontSize = 17.sp
            )
        }
    }
}

private fun parseGeneratedQuestions(
    text: String,
    lessonId: Int
): List<Question> {

    val questions = mutableListOf<Question>()

    val questionBlocks = text
        .split(Regex("QUESTION\\s+\\d+\\s*:"))
        .map { it.trim() }
        .filter { it.isNotEmpty() }

    for (block in questionBlocks) {

        try {

            val questionText =
                block.substringBefore("A)")
                    .trim()

            val optionA =
                block
                    .substringAfter("A)")
                    .substringBefore("B)")
                    .trim()

            val optionB =
                block
                    .substringAfter("B)")
                    .substringBefore("C)")
                    .trim()

            val optionC =
                block
                    .substringAfter("C)")
                    .substringBefore("D)")
                    .trim()

            val optionD =
                block
                    .substringAfter("D)")
                    .substringBefore("ANSWER:")
                    .trim()

            val answer =
                block
                    .substringAfter("ANSWER:")
                    .trim()
                    .uppercase()
                    .firstOrNull()
                    ?.toString()
                    ?: ""

            if (
                questionText.isNotBlank() &&
                optionA.isNotBlank() &&
                optionB.isNotBlank() &&
                optionC.isNotBlank() &&
                optionD.isNotBlank() &&
                answer in listOf("A", "B", "C", "D")
            ) {

                val correctAnswer = when (answer) {
                    "A" -> optionA
                    "B" -> optionB
                    "C" -> optionC
                    else -> optionD
                }

                questions.add(
                    Question(
                        lessonId = lessonId,
                        question = questionText,
                        optionA = optionA,
                        optionB = optionB,
                        optionC = optionC,
                        optionD = optionD,
                        correctAnswer = correctAnswer
                    )
                )
            }

        } catch (_: Exception) {
            // Ignore malformed question blocks
        }
    }

    return questions
}