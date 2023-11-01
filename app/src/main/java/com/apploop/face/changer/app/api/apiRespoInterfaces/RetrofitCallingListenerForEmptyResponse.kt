package com.apploop.face.changer.app.api.apiRespoInterfaces

interface RetrofitCallingListenerForEmptyResponse {

    fun onSuccessResponse(key: String, response: String)
    fun onFailureResponse(key: String, error: String)
}