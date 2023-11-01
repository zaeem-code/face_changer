package com.apploop.face.changer.app.api.retrofit

import com.google.gson.JsonObject
import com.apploop.face.changer.app.api.apiRespoInterfaces.RetrofitCallingListener
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RetrofitCalling(var retrofitCallingListener: com.apploop.face.changer.app.api.apiRespoInterfaces.RetrofitCallingListener, var key: String, var call: Call<JsonObject>) {

    var job: CompletableJob = Job()


    fun apiCalling() {


        CoroutineScope(Dispatchers.IO + job).launch {


            call.enqueue(object : Callback<JsonObject> {

                override fun onResponse(
                        call: Call<JsonObject>?,
                        response: Response<JsonObject>?) {

                    job.complete()
                    val resp = response!!.body()

                    if (resp != null && response.isSuccessful ) {
                        retrofitCallingListener.onSuccessResponse(key, response)

                    }
                    else
                    {
                        retrofitCallingListener.onFailureResponse(key, response.code().toString())
                    }

                }


                override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                    job.complete()
                    retrofitCallingListener.onFailureResponse(key, t.toString())
                }
            })
        }
    }




}