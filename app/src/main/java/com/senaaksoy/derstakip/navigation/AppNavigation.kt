package com.senaaksoy.derstakip.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
import com.senaaksoy.derstakip.viewModel.StatisticsViewModel

@Composable
fun AppNavigation(
    courseViewModel: CourseViewModel = hiltViewModel(),
    noteViewModel: NoteViewModel = hiltViewModel(),
    statisticsViewModel: StatisticsViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val courseUistate by courseViewModel.uiState.collectAsStateWithLifecycle()
    val noteUiState by noteViewModel.uiState.collectAsStateWithLifecycle()
    val groupedNotes = noteUiState.groupBy { it.title }

    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = currentBackStackEntry?.destination?.route ?: Screen.CourseListScreen.route

    var currentCourseName by remember { mutableStateOf<String?>(null) }

    val elapsedTime by noteViewModel.timerDisplay.collectAsState()
    val formattedTime = noteViewModel.studyTimer.formatTime(elapsedTime)

    val formatTimeFunction: (Long) -> String = { timeMillis ->
        noteViewModel.studyTimer.formatTime(timeMillis)
    }
    val formatDateFunction: (Long) -> String = { timestamp ->
        noteViewModel.formatDate(timestamp)
    }


    val currentTimerState by noteViewModel.timerState.collectAsState()
    val timerStateString = when (currentTimerState) {
        NoteViewModel.TimerState.INITIAL -> "initial"
        NoteViewModel.TimerState.RUNNING -> "running"
        NoteViewModel.TimerState.PAUSED -> "paused"
        NoteViewModel.TimerState.RESET -> "reset"
    }
    val dailyStats by statisticsViewModel.dailyStats.collectAsState()
    val weeklyStats by statisticsViewModel.weeklyStats.collectAsState()
    val monthlyStats by statisticsViewModel.monthlyStats.collectAsState()



    Scaffold(
        topBar = {
            CourseTopAppBar(
                currentRoute = currentRoute,
                navController = navController,
                courseName = currentCourseName,
                courseToDelete = courseUistate.find { it.name == currentCourseName },
                onDeleteCourse = { course ->
                    course.let {
                        noteViewModel.deleteNotesByCourseId(it.id) {
                            courseViewModel.deleteCourse(it)
                        }
                    }
                }

            )
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.CourseListScreen.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            composable(route = Screen.CourseListScreen.route) {
                CourseListScreen(
                    navController = navController,
                    courseList = courseUistate,
                    courseName = courseViewModel.inputCourseName,
                    setCourseName = { courseViewModel.upDateCourseName(it) },
                    saveCourse = { name -> courseViewModel.saveCourse(name) },
                    clearItem = { courseViewModel.clearCourses() },
                    upDateCourse = { course -> courseViewModel.upDateCourse(course) }
                )

            }
            composable(
                route = "CourseDetailScreen/{courseId}",
                arguments = listOf(navArgument("courseId") { type = NavType.IntType })
            ) { backStackEntry ->
                val courseId = backStackEntry.arguments?.getInt(("courseId"))
                LaunchedEffect(courseId) {
                    courseId?.let {
                        noteViewModel.getNotesForCourse(it)
                    }
                }
                val course = courseUistate.find { it.id == courseId }
                currentCourseName = course?.name
                CourseDetailScreen(
                    navController = navController,
                    courseId = courseId,
                    groupedNotes = groupedNotes,
                    clearItem = { noteViewModel.clearNotes() },
                    resetTimer = { noteViewModel.resetTimer() },
                    formatTime = formatTimeFunction,
                    formatDate = formatDateFunction
                )
            }
            composable(
                route = "AddNoteScreen/{courseId}",
                arguments = listOf(navArgument("courseId") { type = NavType.IntType })
            ) { backStackEntry ->
                val courseId = backStackEntry.arguments?.getInt(("courseId"))

                AddNoteScreen(
                    navController = navController,
                    title = noteViewModel.inputTitle,
                    noteContent = noteViewModel.inputNoteContent,
                    setTitle = { noteViewModel.upDateTitle(it) },
                    setNoteContent = { noteViewModel.upDateNoteContent(it) },
                    saveNote = { id, title, content ->
                        noteViewModel.saveNote(id, title, content)
                        noteViewModel.getNotesForCourse(id)
                    },
                    currentRoute = currentRoute,
                    clearItem = { noteViewModel.clearNotes() },
                    courseId = courseId,
                    formattedTime = formattedTime,
                    startTimer = { noteViewModel.startTimer() },
                    resetTimer = { noteViewModel.resetTimer() },
                    resumeTimer = { noteViewModel.resumeTimer() },
                    pauseTimer = { noteViewModel.pauseTimer() },
                    timerState = timerStateString
                )

            }
            composable(
                route = "EditNoteScreen/{courseId}/{noteId}",
                arguments = listOf(
                    navArgument("courseId") { type = NavType.IntType },
                    navArgument("noteId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val courseId = backStackEntry.arguments?.getInt(("courseId"))
                val noteId = backStackEntry.arguments?.getInt("noteId")

                EditNoteScreen(
                    navController = navController,
                    courseId = courseId,
                    noteId = noteId,
                    noteList = noteUiState,
                    editNotes = { noteId, courseId, title, noteContent ->
                        noteViewModel.editNotes(noteId, courseId, title, noteContent)
                        noteViewModel.getNotesForCourse(courseId)
                    },
                    deleteNote = { note -> noteViewModel.deleteNote(note) },
                    startTimer = { noteViewModel.startTimer() },
                    resetTimer = { noteViewModel.resetTimer() },
                    resumeTimer = { noteViewModel.resumeTimer() },
                    pauseTimer = { noteViewModel.pauseTimer() },
                    formattedTime = formattedTime,
                    timerState = timerStateString,
                    setTimerValue = { noteViewModel.setTimerValue(it) }


                )

            }
            composable(route = Screen.StatisticsScreen.route) {
                LaunchedEffect(Unit) {
                    noteViewModel.getAllNotes()
                }
                StatisticsScreen(
                    dailyStats = dailyStats,
                    weeklyStats = weeklyStats,
                    monthlyStats = monthlyStats,
                    formatTime = { timeMillis -> statisticsViewModel.formatTime(timeMillis) },
                    calculateTotalTime = { statsList -> statisticsViewModel.calculateTotalTime(statsList) }
                )
            }


        }

    }


}