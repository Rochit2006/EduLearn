package com.example.edulearn

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun EduLearnBottomNavigation(
    selectedTab: Int,
    onHomeClick: () -> Unit,
    onLearnClick: () -> Unit,
    onProgressClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    NavigationBar {

        NavigationBarItem(
            selected = selectedTab == 0,
            onClick = onHomeClick,
            icon = {
                Text("🏠")
            },
            label = {
                Text("Home")
            }
        )

        NavigationBarItem(
            selected = selectedTab == 1,
            onClick = onLearnClick,
            icon = {
                Text("📚")
            },
            label = {
                Text("Learn")
            }
        )

        NavigationBarItem(
            selected = selectedTab == 2,
            onClick = onProgressClick,
            icon = {
                Text("📊")
            },
            label = {
                Text("Progress")
            }
        )

        NavigationBarItem(
            selected = selectedTab == 3,
            onClick = onProfileClick,
            icon = {
                Text("👤")
            },
            label = {
                Text("Profile")
            }
        )
    }
}