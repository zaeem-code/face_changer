package com.apploop.face.changer.app.api.apiRespoInterfaces

import org.json.JSONObject

interface CallBackResponseJson {

    fun onSuccessResponse(result: JSONObject)
    fun onFailResponse(result: String)
}