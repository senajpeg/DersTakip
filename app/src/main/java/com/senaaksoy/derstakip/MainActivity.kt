package com.senaaksoy.derstakip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.senaaksoy.derstakip.navigation.AppNavigation
import com.senaaksoy.derstakip.ui.theme.DersTakipTheme
import com.senaaksoy.derstakip.work.NotificationWorker
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            DersTakipTheme {
                AppNavigation()
            }
        }
        scheduleNotificationWork()

    }
    private fun scheduleNotificationWork() {
        val workManager = WorkManager.getInstance(applicationContext)

        val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
            15, TimeUnit.MINUTES
        )
           // .setInitialDelay(15, TimeUnit.MINUTES)
            .setInitialDelay(0,TimeUnit.SECONDS)
            .build()

        workManager.enqueueUniquePeriodicWork(
            "studyReminder",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }

}


@Preview(showBackground = true)
@Composable
fun AppNavigationPreview() {
    DersTakipTheme {
        AppNavigation()
    }
}