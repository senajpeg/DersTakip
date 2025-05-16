package com.senaaksoy.derstakip.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.senaaksoy.derstakip.R
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
    val tabs = listOf("Daily", "Weekly", "Monthly")

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
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
    if (dailyStats.isEmpty()) {
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
                title = "Günlük İstatistik - $date",
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
    if (weeklyStats.isEmpty()) {
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
    if (monthlyStats.isEmpty()) {
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

@Composable
fun StatisticsCard(
    title: String,
    stats: List<StatisticsViewModel.CourseStudyTime>,
    formatTime: (Long) -> String,
    calculateTotalTime: (List<StatisticsViewModel.CourseStudyTime>) -> Long
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD3CBDA))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (stats.isEmpty()) {
                Text(
                    text = stringResource(R.string.bu_doneme_ait_calisma_verisi_yok),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(stats) { stat ->
                        CourseTimeRow(
                            courseName = stat.courseName,
                            studyTime = formatTime(stat.durationMillis)
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        TotalTimeRow(
                            totalTime = formatTime(calculateTotalTime(stats))
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CourseTimeRow(
    courseName: String,
    studyTime: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = courseName,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = studyTime,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun TotalTimeRow(
    totalTime: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.toplam_calisma_suresi),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = totalTime,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun EmptyStatsMessage() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.mevcut_istatistik_yok),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}