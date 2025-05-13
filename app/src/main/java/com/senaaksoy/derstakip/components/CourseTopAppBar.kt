package com.senaaksoy.derstakip.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.senaaksoy.derstakip.R
import com.senaaksoy.derstakip.navigation.Screen
import com.senaaksoy.derstakip.roomDb.Course

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseTopAppBar(
    navController: NavController,
    currentRoute: String,
    courseName: String? = null,
    courseToDelete: Course? = null,
    onDeleteCourse: ((Course) -> Unit)? = null

) {
    var showDialog by rememberSaveable { mutableStateOf(false) }

    val titleText = when {
        currentRoute.startsWith("CourseDetailScreen") && courseName != null -> courseName
        currentRoute.startsWith("CourseListScreen") -> stringResource(R.string.derslerim)
        currentRoute.startsWith("AddNoteScreen") -> stringResource(R.string.not_ekleme)
        currentRoute.startsWith("EditNoteScreen") -> stringResource(R.string.not_duzenleme)
        currentRoute.startsWith("StatisticsScreen") -> stringResource(R.string.istatistik)
        else -> stringResource(R.string.app_name)
    }
    TopAppBar(
        title = {
            Text(
                text = titleText,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            )

        },
        navigationIcon = {
            if (currentRoute != Screen.CourseListScreen.route) {
                EditIconButton(
                    onClick = { navController.navigateUp() },
                    isEnabled = true,
                    icon = Icons.AutoMirrored.Filled.ArrowBack
                )
            }
        },
        actions = {
            if (currentRoute == Screen.CourseListScreen.route) {
                EditIconButton(
                    onClick = { navController.navigate(Screen.StatisticsScreen.route) },
                    icon = Icons.Default.BarChart,
                    isEnabled = true
                )
            } else if (currentRoute.startsWith("CourseDetailScreen") && courseToDelete != null && onDeleteCourse != null) {
                EditIconButton(
                    onClick = { showDialog = true },
                    isEnabled = true,
                    icon = Icons.Default.Delete
                )

                if (showDialog) {
                    EditAlertDialog(
                        title = stringResource(R.string.dersi_sil),
                        value = "",
                        text = R.string.ders_silme_onayi,
                        onValueChange = {},
                        onConfirm = {
                            onDeleteCourse(courseToDelete)
                            showDialog = false
                            navController.popBackStack()
                        },
                        onDismiss = { showDialog = false },
                        confirmButtonText = stringResource(R.string.evet),
                        dismissButtonText = stringResource(R.string.hayır),
                        isConfirmEnabled = true,
                        isTextFieldVisible = false
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            Color(0xFF8177A7)
        )
    )

}