package com.jointdelivery.auth


import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import java.util.concurrent.TimeUnit

object ApiClient {

    private val httpClient = OkHttpClient.Builder()
    private var retrofit: Retrofit? = null
    private val builder = Retrofit.Builder()
        .baseUrl("https://jointdeliverydev.azurewebsites.net/")
        .addConverterFactory(GsonConverterFactory.create(Gson()))

    fun <S> createService(serviceClass: Class<S>): S {
        return createService(serviceClass, null)
    }

    fun <S> createService(serviceClass: Class<S>, authToken: String?): S {
        try {
            if (authToken != null) {

                httpClient.addInterceptor(Interceptor { chain ->
                    val original = chain.request()
                    // Request customization: add request headers
                    val requestBuilder = original.newBuilder()
                        .header("Content-Type", "application/json")
                        .header("Authorization", "bearer $authToken")
                        .method(original.method(), original.body())

                    if (requestBuilder != null) {
                        val request = requestBuilder.build()
                        return@Interceptor chain.proceed(request)
                    }
                    null
                })

                httpClient.addNetworkInterceptor { chain ->
                    val request = chain.request().newBuilder().addHeader("Connection", "close").build()
                    chain.proceed(request)
                }
            }
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = httpClient.addInterceptor(interceptor).connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS).build()
            retrofit = builder.client(client).build()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return retrofit!!.create(serviceClass)
    }
}
