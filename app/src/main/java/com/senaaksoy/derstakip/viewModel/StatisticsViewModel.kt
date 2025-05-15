package com.senaaksoy.derstakip.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senaaksoy.derstakip.repository.CourseRepository
import com.senaaksoy.derstakip.repository.NoteRepository
import com.senaaksoy.derstakip.roomDb.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    val allNotes: StateFlow<List<Note>> = _allNotes.asStateFlow()

    private val _courseMap = MutableStateFlow<Map<Int, String>>(emptyMap())
    val courseMap: StateFlow<Map<Int, String>> = _courseMap.asStateFlow()


    private val _dailyStats = MutableStateFlow<Map<String, List<CourseStudyTime>>>(emptyMap())
    val dailyStats: StateFlow<Map<String, List<CourseStudyTime>>> = _dailyStats.asStateFlow()

    private val _weeklyStats = MutableStateFlow<Map<String, List<CourseStudyTime>>>(emptyMap())
    val weeklyStats: StateFlow<Map<String, List<CourseStudyTime>>> = _weeklyStats.asStateFlow()

    private val _monthlyStats = MutableStateFlow<Map<String, List<CourseStudyTime>>>(emptyMap())
    val monthlyStats: StateFlow<Map<String, List<CourseStudyTime>>> = _monthlyStats.asStateFlow()

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
            courseRepository.allCourses().collect { courses ->
                _courseMap.value = courses.associate { it.id to it.name }

                noteRepository.getAllNotes().collect { notes ->
                    _allNotes.value = notes
                    calculateStatistics(notes)
                }
            }
        }
    }

    private fun calculateStatistics(notes: List<Note>) {
        val currentDate = Calendar.getInstance()


        val today = getDateString(currentDate.time)
        _dailyStats.value = calculateDailyStats(notes, today)


        val thisWeekStats = calculateWeeklyStats(notes, currentDate)
        _weeklyStats.value = mapOf("This Week" to thisWeekStats)


        val thisMonthStats = calculateMonthlyStats(notes, currentDate)
        val monthName = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(currentDate.time)
        _monthlyStats.value = mapOf(monthName to thisMonthStats)
    }

    private fun calculateDailyStats(notes: List<Note>, dateString: String): Map<String, List<CourseStudyTime>> {
        val todayNotes = notes.filter { getDateString(Date(it.timestamp)) == dateString }

        val courseStats = todayNotes
            .groupBy { it.courseId }
            .mapValues { (courseId, notesForCourse) ->
                val totalDuration = notesForCourse.sumOf { (it.durationMillis / 1000) * 1000 }
                CourseStudyTime(
                    courseId = courseId,
                    courseName = _courseMap.value[courseId] ?: "Unknown Course",
                    durationMillis = totalDuration
                )
            }
            .values
            .toList()

        return mapOf(dateString to courseStats)
    }

    private fun calculateWeeklyStats(notes: List<Note>, currentCalendar: Calendar): List<CourseStudyTime> {

        val startOfWeek = Calendar.getInstance()
        startOfWeek.time = currentCalendar.time
        startOfWeek.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        startOfWeek.set(Calendar.HOUR_OF_DAY, 0)
        startOfWeek.set(Calendar.MINUTE, 0)
        startOfWeek.set(Calendar.SECOND, 0)
        startOfWeek.set(Calendar.MILLISECOND, 0)

        val endOfWeek = Calendar.getInstance()
        endOfWeek.time = startOfWeek.time
        endOfWeek.add(Calendar.DAY_OF_WEEK, 6)
        endOfWeek.set(Calendar.HOUR_OF_DAY, 23)
        endOfWeek.set(Calendar.MINUTE, 59)
        endOfWeek.set(Calendar.SECOND, 59)
        endOfWeek.set(Calendar.MILLISECOND, 999)

        val weeklyNotes = notes.filter {
            it.timestamp in startOfWeek.timeInMillis..endOfWeek.timeInMillis
        }

        return weeklyNotes
            .groupBy { it.courseId }
            .mapValues { (courseId, notesForCourse) ->
                val totalDuration = notesForCourse.sumOf { (it.durationMillis / 1000) * 1000 }
                CourseStudyTime(
                    courseId = courseId,
                    courseName = _courseMap.value[courseId] ?: "Unknown Course",
                    durationMillis = totalDuration
                )
            }
            .values
            .toList()
    }

    private fun calculateMonthlyStats(notes: List<Note>, currentCalendar: Calendar): List<CourseStudyTime> {
        val startOfMonth = Calendar.getInstance()
        startOfMonth.time = currentCalendar.time
        startOfMonth.set(Calendar.DAY_OF_MONTH, 1)
        startOfMonth.set(Calendar.HOUR_OF_DAY, 0)
        startOfMonth.set(Calendar.MINUTE, 0)
        startOfMonth.set(Calendar.SECOND, 0)
        startOfMonth.set(Calendar.MILLISECOND, 0)

        val endOfMonth = Calendar.getInstance()
        endOfMonth.time = startOfMonth.time
        endOfMonth.set(Calendar.DAY_OF_MONTH, endOfMonth.getActualMaximum(Calendar.DAY_OF_MONTH))
        endOfMonth.set(Calendar.HOUR_OF_DAY, 23)
        endOfMonth.set(Calendar.MINUTE, 59)
        endOfMonth.set(Calendar.SECOND, 59)
        endOfMonth.set(Calendar.MILLISECOND, 999)

        val monthlyNotes = notes.filter {
            it.timestamp in startOfMonth.timeInMillis..endOfMonth.timeInMillis
        }

        return monthlyNotes
            .groupBy { it.courseId }
            .mapValues { (courseId, notesForCourse) ->
                val totalDuration = notesForCourse.sumOf { (it.durationMillis / 1000) * 1000 }
                CourseStudyTime(
                    courseId = courseId,
                    courseName = _courseMap.value[courseId] ?: "Unknown Course",
                    durationMillis = totalDuration
                )
            }
            .values
            .toList()
    }

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