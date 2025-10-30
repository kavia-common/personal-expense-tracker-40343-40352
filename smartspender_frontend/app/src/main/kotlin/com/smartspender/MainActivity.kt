package com.smartspender

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.smartspender.navigation.SmartSpenderApp
import com.smartspender.ui.theme.SmartSpenderTheme

// PUBLIC_INTERFACE
class MainActivity : ComponentActivity() {
    /** Entry point activity hosting the Compose content and app navigation. */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityContext = this
        setContent {
            SmartSpenderTheme {
                SmartSpenderApp(activityContext)
            }
        }
    }
}
