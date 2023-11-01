package com.apploop.face.changer.app.api.viewModel

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.apploop.face.changer.app.api.apiRespoInterfaces.CallBackResponseJson
import com.apploop.face.changer.app.api.apiRespoInterfaces.RepositoryListener
import com.apploop.face.changer.app.api.repostries.RepoVideos
import com.apploop.face.changer.app.models.SuitList
import com.apploop.face.changer.app.utils.Extension.africaList
import com.apploop.face.changer.app.utils.Extension.bblList
import com.apploop.face.changer.app.utils.Extension.bplList
import com.apploop.face.changer.app.utils.Extension.fifaList
import com.apploop.face.changer.app.utils.Extension.iplList
import com.apploop.face.changer.app.utils.Extension.pslList
import com.apploop.face.changer.app.utils.Extension.superList
import com.apploop.face.changer.app.utils.Extension.wtList
import org.json.JSONObject

class ViewModelVideos(context: Context?, private val callBackResp: com.apploop.face.changer.app.api.apiRespoInterfaces.CallBackResponseJson?) :
    com.apploop.face.changer.app.api.apiRespoInterfaces.RepositoryListener {

    private val TAG = "ViewModelVideos"
    private val repoLocation = com.apploop.face.changer.app.api.repostries.RepoVideos(this)
    private val context: Context? = context?.applicationContext

    fun getIplList() {
        repoLocation.getIplVideos()
    }


    fun getPslVideos() {
        repoLocation.getPslVideos()
    }

    fun getAfricaVideos() {
        repoLocation.getAfricaVideos()
    }

    fun getBplVideos() {
        repoLocation.getBplVideos()
    }

    fun getBblVideos() {
        repoLocation.getBblVideos()
    }

    fun getWtVideos() {
        repoLocation.getWtVideos()
    }

    fun getFifaVideos() {
        repoLocation.getFifaVideos()
    }

    fun getSuperVideos() {
        repoLocation.getSuperVideos()
    }


    private fun apiResponse(key: String, jsonObject: JSONObject) {
        Log.d(TAG, "-----------$jsonObject")
        when (key) {
            "ipl" -> {
                val listType = object : TypeToken<ArrayList<SuitList?>?>() {}.type
                val tempList: ArrayList<SuitList> = Gson().fromJson<ArrayList<SuitList>>(
                    jsonObject.optJSONArray("ipl")!!.toString(), listType
                )
                Log.d(TAG, "------list-----${Gson().toJson(tempList)}")

                iplList.clear()
                iplList.addAll(tempList)
            }

            "AS" -> {
                val listType = object : TypeToken<ArrayList<SuitList?>?>() {}.type
                val tempList: ArrayList<SuitList> = Gson().fromJson<ArrayList<SuitList>>(
                    jsonObject.optJSONArray("AS")!!.toString(), listType
                )
                Log.d(TAG, "--AS----list-----${Gson().toJson(tempList)}")

                africaList.clear()
                africaList.addAll(tempList)
            }
            "psl" -> {
                val listType = object : TypeToken<ArrayList<SuitList?>?>() {}.type
                val tempList: ArrayList<SuitList> = Gson().fromJson<ArrayList<SuitList>>(
                    jsonObject.optJSONArray("psl")!!.toString(), listType
                )
                Log.d(TAG, "------list-----${Gson().toJson(tempList)}")

                pslList.clear()
                pslList.addAll(tempList)
            }
            "bpl" -> {
                val listType = object : TypeToken<ArrayList<SuitList?>?>() {}.type
                val tempList: ArrayList<SuitList> = Gson().fromJson<ArrayList<SuitList>>(
                    jsonObject.optJSONArray("bpl")!!.toString(), listType
                )
                Log.d(TAG, "------list-----${Gson().toJson(tempList)}")
                bplList.clear()
                bplList.addAll(tempList)
            }
            "bbl" -> {
                val listType = object : TypeToken<ArrayList<SuitList?>?>() {}.type
                val tempList: ArrayList<SuitList> = Gson().fromJson<ArrayList<SuitList>>(
                    jsonObject.optJSONArray("bbl")!!.toString(), listType
                )
                Log.d(TAG, "------list-----${Gson().toJson(tempList)}")

                bblList.clear()
                bblList.addAll(tempList)
            }
            "wt" -> {
                val listType = object : TypeToken<ArrayList<SuitList?>?>() {}.type
                val tempList: ArrayList<SuitList> = Gson().fromJson<ArrayList<SuitList>>(
                    jsonObject.optJSONArray("wt")!!.toString(), listType
                )
                Log.d(TAG, "------list-----${Gson().toJson(tempList)}")

                wtList.clear()
                wtList.addAll(tempList)
            }
            "fifa" -> {
                val listType = object : TypeToken<ArrayList<SuitList?>?>() {}.type
                val tempList: ArrayList<SuitList> = Gson().fromJson<ArrayList<SuitList>>(
                    jsonObject.optJSONArray("fifa")!!.toString(), listType
                )
                Log.d(TAG, "------list-----${Gson().toJson(tempList)}")

                fifaList.clear()
                fifaList.addAll(tempList)
            }
            "super" -> {
                val listType = object : TypeToken<ArrayList<SuitList?>?>() {}.type
                val tempList: ArrayList<SuitList> = Gson().fromJson<ArrayList<SuitList>>(
                    jsonObject.optJSONArray("sh")!!.toString(), listType
                )
                Log.d(TAG, "------list-----${Gson().toJson(tempList)}")

                superList.clear()
                superList.addAll(tempList)

            }
        }
    }

    override fun onSuccessResponse(key: String, result: String) {
        when (key) {

            "ipl" -> {
                apiResponse(key, JSONObject(result))
                Log.d(TAG, "-----------$result")
                callBackResp?.onSuccessResponse(JSONObject(result))
            }
            "psl" -> {
                apiResponse(key, JSONObject(result))
                Log.d(TAG, "-----------$result")
                callBackResp?.onSuccessResponse(JSONObject(result))
            }

            "AS" -> {
                apiResponse(key, JSONObject(result))
                Log.d(TAG, "-----------$result")
                callBackResp?.onSuccessResponse(JSONObject(result))
            }
            "bpl" -> {
                apiResponse(key, JSONObject(result))
                Log.d(TAG, "-----------$result")
                callBackResp?.onSuccessResponse(JSONObject(result))
            }
            "bbl" -> {
                apiResponse(key, JSONObject(result))
                Log.d(TAG, "-----------$result")
                callBackResp?.onSuccessResponse(JSONObject(result))
            }
            "wt" -> {
                apiResponse(key, JSONObject(result))
                Log.d(TAG, "-----------$result")
                callBackResp?.onSuccessResponse(JSONObject(result))
            }
            "fifa" -> {
                apiResponse(key, JSONObject(result))
                Log.d(TAG, "-----------$result")
                callBackResp?.onSuccessResponse(JSONObject(result))
            }
            "super" -> {
                apiResponse(key, JSONObject(result))
                Log.d(TAG, "-----------$result")
                callBackResp?.onSuccessResponse(JSONObject(result))

            }
        }
    }

    override fun onFailureResponse(key: String, error: String) {

        callBackResp?.onFailResponse(error)
        Log.d(TAG, "-----------$error")

    }


}
