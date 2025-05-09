package com.senaaksoy.derstakip.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senaaksoy.derstakip.repository.CourseRepository
import com.senaaksoy.derstakip.roomDb.Course
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseViewModel @Inject constructor(private val courseRepo:CourseRepository): ViewModel(){

    private val _uiState= MutableStateFlow<List<Course>>(emptyList())
    val uiState: StateFlow<List<Course>> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            courseRepo.allCourses().collect{courseList->
                _uiState.value=courseList
            }
        }
    }





}