package com.sss.monikaapps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.ui.Modifier
import com.sss.monikaapps.common.component.SnackbarHostView
import com.sss.monikaapps.common.navigation.AppNavGraph
import com.sss.monikaapps.common.theme.MonikaAppsTheme

class MainScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MonikaAppsTheme {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding()) {
                    AppNavGraph()

                    SnackbarHostView()
                }
            }
        }
    }
}