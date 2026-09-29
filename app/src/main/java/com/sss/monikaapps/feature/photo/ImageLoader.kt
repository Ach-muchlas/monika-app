package com.sss.monikaapps.feature.photo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.request.CachePolicy
import com.sss.monikaapps.common.manager.SessionManager
import com.sss.monikaapps.network.ApiConfig

@Composable
fun rememberImageLoader(): ImageLoader {
    val context = LocalContext.current
    val token = remember { SessionManager.getInstance().getDataUser().token }

    return remember(token) {
        ImageLoader.Builder(context)
            .okHttpClient(ApiConfig.provideOkHttpClient())
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()
    }
}