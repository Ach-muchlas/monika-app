package com.sss.monikaapps.network

import com.google.gson.GsonBuilder
import com.sss.monikaapps.network.interceptor.AuthInterceptor
import com.sss.monikaapps.network.interceptor.BaseUrlInterceptor
import com.sss.monikaapps.network.interceptor.ConnectionInterceptor
import com.sss.monikaapps.utils.constanta.ApiConstant
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object ApiConfig {
    private var retrofit: Retrofit? = null

    fun getApiService(): ApiService {
        if (retrofit == null) {
            retrofit = createRetrofitInstance()
        }
        return retrofit!!.create(ApiService::class.java)
    }

    fun reloadApiService() {
        retrofit = null
    }

    private fun createRetrofitInstance(): Retrofit {
        val client = OkHttpClient.Builder()
            .connectTimeout(500, TimeUnit.SECONDS)
            .readTimeout(500, TimeUnit.SECONDS)
            .writeTimeout(500, TimeUnit.SECONDS)
            .addInterceptor(ConnectionInterceptor())
            .addInterceptor(AuthInterceptor())
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(BaseUrlInterceptor())
            .build()

        val gson = GsonBuilder()
            .setLenient()
            .create()
        return Retrofit.Builder()
            .baseUrl(ApiConstant.urlDomain())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }
}