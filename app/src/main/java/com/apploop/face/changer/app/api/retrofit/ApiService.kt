package com.apploop.face.changer.app.api.retrofit

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

interface ApiService {


    @Headers("Accept:application/json")
    @GET("wp-json/jacket-app/v1/images/?type=ipl")
    fun getIplList(): Call<JsonObject>

    @Headers("Accept:application/json")
    @GET("wp-json/jacket-app/v1/images/?type=psl")
    fun getPslList(): Call<JsonObject>

    @Headers("Accept:application/json")
    @GET("wp-json/jacket-app/v1/images/?type=bpl")
    fun getBplList(): Call<JsonObject>

    @Headers("Accept:application/json")
    @GET("wp-json/jacket-app/v1/images/?type=bbl")
    fun getBblList(): Call<JsonObject>

    @Headers("Accept:application/json")
    @GET("wp-json/jacket-app/v1/images/?type=wt")
    fun getWTList(): Call<JsonObject>

    @Headers("Accept:application/json")
    @GET("wp-json/jacket-app/v1/images/?type=fifa")
    fun getFifaList(): Call<JsonObject>

    @Headers("Accept:application/json")
    @GET("wp-json/jacket-app/v1/images/?type=sh")
    fun getSuperList(): Call<JsonObject>

    @Headers("Accept:application/json")
    @GET("wp-json/jacket-app/v1/images/?type=AS")
    fun getAfricaList(): Call<JsonObject>

}