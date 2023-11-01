package com.apploop.face.changer.app.api.repostries

import com.google.gson.JsonObject
import com.apploop.face.changer.app.api.retrofit.RetrofitCalling
import com.apploop.face.changer.app.api.retrofit.RetrofitController
import com.apploop.face.changer.app.api.apiRespoInterfaces.RepositoryListener
import com.apploop.face.changer.app.api.apiRespoInterfaces.RetrofitCallingListener


import retrofit2.Response

class RepoVideos(private val repositoryListener: com.apploop.face.changer.app.api.apiRespoInterfaces.RepositoryListener) :
    com.apploop.face.changer.app.api.apiRespoInterfaces.RetrofitCallingListener {

    lateinit var key: String


    fun getIplVideos() {
        key = "ipl"
        val apiService = RetrofitController.apiServiceMesh.getIplList()
        val retrofitCalling =
            com.apploop.face.changer.app.api.retrofit.RetrofitCalling(this, key, apiService)
        retrofitCalling.apiCalling()
    }

    fun getAfricaVideos() {
        key = "AS"
        val apiService = RetrofitController.apiServiceMesh.getAfricaList()
        val retrofitCalling =
            com.apploop.face.changer.app.api.retrofit.RetrofitCalling(this, key, apiService)
        retrofitCalling.apiCalling()
    }

    fun getPslVideos() {
        key = "psl"
        val apiService = RetrofitController.apiServiceMesh.getPslList()
        val retrofitCalling =
            com.apploop.face.changer.app.api.retrofit.RetrofitCalling(this, key, apiService)
        retrofitCalling.apiCalling()
    }

    fun getBplVideos() {
        key = "bpl"
        val apiService = RetrofitController.apiServiceMesh.getBplList()
        val retrofitCalling =
            com.apploop.face.changer.app.api.retrofit.RetrofitCalling(this, key, apiService)
        retrofitCalling.apiCalling()
    }

    fun getBblVideos() {
        key = "bbl"
        val apiService = RetrofitController.apiServiceMesh.getBblList()
        val retrofitCalling =
            com.apploop.face.changer.app.api.retrofit.RetrofitCalling(this, key, apiService)
        retrofitCalling.apiCalling()
    }

    fun getWtVideos() {
        key = "wt"
        val apiService = RetrofitController.apiServiceMesh.getWTList()
        val retrofitCalling =
            com.apploop.face.changer.app.api.retrofit.RetrofitCalling(this, key, apiService)
        retrofitCalling.apiCalling()
    }

    fun getFifaVideos() {
        key = "fifa"
        val apiService = RetrofitController.apiServiceMesh.getFifaList()
        val retrofitCalling =
            com.apploop.face.changer.app.api.retrofit.RetrofitCalling(this, key, apiService)
        retrofitCalling.apiCalling()
    }

    fun getSuperVideos() {
        key = "super"
        val apiService = RetrofitController.apiServiceMesh.getSuperList()
        val retrofitCalling =
            com.apploop.face.changer.app.api.retrofit.RetrofitCalling(this, key, apiService)
        retrofitCalling.apiCalling()
    }


    override fun onSuccessResponse(key: String, response: Response<JsonObject>?) {
        if (response!!.isSuccessful)
            repositoryListener.onSuccessResponse(key, response.body()!!.toString())
        else
            repositoryListener.onFailureResponse(key, "Error")
    }

    override fun onFailureResponse(key: String, error: String) {
        repositoryListener.onFailureResponse(key, error)
    }
}