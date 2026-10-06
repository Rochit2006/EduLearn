package com.example.edulearn.database

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object DatabaseSeeder {

    fun seed(context: Context) {

        CoroutineScope(Dispatchers.IO).launch {

            val database = EduLearnDatabase.getDatabase(context)
            val dao = database.eduLearnDao()

            // Check if subjects already exist
            val existingSubjects = dao.getAllSubjects()

            if (existingSubjects.isNotEmpty()) {
                return@launch
            }

            // =========================
            // PYTHON
            // =========================

            val pythonId = dao.insertSubject(
                Subject(name = "🐍 Python")
            ).toInt()

            val pythonLessons = listOf(
                Lesson(
                    subjectId = pythonId,
                    title = "Introduction",
                    content = """
                        Python is a high-level programming language.

                        It is easy to learn and widely used in:
                        • Web development
                        • Data Science
                        • Artificial Intelligence
                        • Automation

                        Example:

                        print("Hello World")
                    """.trimIndent()
                ),

                Lesson(
                    subjectId = pythonId,
                    title = "Variables & Data Types",
                    content = """
                        A variable stores data.

                        Example:

                        name = "Rochit"
                        age = 20

                        Common Python data types:
                        • String
                        • Integer
                        • Float
                        • Boolean
                    """.trimIndent()
                ),

                Lesson(
                    subjectId = pythonId,
                    title = "Conditions",
                    content = """
                        Conditions are used to make decisions.

                        Example:

                        age = 20

                        if age >= 18:
                            print("Adult")
                        else:
                            print("Minor")
                    """.trimIndent()
                ),

                Lesson(
                    subjectId = pythonId,
                    title = "Loops",
                    content = """
                        Loops are used to repeat a block of code.

                        Example:

                        for i in range(5):
                            print(i)

                        Python provides for and while loops.
                    """.trimIndent()
                ),

                Lesson(
                    subjectId = pythonId,
                    title = "Functions",
                    content = """
                        A function is a reusable block of code.

                        Example:

                        def greet():
                            print("Hello")

                        Functions help us organize our programs.
                    """.trimIndent()
                )
            )

            for (lesson in pythonLessons) {

                val lessonId = dao.insertLesson(lesson).toInt()

                addPythonQuestions(dao, lessonId, lesson.title)
            }


            // =========================
            // C++
            // =========================

            val cppId = dao.insertSubject(
                Subject(name = "💻 C++")
            ).toInt()

            val cppLessons = listOf(
                Lesson(
                    subjectId = cppId,
                    title = "Introduction",
                    content = """
                        C++ is a general-purpose programming language.

                        It is widely used for:
                        • Competitive Programming
                        • Software Development
                        • Game Development
                        • System Programming
                    """.trimIndent()
                ),

                Lesson(
                    subjectId = cppId,
                    title = "Variables & Data Types",
                    content = """
                        Variables store values.

                        Example:

                        int age = 20;
                        float marks = 85.5;
                        char grade = 'A';

                        Common data types include:
                        int, float, double, char and bool.
                    """.trimIndent()
                ),

                Lesson(
                    subjectId = cppId,
                    title = "Conditions",
                    content = """
                        Conditions allow a program to make decisions.

                        Example:

                        if (age >= 18) {
                            cout << "Adult";
                        } else {
                            cout << "Minor";
                        }
                    """.trimIndent()
                ),

                Lesson(
                    subjectId = cppId,
                    title = "Loops",
                    content = """
                        Loops repeat a block of code.

                        Common C++ loops:
                        • for
                        • while
                        • do-while
                    """.trimIndent()
                ),

                Lesson(
                    subjectId = cppId,
                    title = "Functions",
                    content = """
                        Functions are reusable blocks of code.

                        Example:

                        int add(int a, int b) {
                            return a + b;
                        }
                    """.trimIndent()
                )
            )

            for (lesson in cppLessons) {

                val lessonId = dao.insertLesson(lesson).toInt()

                addCppQuestions(dao, lessonId, lesson.title)
            }


            // =========================
            // DATA STRUCTURES
            // =========================

            val dsId = dao.insertSubject(
                Subject(name = "🧠 Data Structures")
            ).toInt()

            val dsLessons = listOf(
                Lesson(
                    subjectId = dsId,
                    title = "Introduction",
                    content = """
                        A data structure is a way of organizing and storing data.

                        Examples:
                        • Array
                        • Linked List
                        • Stack
                        • Queue
                        • Tree
                    """.trimIndent()
                ),

                Lesson(
                    subjectId = dsId,
                    title = "Arrays",
                    content = """
                        An array stores multiple values of the same type.

                        Example:

                        int arr[5] = {1, 2, 3, 4, 5};

                        Array elements are accessed using an index.
                    """.trimIndent()
                ),

                Lesson(
                    subjectId = dsId,
                    title = "Linked List",
                    content = """
                        A linked list is a collection of nodes.

                        Each node contains:
                        • Data
                        • Link to another node

                        Linked lists can grow dynamically.
                    """.trimIndent()
                ),

                Lesson(
                    subjectId = dsId,
                    title = "Stack & Queue",
                    content = """
                        Stack follows LIFO:
                        Last In, First Out.

                        Queue follows FIFO:
                        First In, First Out.
                    """.trimIndent()
                ),

                Lesson(
                    subjectId = dsId,
                    title = "Trees",
                    content = """
                        A tree is a hierarchical data structure.

                        A tree contains nodes connected by edges.

                        The top node is called the root.
                    """.trimIndent()
                )
            )

            for (lesson in dsLessons) {

                val lessonId = dao.insertLesson(lesson).toInt()

                addDataStructureQuestions(
                    dao,
                    lessonId,
                    lesson.title
                )
            }
        }
    }


    // =====================================
    // PYTHON QUESTIONS
    // =====================================

    private suspend fun addPythonQuestions(
        dao: EduLearnDao,
        lessonId: Int,
        title: String
    ) {

        when (title) {

            "Introduction" -> {

                dao.insertQuestion(
                    Question(
                        lessonId = lessonId,
                        question = "Which language is known for its simple syntax?",
                        optionA = "Python",
                        optionB = "HTML",
                        optionC = "SQL",
                        optionD = "CSS",
                        correctAnswer = "Python"
                    )
                )

                dao.insertQuestion(
                    Question(
                        lessonId = lessonId,
                        question = "Which function prints output in Python?",
                        optionA = "echo()",
                        optionB = "print()",
                        optionC = "display()",
                        optionD = "show()",
                        correctAnswer = "print()"
                    )
                )

                dao.insertQuestion(
                    Question(
                        lessonId = lessonId,
                        question = "Python is widely used in which field?",
                        optionA = "Data Science",
                        optionB = "Only hardware",
                        optionC = "Only networking",
                        optionD = "None",
                        correctAnswer = "Data Science"
                    )
                )
            }

            "Variables & Data Types" -> {

                dao.insertQuestion(
                    Question(
                        lessonId = lessonId,
                        question = "Which data type stores whole numbers?",
                        optionA = "String",
                        optionB = "Integer",
                        optionC = "Boolean",
                        optionD = "List",
                        correctAnswer = "Integer"
                    )
                )

                dao.insertQuestion(
                    Question(
                        lessonId = lessonId,
                        question = "Which symbol is used to assign a value?",
                        optionA = "=",
                        optionB = "==",
                        optionC = "!=",
                        optionD = "<=",
                        correctAnswer = "="
                    )
                )

                dao.insertQuestion(
                    Question(
                        lessonId = lessonId,
                        question = "Which type stores True or False?",
                        optionA = "Integer",
                        optionB = "Float",
                        optionC = "Boolean",
                        optionD = "String",
                        correctAnswer = "Boolean"
                    )
                )
            }

            "Conditions" -> {

                dao.insertQuestion(
                    Question(
                        lessonId = lessonId,
                        question = "Which keyword starts a condition in Python?",
                        optionA = "if",
                        optionB = "when",
                        optionC = "check",
                        optionD = "condition",
                        correctAnswer = "if"
                    )
                )

                dao.insertQuestion(
                    Question(
                        lessonId = lessonId,
                        question = "Which keyword is used when the if condition is false?",
                        optionA = "otherwise",
                        optionB = "else",
                        optionC = "false",
                        optionD = "default",
                        correctAnswer = "else"
                    )
                )

                dao.insertQuestion(
                    Question(
                        lessonId = lessonId,
                        question = "What does == compare?",
                        optionA = "Values",
                        optionB = "Variables only",
                        optionC = "Loops",
                        optionD = "Functions",
                        correctAnswer = "Values"
                    )
                )
            }

            "Loops" -> {

                dao.insertQuestion(
                    Question(
                        lessonId = lessonId,
                        question = "Which loop is commonly used with range()?",
                        optionA = "for",
                        optionB = "switch",
                        optionC = "if",
                        optionD = "try",
                        correctAnswer = "for"
                    )
                )

                dao.insertQuestion(
                    Question(
                        lessonId = lessonId,
                        question = "Which loop continues while a condition is true?",
                        optionA = "for",
                        optionB = "while",
                        optionC = "if",
                        optionD = "switch",
                        correctAnswer = "while"
                    )
                )

                dao.insertQuestion(
                    Question(
                        lessonId = lessonId,
                        question = "What is used to stop a loop?",
                        optionA = "stop",
                        optionB = "exit",
                        optionC = "break",
                        optionD = "end",
                        correctAnswer = "break"
                    )
                )
            }

            "Functions" -> {

                dao.insertQuestion(
                    Question(
                        lessonId = lessonId,
                        question = "Which keyword defines a function in Python?",
                        optionA = "function",
                        optionB = "def",
                        optionC = "fun",
                        optionD = "define",
                        correctAnswer = "def"
                    )
                )

                dao.insertQuestion(
                    Question(
                        lessonId = lessonId,
                        question = "Why are functions useful?",
                        optionA = "Code reuse",
                        optionB = "They delete code",
                        optionC = "They stop programs",
                        optionD = "None",
                        correctAnswer = "Code reuse"
                    )
                )

                dao.insertQuestion(
                    Question(
                        lessonId = lessonId,
                        question = "Which keyword returns a value?",
                        optionA = "send",
                        optionB = "return",
                        optionC = "output",
                        optionD = "give",
                        correctAnswer = "return"
                    )
                )
            }
        }
    }


    // =====================================
    // C++ QUESTIONS
    // =====================================

    private suspend fun addCppQuestions(
        dao: EduLearnDao,
        lessonId: Int,
        title: String
    ) {

        val questions = when (title) {

            "Introduction" -> listOf(
                Question(
                    lessonId = lessonId,
                    question = "C++ is a:",
                    optionA = "Programming language",
                    optionB = "Database",
                    optionC = "Browser",
                    optionD = "Operating system",
                    correctAnswer = "Programming language"
                ),
                Question(
                    lessonId = lessonId,
                    question = "C++ is commonly used in:",
                    optionA = "Game development",
                    optionB = "Only documents",
                    optionC = "Only email",
                    optionD = "None",
                    correctAnswer = "Game development"
                ),
                Question(
                    lessonId = lessonId,
                    question = "Which language is C++ based on?",
                    optionA = "C",
                    optionB = "Python",
                    optionC = "Java",
                    optionD = "HTML",
                    correctAnswer = "C"
                )
            )

            "Variables & Data Types" -> listOf(
                Question(
                    lessonId = lessonId,
                    question = "Which type stores whole numbers in C++?",
                    optionA = "int",
                    optionB = "float",
                    optionC = "char",
                    optionD = "bool",
                    correctAnswer = "int"
                ),
                Question(
                    lessonId = lessonId,
                    question = "Which type stores a single character?",
                    optionA = "int",
                    optionB = "char",
                    optionC = "float",
                    optionD = "double",
                    correctAnswer = "char"
                ),
                Question(
                    lessonId = lessonId,
                    question = "Which symbol assigns a value?",
                    optionA = "=",
                    optionB = "==",
                    optionC = "!=",
                    optionD = "<",
                    correctAnswer = "="
                )
            )

            "Conditions" -> listOf(
                Question(
                    lessonId = lessonId,
                    question = "Which keyword is used for a condition?",
                    optionA = "if",
                    optionB = "check",
                    optionC = "when",
                    optionD = "condition",
                    correctAnswer = "if"
                ),
                Question(
                    lessonId = lessonId,
                    question = "Which keyword executes when if is false?",
                    optionA = "else",
                    optionB = "otherwise",
                    optionC = "false",
                    optionD = "default",
                    correctAnswer = "else"
                ),
                Question(
                    lessonId = lessonId,
                    question = "Which operator checks equality?",
                    optionA = "=",
                    optionB = "==",
                    optionC = "!=",
                    optionD = ">",
                    correctAnswer = "=="
                )
            )

            "Loops" -> listOf(
                Question(
                    lessonId = lessonId,
                    question = "Which loop is commonly used for a known number of repetitions?",
                    optionA = "for",
                    optionB = "if",
                    optionC = "switch",
                    optionD = "class",
                    correctAnswer = "for"
                ),
                Question(
                    lessonId = lessonId,
                    question = "Which loop checks a condition before execution?",
                    optionA = "while",
                    optionB = "do-while",
                    optionC = "class",
                    optionD = "if",
                    correctAnswer = "while"
                ),
                Question(
                    lessonId = lessonId,
                    question = "Which statement stops a loop?",
                    optionA = "break",
                    optionB = "stop",
                    optionC = "exitloop",
                    optionD = "end",
                    correctAnswer = "break"
                )
            )

            else -> listOf(
                Question(
                    lessonId = lessonId,
                    question = "Which keyword is used to define a function return type?",
                    optionA = "int",
                    optionB = "function",
                    optionC = "def",
                    optionD = "fun",
                    correctAnswer = "int"
                ),
                Question(
                    lessonId = lessonId,
                    question = "Can a function return a value?",
                    optionA = "Yes",
                    optionB = "No",
                    optionC = "Never",
                    optionD = "Only in Python",
                    correctAnswer = "Yes"
                ),
                Question(
                    lessonId = lessonId,
                    question = "Why are functions useful?",
                    optionA = "Code reuse",
                    optionB = "Delete variables",
                    optionC = "Stop programs",
                    optionD = "None",
                    correctAnswer = "Code reuse"
                )
            )
        }

        questions.forEach {
            dao.insertQuestion(it)
        }
    }


    // =====================================
    // DATA STRUCTURES QUESTIONS
    // =====================================

    private suspend fun addDataStructureQuestions(
        dao: EduLearnDao,
        lessonId: Int,
        title: String
    ) {

        val questions = when (title) {

            "Introduction" -> listOf(
                Question(
                    lessonId = lessonId,
                    question = "What is a data structure?",
                    optionA = "Way to organize data",
                    optionB = "Programming language",
                    optionC = "Operating system",
                    optionD = "Compiler",
                    correctAnswer = "Way to organize data"
                ),
                Question(
                    lessonId = lessonId,
                    question = "Which is a data structure?",
                    optionA = "Array",
                    optionB = "Python",
                    optionC = "HTML",
                    optionD = "Compiler",
                    correctAnswer = "Array"
                ),
                Question(
                    lessonId = lessonId,
                    question = "Which structure is hierarchical?",
                    optionA = "Tree",
                    optionB = "Array",
                    optionC = "Stack",
                    optionD = "Queue",
                    correctAnswer = "Tree"
                )
            )

            "Arrays" -> listOf(
                Question(
                    lessonId = lessonId,
                    question = "An array stores:",
                    optionA = "Multiple values",
                    optionB = "Only one value",
                    optionC = "Only functions",
                    optionD = "Only strings",
                    correctAnswer = "Multiple values"
                ),
                Question(
                    lessonId = lessonId,
                    question = "Array indexing commonly starts at:",
                    optionA = "0",
                    optionB = "1",
                    optionC = "-1",
                    optionD = "10",
                    correctAnswer = "0"
                ),
                Question(
                    lessonId = lessonId,
                    question = "Array elements are usually:",
                    optionA = "Same type",
                    optionB = "Always different",
                    optionC = "Only strings",
                    optionD = "Only integers",
                    correctAnswer = "Same type"
                )
            )

            "Linked List" -> listOf(
                Question(
                    lessonId = lessonId,
                    question = "A linked list consists of:",
                    optionA = "Nodes",
                    optionB = "Tables",
                    optionC = "Files",
                    optionD = "Classes only",
                    correctAnswer = "Nodes"
                ),
                Question(
                    lessonId = lessonId,
                    question = "A node contains data and:",
                    optionA = "Link",
                    optionB = "Compiler",
                    optionC = "Loop",
                    optionD = "Function only",
                    correctAnswer = "Link"
                ),
                Question(
                    lessonId = lessonId,
                    question = "Linked lists can grow:",
                    optionA = "Dynamically",
                    optionB = "Never",
                    optionC = "Only once",
                    optionD = "Only during compilation",
                    correctAnswer = "Dynamically"
                )
            )

            "Stack & Queue" -> listOf(
                Question(
                    lessonId = lessonId,
                    question = "Stack follows:",
                    optionA = "LIFO",
                    optionB = "FIFO",
                    optionC = "Random",
                    optionD = "None",
                    correctAnswer = "LIFO"
                ),
                Question(
                    lessonId = lessonId,
                    question = "Queue follows:",
                    optionA = "FIFO",
                    optionB = "LIFO",
                    optionC = "Random",
                    optionD = "None",
                    correctAnswer = "FIFO"
                ),
                Question(
                    lessonId = lessonId,
                    question = "LIFO means:",
                    optionA = "Last In First Out",
                    optionB = "Last In Final Out",
                    optionC = "First In First Out",
                    optionD = "First In Last Out",
                    correctAnswer = "Last In First Out"
                )
            )

            else -> listOf(
                Question(
                    lessonId = lessonId,
                    question = "A tree is a:",
                    optionA = "Hierarchical structure",
                    optionB = "Programming language",
                    optionC = "Database",
                    optionD = "Compiler",
                    correctAnswer = "Hierarchical structure"
                ),
                Question(
                    lessonId = lessonId,
                    question = "The top node of a tree is called:",
                    optionA = "Root",
                    optionB = "Leaf",
                    optionC = "Child",
                    optionD = "Edge",
                    correctAnswer = "Root"
                ),
                Question(
                    lessonId = lessonId,
                    question = "A connection between tree nodes is called:",
                    optionA = "Edge",
                    optionB = "Root",
                    optionC = "Leaf",
                    optionD = "Branch only",
                    correctAnswer = "Edge"
                )
            )
        }

        questions.forEach {
            dao.insertQuestion(it)
        }
    }
}