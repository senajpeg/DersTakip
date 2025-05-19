package com.senaaksoy.derstakip.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.senaaksoy.derstakip.R
import com.senaaksoy.derstakip.viewModel.StatisticsViewModel

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