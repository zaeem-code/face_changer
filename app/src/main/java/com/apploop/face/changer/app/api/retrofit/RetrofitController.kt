package com.apploop.face.changer.app.api.retrofit

import android.os.Build
import com.apploop.face.changer.app.BuildConfig

import com.apploop.face.changer.app.api.util.ServiceUrl

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitController {

    private val SERVER_ENDPOINT_CORE = com.apploop.face.changer.app.api.util.ServiceUrl.SERVER_ENDPOINT_CORE



    private val client: OkHttpClient by lazy {
        val interceptor = HttpLoggingInterceptor()


        OkHttpClient.Builder()
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .addInterceptor(RetrofitInterceptor("MenJacket/" + BuildConfig.VERSION_NAME + " (Android; Build:" + BuildConfig.VERSION_CODE + "; SDK:" + Build.VERSION.SDK_INT + ";)"))
//                .authenticator(RetrofitAuthenticator())
                .build()
    }

    private val retrofitController_Mesh: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl(SERVER_ENDPOINT_CORE)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
    }




    val apiServiceMesh: com.apploop.face.changer.app.api.retrofit.ApiService by lazy {
        retrofitController_Mesh
            .build()
            .create(com.apploop.face.changer.app.api.retrofit.ApiService::class.java)
    }












}