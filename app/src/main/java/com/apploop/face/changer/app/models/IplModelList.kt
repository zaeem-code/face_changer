package com.apploop.face.changer.app.models


import com.google.gson.annotations.SerializedName

data class IplModelList(
    @SerializedName("ipl")
    val ipl: List<Ipl>
) {
    data class Ipl(
        @SerializedName("image_url")
        val imageUrl: String
    )
}
