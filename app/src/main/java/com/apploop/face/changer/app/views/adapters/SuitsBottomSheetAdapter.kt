package com.apploop.face.changer.app.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.apploop.face.changer.app.R
import com.apploop.face.changer.app.callBacks.AdapterPathInterface
import com.apploop.face.changer.app.callBacks.SuitBottomSheetViewModelInterface
import com.apploop.face.changer.app.models.ObjSuitImageOption

class SuitsBottomSheetAdapter(
    val list: List<ObjSuitImageOption>,
    val context: Context,
    val listener: SuitBottomSheetViewModelInterface,
    private val listenerAdapterPathInterface: AdapterPathInterface
) :
    RecyclerView.Adapter<SuitsBottomSheetAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cus_suit_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.progressBar.visibility = View.GONE
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
            listener.onSuitBottomSheetButtonClicks(model.suitpath)
            listenerAdapterPathInterface.onClick()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: AppCompatImageView = itemView.findViewById(R.id.iv_suits)
        val progressBar: ProgressBar = itemView.findViewById(R.id.progress_bar)
    }
}