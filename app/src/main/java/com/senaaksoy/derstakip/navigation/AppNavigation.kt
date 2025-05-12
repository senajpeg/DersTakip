package com.senaaksoy.derstakip.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.senaaksoy.derstakip.components.CourseTopAppBar
import com.senaaksoy.derstakip.screens.AddNoteScreen
import com.senaaksoy.derstakip.screens.CourseDetailScreen
import com.senaaksoy.derstakip.screens.CourseListScreen
import com.senaaksoy.derstakip.screens.EditNoteScreen
import com.senaaksoy.derstakip.screens.StatisticsScreen
import com.senaaksoy.derstakip.viewModel.CourseViewModel
import com.senaaksoy.derstakip.viewModel.NoteViewModel

@Composable
fun AppNavigation(
    courseViewModel: CourseViewModel = hiltViewModel(),
    noteViewModel: NoteViewModel= hiltViewModel()
) {
    val navController = rememberNavController()
    val courseUistate by courseViewModel.uiState.collectAsStateWithLifecycle()
    val noteUiState by noteViewModel.uiState.collectAsStateWithLifecycle()


    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = currentBackStackEntry?.destination?.route ?: Screen.CourseListScreen.route

    var currentCourseName by remember { mutableStateOf<String?>(null) }

    /*val courseToDelete = if (currentRoute.startsWith("CourseDetailScreen")) {
        val courseId = currentBackStackEntry?.arguments?.getInt("courseId")
        uistate.find { it.id == courseId }
    } else null*/


    Scaffold(
        topBar = {
            CourseTopAppBar(
                currentRoute = currentRoute,
                navController = navController,
                courseName = currentCourseName,
                courseToDelete = courseUistate.find { it.name == currentCourseName },
                onDeleteCourse = { course -> courseViewModel.deleteCourse(course) }
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
                        courseList = courseUistate,
                        currentRoute = currentRoute,
                        courseName = courseViewModel.inputCourseName,
                        setCourseName = {courseViewModel.upDateCourseName(it)},
                        saveCourse = {name->courseViewModel.saveCourse(name)},
                        clearItem = {courseViewModel.clearCourses()},
                        upDateCourse = {course-> courseViewModel.upDateCourse(course)}
                    )

                }
                composable(
                    route ="CourseDetailScreen/{courseId}",
                    arguments = listOf(navArgument("courseId"){type=NavType.IntType})
                ) {backStackEntry->
                    val courseId=backStackEntry.arguments?.getInt(("courseId"))
                    val course = courseUistate.find { it.id == courseId }
                    currentCourseName = course?.name
                    CourseDetailScreen(
                        navController = navController,
                        courseId = courseId,
                        noteList = noteUiState
                    )
                }
                composable(
                    route ="AddNoteScreen/{courseId}",
                    arguments = listOf(navArgument("courseId"){type=NavType.IntType})
                ) {backStackEntry->
                    val courseId=backStackEntry.arguments?.getInt(("courseId"))
                    AddNoteScreen(
                        navController = navController,
                        title = noteViewModel.inputTitle,
                        noteContent = noteViewModel.inputNoteContent,
                        setTitle = {noteViewModel.upDateTitle(it)},
                        setNoteContent = {noteViewModel.upDateNoteContent(it)},
                        saveNote = {id,title,content->noteViewModel.saveNote(id,title,content)
                                   noteViewModel.getNotesForCourse(id)},
                        currentRoute = currentRoute,
                        clearItem = {noteViewModel.clearNotes()},
                        courseId = courseId
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