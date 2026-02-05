package com.sss.monikaapps.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

class TimeoutInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val isGet = request.method == "GET"

        return chain.withConnectTimeout(15, TimeUnit.SECONDS).withReadTimeout(
            if (isGet) 60 else 4 * 60, TimeUnit.SECONDS
        ).withWriteTimeout(
            if (isGet) 30 else 4 * 60, TimeUnit.SECONDS
        ).proceed(request)
    }
}
