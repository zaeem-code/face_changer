package com.apploop.face.changer.app.api.apiRespoInterfaces

import com.google.gson.JsonObject
import retrofit2.Response

interface RetrofitCallingListener {

    fun onSuccessResponse(key: String, response: Response<JsonObject>?)
    fun onFailureResponse(key: String, error: String)
}