package com.apploop.face.changer.app.api.retrofit

import okhttp3.Interceptor
import okhttp3.Response

class RetrofitInterceptor(var userAgent: String) : Interceptor {

    /**
     * Interceptor class for setting of the headers for every request
     */
    override fun intercept(chain: Interceptor.Chain): Response {

        var request = chain.request()
        request = request.newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("User-Agent", userAgent)
                .build()
        return chain.proceed(request)
    }
}