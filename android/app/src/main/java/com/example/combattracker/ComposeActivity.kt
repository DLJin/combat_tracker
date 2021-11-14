package com.example.combattracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.combattracker.model.Entity
import com.example.combattracker.ui.theme.CombatTrackerTheme

@ExperimentalFoundationApi
class ComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DashboardApp()
        }
    }

    @Composable
    fun DashboardApp() {
        CombatTrackerTheme {
            val allScreens = DashboardScreens.values().toList()
            var currentScreen by rememberSaveable { mutableStateOf(DashboardScreens.CharacterSheet) }
            val navController = rememberNavController()
            val players = createDummyData()
            Scaffold(
                bottomBar = {
                    BottomNavigation {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination
                        allScreens.forEach { screen ->
                            BottomNavigationItem(
                                icon = {Icon(screen.icon, contentDescription = null)},
                                label = {Text(screen.displayName)},
                                selected = currentDestination?.hierarchy?.any {it.route == screen.route} == true,
                                onClick = {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                            inclusive = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                }
            ) { innerPadding ->
                NavHost(navController = navController, startDestination = currentScreen.route, modifier = Modifier.padding(innerPadding)) {
                    composable(DashboardScreens.CharacterSheet.route) { CharacterSheetBody(controller = navController, players = players) }
                    composable(DashboardScreens.PlayerView.route) { PlayerViewBody(controller = navController, players = players) }
                }
            }
        }
    }
}

fun createDummyData(): List<Entity> {
    val ixar = Entity(
        name = "Ixar",
        attributes = Entity.Attributes(13, 12, 8, 10, 15, 14),
        movement = 30,
        healthFactor = 12.0,
        focusFactor = 6.5
    )

    val seren = Entity(
        name = "Seren",
        attributes = Entity.Attributes(8, 12, 15, 14, 13, 12),
        movement = 30,
        healthFactor = 11.0,
        focusFactor = 7.0
    )

    val elmo = Entity(
        name = "Elmo Elless",
        attributes = Entity.Attributes(13, 10, 14, 15, 12, 8),
        movement = 45,
        healthFactor = 13.0,
        focusFactor = 6.0
    )

    val bandit = Entity(
        name = "Bandit",
        attributes = Entity.Attributes(10, 10, 10, 10, 10, 10),
        movement = 30,
        healthFactor = 10.0,
        focusFactor = 5.0
    )

    return listOf(ixar, seren, elmo, bandit)
}