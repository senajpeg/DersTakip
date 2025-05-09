package com.senaaksoy.derstakip.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.senaaksoy.derstakip.components.CourseTopAppBar
import com.senaaksoy.derstakip.screens.AddNoteScreen
import com.senaaksoy.derstakip.screens.CourseDetailScreen
import com.senaaksoy.derstakip.screens.CourseListScreen
import com.senaaksoy.derstakip.screens.EditNoteScreen
import com.senaaksoy.derstakip.screens.StatisticsScreen
import com.senaaksoy.derstakip.viewModel.CourseViewModel

@Composable
fun AppNavigation(viewModel: CourseViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val uistate by viewModel.uiState.collectAsStateWithLifecycle()

    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = currentBackStackEntry?.destination?.route ?: Screen.CourseListScreen.route

    Scaffold(
        topBar = {
            CourseTopAppBar(
                currentRoute = currentRoute,
                navController = navController
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Divider(
                color = Color(0xFFCDBBD0),
                thickness = 4.dp,
                modifier = Modifier.fillMaxWidth()
            )

            NavHost(
                navController = navController,
                startDestination = Screen.CourseListScreen.route,
                modifier = Modifier.fillMaxSize()
            ) {
                composable(route = Screen.CourseListScreen.route) {
                    CourseListScreen(
                        navController = navController,
                        courseList = uistate,
                        currentRoute = currentRoute,
                        courseName = viewModel.inputCourseName,
                        setCourseName = {viewModel.upDateCourseName(it)},
                        saveCourse = {name->viewModel.saveCourse(name)},
                        clearItem = {viewModel.clearCourses()}
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


    }