package com.example.combattracker

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import java.lang.IllegalArgumentException

enum class DashboardScreens(
    val icon: ImageVector,
    val route: String
) {
    CharacterSheet(
        icon = Icons.Filled.List,
        route = "characterSheet"
    ),
    PlayerView(
        icon = Icons.Filled.Face,
        route = "playerView"
    );
}