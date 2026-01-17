package com.sss.monikaapps.network.interceptor
import androidx.core.net.toUri
import com.sss.monikaapps.common.manager.ServerManager
import okhttp3.Interceptor
import okhttp3.Response

class BaseUrlInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newUrlString =
            ServerManager.getInstance().getServerAddress().trimEnd('/') + "/monika_api/"
        val newUri = newUrlString.toUri()
        val newHttpUrl = originalRequest.url.newBuilder()
            .scheme(newUri.scheme ?: "http")
            .host(newUri.host ?: originalRequest.url.host)
            .port(newUri.port.takeIf { it != -1 } ?: originalRequest.url.port)
            .build()

        val newRequest = originalRequest.newBuilder()
            .url(newHttpUrl)
            .build()

        return chain.proceed(newRequest)
    }
}
