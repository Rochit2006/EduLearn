package com.example.edulearn

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
import com.example.edulearn.database.Progress

@Composable
fun AchievementsScreen(
    userId: Int,
    onBackClick: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current

    var progressList by remember {
        mutableStateOf(emptyList<Progress>())
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(userId) {

        val database = EduLearnDatabase.getDatabase(context)

        progressList =
            database.eduLearnDao()
                .getProgressByUser(userId)

        isLoading = false
    }

    if (isLoading) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                "Loading achievements...",
                fontSize = 20.sp,
                modifier = Modifier.padding(20.dp)
            )
        }

        return
    }

    val completedLessons =
        progressList.count { it.completed }

    val totalPoints =
        progressList.sumOf { it.score }

    val perfectScores =
        progressList.count { it.score == 3 }

    val achievements = listOf(

        Achievement(
            icon = "🌱",
            title = "First Step",
            description = "Complete your first lesson",
            current = completedLessons,
            target = 1
        ),

        Achievement(
            icon = "📚",
            title = "Quick Learner",
            description = "Complete 5 lessons",
            current = completedLessons,
            target = 5
        ),

        Achievement(
            icon = "🧠",
            title = "Knowledge Seeker",
            description = "Complete 10 lessons",
            current = completedLessons,
            target = 10
        ),

        Achievement(
            icon = "👑",
            title = "Learning Master",
            description = "Complete all 15 lessons",
            current = completedLessons,
            target = 15
        ),

        Achievement(
            icon = "💯",
            title = "Perfect Score",
            description = "Get a perfect quiz score",
            current = perfectScores,
            target = 1
        ),

        Achievement(
            icon = "⭐",
            title = "High Scorer",
            description = "Earn 10 total points",
            current = totalPoints,
            target = 10
        )
    )

    val unlockedCount =
        achievements.count {
            it.current >= it.target
        }

    val totalAchievements =
        achievements.size

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        Text(
            "🏆 Achievements",
            fontSize = 32.sp
        )

        Text(
            "Complete lessons and quizzes to unlock achievements.",
            fontSize = 16.sp
        )

        // Summary

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            )
        ) {

            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Text(
                    "🏆 Achievement Summary",
                    fontSize = 21.sp
                )

                Text(
                    "$unlockedCount / $totalAchievements unlocked",
                    fontSize = 18.sp
                )

                LinearProgressIndicator(
                    progress = {
                        if (totalAchievements > 0) {
                            unlockedCount.toFloat() /
                                    totalAchievements.toFloat()
                        } else {
                            0f
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    "📚 Lessons: $completedLessons",
                    fontSize = 15.sp
                )

                Text(
                    "⭐ Points: $totalPoints",
                    fontSize = 15.sp
                )
            }
        }

        // Achievement cards

        achievements.forEach { achievement ->

            val unlocked =
                achievement.current >= achievement.target

            val achievementProgress =
                if (achievement.target > 0) {
                    (
                            achievement.current.toFloat() /
                                    achievement.target.toFloat()
                            ).coerceAtMost(1f)
                } else {
                    0f
                }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 5.dp
                )
            ) {

                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    Text(
                        if (unlocked)
                            "🔓 ${achievement.icon} ${achievement.title}"
                        else
                            "🔒 ${achievement.icon} ${achievement.title}",
                        fontSize = 20.sp
                    )

                    Text(
                        achievement.description,
                        fontSize = 15.sp
                    )

                    LinearProgressIndicator(
                        progress = {
                            achievementProgress
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        if (unlocked) {
                            "✅ Achievement Unlocked!"
                        } else {
                            "Progress: ${achievement.current}/${achievement.target}"
                        },
                        fontSize = 15.sp
                    )
                }
            }
        }

        Button(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {

            Text(
                "⬅️ Back to Home",
                fontSize = 17.sp
            )
        }
    }
}

private data class Achievement(
    val icon: String,
    val title: String,
    val description: String,
    val current: Int,
    val target: Int
)