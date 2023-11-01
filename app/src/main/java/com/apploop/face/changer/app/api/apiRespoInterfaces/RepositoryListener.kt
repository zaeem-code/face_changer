package com.apploop.face.changer.app.api.apiRespoInterfaces

interface RepositoryListener {
    fun onSuccessResponse(key: String, result: String)
    fun onFailureResponse(key: String, error: String)
}