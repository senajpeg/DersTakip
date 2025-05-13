package com.senaaksoy.derstakip.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.senaaksoy.derstakip.R
import com.senaaksoy.derstakip.components.EditAlertDialog
import com.senaaksoy.derstakip.components.EditButton
import com.senaaksoy.derstakip.navigation.Screen
import com.senaaksoy.derstakip.roomDb.Course

@Composable
fun CourseListScreen(
    modifier: Modifier = Modifier,
    courseList: List<Course>,
    navController: NavController,
    courseName: String,
    setCourseName: (String) -> Unit,
    saveCourse: (String) -> Unit,
    clearItem: () -> Unit,
    upDateCourse: (Course) -> Unit,
) {

    var isDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isEditDialogOpen by rememberSaveable { mutableStateOf(false) }
    var editedCourseName by rememberSaveable { mutableStateOf("") }
    var courseIdToEdit by rememberSaveable { mutableStateOf<Int?>(null) }
    val courseToEdit = courseList.find { it.id == courseIdToEdit }


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EditButton(
            onClick = {
                isDialogOpen = true
                clearItem()
            },
            isIconVisible = true,
            icon = Icons.Filled.Add,
            text = R.string.ders_ekle

        )


        HorizontalDivider(
            color = Color(0xFFCDBBD0),
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth()
        )
        LazyColumn {
            items(courseList) { course ->
                Card(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    onClick = {
                        navController.navigate("${Screen.CourseDetailScreen.route}/${course.id}")
                    },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFD3CBDA))
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = course.name,
                            style = TextStyle(
                                color = Color(0xFF463648),
                                fontSize = 16.sp
                            )
                        )
                        Spacer(modifier = modifier.weight(1f))
                        Icon(
                            modifier = Modifier.clickable {
                                courseIdToEdit = course.id
                                editedCourseName = course.name
                                isEditDialogOpen = true
                            },
                            imageVector = Icons.Filled.Edit,
                            contentDescription = null,
                            tint = Color.DarkGray
                        )
                    }
                }
            }
        }
        if (isEditDialogOpen && courseToEdit != null) {
            EditAlertDialog(
                title = stringResource(R.string.dersi_guncelle),
                value = editedCourseName,
                onValueChange = { editedCourseName = it },
                onConfirm = {
                    val updatedCourse = courseToEdit.copy(name = editedCourseName)
                    upDateCourse(updatedCourse)
                    isEditDialogOpen = false
                },
                onDismiss = { isEditDialogOpen = false },
                confirmButtonText = stringResource(R.string.güncelle),
                dismissButtonText = stringResource(R.string.iptal),
                isConfirmEnabled = editedCourseName.isNotBlank(),
                text = R.string.ders_guncelleme
            )
        }
        if (isDialogOpen) {
            EditAlertDialog(
                title = stringResource(R.string.yeni_ders_ekle),
                value = courseName,
                onValueChange = setCourseName,
                onConfirm = {
                    saveCourse(courseName)
                    isDialogOpen = false
                    clearItem()
                },
                onDismiss = {
                    isDialogOpen = false
                    clearItem()
                },
                confirmButtonText = stringResource(R.string.ders_ekle),
                dismissButtonText = stringResource(R.string.iptal),
                isConfirmEnabled = courseName.isNotBlank(),
                text = R.string.ders_ekleme
            )
        }


    }
}