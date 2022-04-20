package com.jay.iconfinderassignment.data.api

import com.jay.iconfinderassignment.utils.AUTH_TOKEN
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    private val baseUrl = "https://api.iconfinder.com/v4/"
    private val client = OkHttpClient.Builder().apply {
        addInterceptor(AuthInterceptor())
        connectTimeout(10, TimeUnit.SECONDS)
        readTimeout(10, TimeUnit.SECONDS)
        writeTimeout(10, TimeUnit.SECONDS)
    }.build()

    private val retrofit by lazy {
        Retrofit.Builder().baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val API : IconApi by lazy {
        retrofit.create(IconApi::class.java)
    }
}

// This is used to add the credentials in each request
class AuthInterceptor : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Accept", "application/json")
            .addHeader("Authorization", AUTH_TOKEN)
            .build()
        return chain.proceed(request)
    }
}