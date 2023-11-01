package com.apploop.face.changer.app.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.apploop.face.changer.app.R
import com.apploop.face.changer.app.callBacks.AdapterPathInterface
import com.apploop.face.changer.app.callBacks.BackgroundBottomSheetInterface
import com.apploop.face.changer.app.models.ObjSuitImageOption

class BackGroundBottomSheetAdapter(
    val list: List<ObjSuitImageOption>,
    val context: Context,
    val listener: BackgroundBottomSheetInterface,
    private val adapterPathInterface: AdapterPathInterface
) :
    RecyclerView.Adapter<BackGroundBottomSheetAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cus_background_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]
        try {
            val filePath = "file:///android_asset/" + model.suitpath
            Glide.with(context)
                .load(filePath)
                .into(holder.imageView)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        holder.imageView.setOnClickListener {
            listener.onBackgroundBottomSheetButtonClicks(model.suitpath)
            adapterPathInterface.onClick()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: AppCompatImageView = itemView.findViewById(R.id.iv_suits)
    }
}