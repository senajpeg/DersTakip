package com.senaaksoy.derstakip.viewModel

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senaaksoy.derstakip.repository.CourseRepository
import com.senaaksoy.derstakip.repository.NoteRepository
import com.senaaksoy.derstakip.roomDb.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val courseRepository: CourseRepository
) : ViewModel() {

    private val _allNotes = MutableStateFlow<List<Note>>(emptyList())
    private val _courseMap =
        MutableStateFlow<Map<Int, String>>(emptyMap())//ders id si ile ders adını eşleştiren harita

    private val _dailyStats = MutableStateFlow<Map<String, List<CourseStudyTime>>>(emptyMap())
    val dailyStats: StateFlow<Map<String, List<CourseStudyTime>>> = _dailyStats.asStateFlow()

    private val _weeklyStats = MutableStateFlow<Map<String, List<CourseStudyTime>>>(emptyMap())
    val weeklyStats: StateFlow<Map<String, List<CourseStudyTime>>> = _weeklyStats.asStateFlow()

    private val _monthlyStats = MutableStateFlow<Map<String, List<CourseStudyTime>>>(emptyMap())
    val monthlyStats: StateFlow<Map<String, List<CourseStudyTime>>> = _monthlyStats.asStateFlow()

    //bir ders için toplam süreyi ve dersin adını tutmak için açtık bunu
    data class CourseStudyTime(
        val courseId: Int,
        val courseName: String,
        val durationMillis: Long
    )

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            val courseFlow = courseRepository.allCourses()
            val notesFlow = noteRepository.getAllNotes()

            combine(courseFlow, notesFlow) { courses, notes ->
                val courseMap = courses.associate { it.id to it.name }
                _courseMap.value = courseMap
                _allNotes.value = notes
                calculateStatistics(notes, courseMap)
            }.collect {}
        }
    }

    private fun calculateStatistics(notes: List<Note>, courseMap: Map<Int, String>) {
        val currentDate = Calendar.getInstance()


        val today = getDateString(currentDate.time)
        _dailyStats.value = calculateDailyStats(notes, today, courseMap)


        val thisWeekStats = calculateWeeklyStats(notes, currentDate, courseMap)
        _weeklyStats.value = mapOf("Bu Hafta" to thisWeekStats)

        val thisMonthStats = calculateMonthlyStats(notes, currentDate, courseMap)
        val monthName = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(currentDate.time)
        _monthlyStats.value = mapOf(monthName to thisMonthStats)
    }

    private fun calculateDailyStats(
        notes: List<Note>,
        dateString: String,
        courseMap: Map<Int, String>
    ): Map<String, List<CourseStudyTime>> {
        val todayNotes = notes.filter { getDateString(Date(it.timestamp)) == dateString }

        val courseStats = todayNotes
            .groupBy { it.courseId }
            .mapValues { (courseId, notesForCourse) ->
                val totalDuration = notesForCourse.sumOf { (it.durationMillis / 1000) * 1000 }
                CourseStudyTime(
                    courseId = courseId,
                    courseName = courseMap[courseId] ?: "Bilinmeyen Ders",
                    durationMillis = totalDuration
                )
            }
            .values
            .toList()

        return mapOf(dateString to courseStats)
    }

    private fun calculateWeeklyStats(
        notes: List<Note>,
        currentCalendar: Calendar,
        courseMap: Map<Int, String>
    ): List<CourseStudyTime> {
        val cal = Calendar.getInstance()
        cal.time = currentCalendar.time


        cal.firstDayOfWeek = Calendar.MONDAY
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)

        val startOfWeek = cal.timeInMillis


        cal.add(Calendar.DAY_OF_MONTH, 6)
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        cal.set(Calendar.MILLISECOND, 999)

        val endOfWeek = cal.timeInMillis

        val weeklyNotes = notes.filter { note ->
            note.timestamp in startOfWeek..endOfWeek
        }

        return if (weeklyNotes.isEmpty()) {
            emptyList()
        } else {
            weeklyNotes
                .groupBy { it.courseId }
                .mapValues { (courseId, notesForCourse) ->
                    val totalDuration = notesForCourse.sumOf { it.durationMillis }
                    CourseStudyTime(
                        courseId = courseId,
                        courseName = courseMap[courseId] ?: "Bilinmeyen Ders",
                        durationMillis = totalDuration
                    )
                }
                .values
                .toList()
        }
    }

    private fun calculateMonthlyStats(
        notes: List<Note>,
        currentCalendar: Calendar,
        courseMap: Map<Int, String>
    ): List<CourseStudyTime> {
        val cal = Calendar.getInstance()
        cal.time = currentCalendar.time


        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)

        val startOfMonth = cal.timeInMillis


        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        cal.set(Calendar.MILLISECOND, 999)

        val endOfMonth = cal.timeInMillis

        val monthlyNotes = notes.filter { note ->
            note.timestamp in startOfMonth..endOfMonth
        }

        return if (monthlyNotes.isEmpty()) {
            emptyList()
        } else {
            monthlyNotes
                .groupBy { it.courseId }
                .mapValues { (courseId, notesForCourse) ->
                    val totalDuration = notesForCourse.sumOf { it.durationMillis }
                    CourseStudyTime(
                        courseId = courseId,
                        courseName = courseMap[courseId] ?: "Bilinmeyen Ders",
                        durationMillis = totalDuration
                    )
                }
                .values
                .toList()
        }
    }

    @SuppressLint("DefaultLocale")
    fun formatTime(timeMillis: Long): String {
        val totalSeconds = timeMillis / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        return if (hours > 0) {
            String.format("%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format("%02d:%02d", minutes, seconds)
        }
    }

    private fun getDateString(date: Date): String {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return dateFormat.format(date)
    }

    fun calculateTotalTime(statsList: List<CourseStudyTime>): Long {
        return statsList.sumOf { it.durationMillis }
    }
}