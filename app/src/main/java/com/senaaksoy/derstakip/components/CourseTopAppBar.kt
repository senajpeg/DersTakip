package com.senaaksoy.derstakip.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
    currentRoute:String,
    courseName: String? = null,
    courseToDelete: Course? = null,
    onDeleteCourse: ((Course) -> Unit)? = null

){
    var showDialog by remember { mutableStateOf(false) }

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
            if(currentRoute != Screen.CourseListScreen.route){
                IconButton(
                    onClick = { navController.navigateUp() }
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = null,
                        tint = Color(0xFFC0BEC4)
                    )
                }
            }
        },
        actions = {
            if(currentRoute== Screen.CourseListScreen.route){
                IconButton(
                    onClick = {navController.navigate(Screen.StatisticsScreen.route)}
                ) {
                    Icon(
                      imageVector = Icons.Default.BarChart,
                        tint = Color(0xFFF5F5F5),
                        contentDescription = null
                    )
                }
            }
           else if(currentRoute.startsWith("CourseDetailScreen")&& courseToDelete != null && onDeleteCourse != null){
                IconButton(
                    onClick = {
                        showDialog=true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        tint = Color(0xFFF5F5F5),
                        contentDescription = null
                    )
                }
                if(showDialog && courseToDelete !=null){
                    AlertDialog(
                        onDismissRequest = {showDialog=false},
                        confirmButton = {
                            Button(
                                onClick = {onDeleteCourse(courseToDelete)
                                showDialog=false
                                navController.popBackStack()}
                            ) {
                                Text(stringResource(R.string.evet))
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = {showDialog=false}
                            ) { Text(stringResource(R.string.hayır)) }
                        },
                        title = { Text(stringResource(R.string.dersi_sil)) },
                        text = {Text(stringResource(R.string.ders_silme_onayi))}

                    )
                }


            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            Color(0xFF8177A7)
        )
    )

}