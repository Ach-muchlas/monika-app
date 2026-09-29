package com.sss.monikaapps

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.sss.monikaapps.common.component.SnackbarHostView
import com.sss.monikaapps.common.navigation.AppNavGraph
import com.sss.monikaapps.common.theme.MonikaAppsTheme

class MainScreen : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {

        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        splashScreen.setOnExitAnimationListener { provider ->
            val view = provider.view

            view.animate()
                .alpha(0f)
                .setDuration(300)
                .withEndAction {
                    provider.remove()
                }
                .start()
        }

        enableEdgeToEdge()

        setContent {
            val density = LocalDensity.current

            CompositionLocalProvider(
                LocalDensity provides Density(
                    density = density.density,
                    fontScale = 1f
                )
            ) {
                MonikaAppsTheme {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .systemBarsPadding()
                    ) {
                        AppNavGraph()
                        SnackbarHostView()
                    }
                }
            }
        }

    }
}
