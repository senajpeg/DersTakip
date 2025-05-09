package com.senaaksoy.derstakip.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.senaaksoy.derstakip.screens.AddNoteScreen
import com.senaaksoy.derstakip.screens.CourseDetailScreen
import com.senaaksoy.derstakip.screens.CourseListScreen
import com.senaaksoy.derstakip.screens.EditNoteScreen
import com.senaaksoy.derstakip.screens.StatisticsScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    Scaffold(
        topBar = {}
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = Screen.CourseListScreen.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(route = Screen.CourseListScreen.route) {
                CourseListScreen(
                    navController = navController
                )

            }
            composable(route = Screen.CourseDetailScreen.route) {
                CourseDetailScreen(
                    navController = navController
                )
            }
            composable(route = Screen.AddNoteScreen.route) {
                AddNoteScreen(
                    navController = navController
                )

            }
            composable(route = Screen.EditNoteScreen.route) {
                EditNoteScreen(
                    navController = navController
                )

            }
            composable(route = Screen.StatisticsScreen.route) {
                StatisticsScreen(
                    navController = navController
                )
            }


        }

    }


}