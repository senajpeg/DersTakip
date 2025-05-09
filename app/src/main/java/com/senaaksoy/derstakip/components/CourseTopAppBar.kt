package com.senaaksoy.derstakip.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.senaaksoy.derstakip.R
import com.senaaksoy.derstakip.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseTopAppBar(
    navController: NavController,
    currentRoute:String,
    courseName: String? = null

){
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
                        contentDescription = null
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
            if(currentRoute==Screen.CourseDetailScreen.route){
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        tint = Color(0xFFF5F5F5),
                        contentDescription = null
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            Color(0xFF8177A7)
        )
    )

}