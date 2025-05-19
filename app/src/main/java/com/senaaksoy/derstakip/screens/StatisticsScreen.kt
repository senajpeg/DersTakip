package com.senaaksoy.derstakip.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.senaaksoy.derstakip.R
import com.senaaksoy.derstakip.components.EmptyStatsMessage
import com.senaaksoy.derstakip.components.StatisticsCard
import com.senaaksoy.derstakip.viewModel.StatisticsViewModel

@Composable
fun StatisticsScreen(
    dailyStats: Map<String, List<StatisticsViewModel.CourseStudyTime>>,
    weeklyStats: Map<String, List<StatisticsViewModel.CourseStudyTime>>,
    monthlyStats: Map<String, List<StatisticsViewModel.CourseStudyTime>>,
    formatTime: (Long) -> String,
    calculateTotalTime: (List<StatisticsViewModel.CourseStudyTime>) -> Long
) {
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
    val tabTitles = listOf(
        stringResource(R.string.daily_tab),
        stringResource(R.string.weekly_tab),
        stringResource(R.string.monthly_tab)
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(text = title, color = Color(0xFF8177A7)) }
                )
            }
        }

        when (selectedTabIndex) {
            0 -> DailyStatistics(dailyStats, formatTime, calculateTotalTime)
            1 -> WeeklyStatistics(weeklyStats, formatTime, calculateTotalTime)
            2 -> MonthlyStatistics(monthlyStats, formatTime, calculateTotalTime)
        }
    }
}

@Composable
fun DailyStatistics(
    dailyStats: Map<String, List<StatisticsViewModel.CourseStudyTime>>,
    formatTime: (Long) -> String,
    calculateTotalTime: (List<StatisticsViewModel.CourseStudyTime>) -> Long
) {
    if (dailyStats.isEmpty() || dailyStats.all { it.value.isEmpty() }) {
        EmptyStatsMessage()
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        dailyStats.forEach { (date, courseStats) ->
            StatisticsCard(
                title =stringResource(R.string.daily_statistics_title, date),
                stats = courseStats,
                formatTime = formatTime,
                calculateTotalTime = calculateTotalTime
            )
        }
    }
}

@Composable
fun WeeklyStatistics(
    weeklyStats: Map<String, List<StatisticsViewModel.CourseStudyTime>>,
    formatTime: (Long) -> String,
    calculateTotalTime: (List<StatisticsViewModel.CourseStudyTime>) -> Long
) {
    if (weeklyStats.isEmpty() || weeklyStats.all { it.value.isEmpty() }) {
        EmptyStatsMessage()
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        weeklyStats.forEach { (weekLabel, courseStats) ->
            StatisticsCard(
                title = weekLabel,
                stats = courseStats,
                formatTime = formatTime,
                calculateTotalTime = calculateTotalTime
            )
        }
    }
}

@Composable
fun MonthlyStatistics(
    monthlyStats: Map<String, List<StatisticsViewModel.CourseStudyTime>>,
    formatTime: (Long) -> String,
    calculateTotalTime: (List<StatisticsViewModel.CourseStudyTime>) -> Long
) {
    if (monthlyStats.isEmpty() || monthlyStats.all { it.value.isEmpty() }) {
        EmptyStatsMessage()
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        monthlyStats.forEach { (monthLabel, courseStats) ->
            StatisticsCard(
                title = monthLabel,
                stats = courseStats,
                formatTime = formatTime,
                calculateTotalTime = calculateTotalTime
            )
        }
    }
}
