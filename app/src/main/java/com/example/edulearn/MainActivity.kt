package com.example.edulearn

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.edulearn.database.DatabaseSeeder
import com.example.edulearn.ui.theme.EduLearnTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    private var isDarkMode by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        // Firebase App Check
        val firebaseAppCheck = FirebaseAppCheck.getInstance()

        Log.e(
            "EDULEARN_TEST",
            "1. Firebase App Check instance created"
        )

        firebaseAppCheck.installAppCheckProviderFactory(
            DebugAppCheckProviderFactory.getInstance()
        )

        Log.e(
            "EDULEARN_TEST",
            "2. Debug App Check provider installed"
        )

        firebaseAppCheck.getAppCheckToken(true)
            .addOnCompleteListener { task ->

                Log.e(
                    "EDULEARN_TEST",
                    "3. App Check request completed"
                )

                if (task.isSuccessful) {

                    val token = task.result?.token

                    Log.e(
                        "EDULEARN_TEST",
                        "4. DEBUG TOKEN GENERATED: ${token != null}"
                    )

                } else {

                    Log.e(
                        "EDULEARN_TEST",
                        "4. APP CHECK ERROR: ${task.exception?.message}",
                        task.exception
                    )
                }
            }

        // Seed Room Database
        DatabaseSeeder.seed(this)

        setContent {

            EduLearnTheme(
                darkTheme = isDarkMode
            ) {

                var currentScreen by remember {
                    mutableStateOf("login")
                }

                var currentUserId by remember {
                    mutableStateOf(0)
                }

                var selectedSubjectId by remember {
                    mutableStateOf(0)
                }

                var selectedLessonId by remember {
                    mutableStateOf(0)
                }

                var quizScore by remember {
                    mutableStateOf(0)
                }

                when (currentScreen) {

                    // LOGIN
                    "login" -> LoginScreen(
                        onLoginSuccess = { userId ->
                            currentUserId = userId
                            currentScreen = "home"
                        },

                        onRegisterClick = {
                            currentScreen = "register"
                        }
                    )

                    // REGISTER
                    "register" -> RegisterScreen(
                        onRegistrationSuccess = {
                            currentScreen = "login"
                        },

                        onBackToLogin = {
                            currentScreen = "login"
                        }
                    )

                    // HOME
                    "home" -> HomeScreen(
                        userId = currentUserId,

                        onSubjectsClick = {
                            currentScreen = "subjects"
                        },

                        onContinueLearning = { subjectId, lessonId ->
                            selectedSubjectId = subjectId
                            selectedLessonId = lessonId
                            currentScreen = "lesson"
                        },

                        onProgressClick = {
                            currentScreen = "progress"
                        },

                        onAchievementsClick = {
                            currentScreen = "achievements"
                        },

                        onProfileClick = {
                            currentScreen = "profile"
                        },

                        onLogoutClick = {
                            FirebaseAuth.getInstance().signOut()
                            currentUserId = 0
                            currentScreen = "login"
                        }
                    )

                    // SUBJECTS / LEARN
                    "subjects" -> SubjectsScreen(
                        userId = currentUserId,

                        // Existing Python
                        onPythonClick = {
                            selectedSubjectId = 1
                            currentScreen = "lessons"
                        },

                        // Existing C++
                        onCppClick = {
                            selectedSubjectId = 2
                            currentScreen = "lessons"
                        },

                        // Existing Data Structures
                        onDataStructuresClick = {
                            selectedSubjectId = 3
                            currentScreen = "lessons"
                        },

                        // NEW / DYNAMIC SUBJECTS
                        onSubjectClick = { subjectId ->
                            selectedSubjectId = subjectId
                            currentScreen = "lessons"
                        },

                        onBackClick = {
                            currentScreen = "home"
                        },

                        onProgressClick = {
                            currentScreen = "progress"
                        },

                        onProfileClick = {
                            currentScreen = "profile"
                        }
                    )

                    // LESSON LIST
                    "lessons" -> LessonListScreen(
                        subjectId = selectedSubjectId,
                        userId = currentUserId,

                        onLessonClick = { lesson ->
                            selectedLessonId = lesson.id
                            currentScreen = "lesson"
                        },

                        onBackClick = {
                            currentScreen = "subjects"
                        }
                    )

                    // LESSON
                    "lesson" -> LessonScreenLoader(
                        lessonId = selectedLessonId,
                        userId = currentUserId,

                        onStartQuiz = {
                            currentScreen = "quiz"
                        },

                        onBackClick = {
                            currentScreen = "lessons"
                        }
                    )

                    // QUIZ
                    "quiz" -> QuizScreen(
                        lessonId = selectedLessonId,
                        userId = currentUserId,

                        onQuizFinished = { score ->
                            quizScore = score
                            currentScreen = "result"
                        }
                    )

                    // QUIZ RESULT
                    "result" -> QuizResultScreen(
                        score = quizScore,
                        lessonId = selectedLessonId,
                        userId = currentUserId,

                        onRetryQuiz = {
                            currentScreen = "quiz"
                        },

                        onBackToSubjects = {
                            currentScreen = "subjects"
                        },

                        onBackToHome = {
                            currentScreen = "home"
                        }
                    )

                    // PROGRESS
                    "progress" -> ProgressScreen(
                        userId = currentUserId,

                        onBackClick = {
                            currentScreen = "home"
                        },

                        onSubjectsClick = {
                            currentScreen = "subjects"
                        },

                        onProfileClick = {
                            currentScreen = "profile"
                        }
                    )

                    // ACHIEVEMENTS
                    "achievements" -> AchievementsScreen(
                        userId = currentUserId,

                        onBackClick = {
                            currentScreen = "home"
                        }
                    )

                    // PROFILE
                    "profile" -> ProfileScreen(
                        userId = currentUserId,

                        onBackClick = {
                            currentScreen = "home"
                        },

                        onLogoutClick = {
                            FirebaseAuth.getInstance().signOut()
                            currentUserId = 0
                            currentScreen = "login"
                        },

                        onSubjectsClick = {
                            currentScreen = "subjects"
                        },

                        onProgressClick = {
                            currentScreen = "progress"
                        },

                        onManageContentClick = {
                            currentScreen = "content_management"
                        },

                        isDarkMode = isDarkMode,

                        onDarkModeChange = { enabled ->
                            isDarkMode = enabled
                        }
                    )

                    // CONTENT MANAGEMENT
                    "content_management" -> ContentManagementScreen(
                        onBackClick = {
                            currentScreen = "profile"
                        }
                    )

                    // DEFAULT
                    else -> {
                        currentScreen = "login"
                    }
                }
            }
        }
    }
}