package com.apploop.face.changer.app.views.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.apploop.face.changer.app.R
import com.apploop.face.changer.app.callBacks.AdapterPathInterface
import com.apploop.face.changer.app.callBacks.SuitBottomSheetViewModelInterface
import com.apploop.face.changer.app.callBacks.SuitBottomSheetViewModelInterfaceApi
import com.apploop.face.changer.app.models.SuitList

class SuitsBottomSheetAdapter1(
    val list: List<SuitList>,
    val context: Context,
    val listener: SuitBottomSheetViewModelInterface,
    val listenerApi: SuitBottomSheetViewModelInterfaceApi,
    private val listenerAdapterPathInterface: AdapterPathInterface
) :
    RecyclerView.Adapter<SuitsBottomSheetAdapter1.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cus_suit_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]
        holder.progressBar.visibility = View.GONE

        try {
            val filePath = model.image_url
            Glide.with(context)
                .load(filePath)
                .placeholder(R.drawable.ic_suit_drawer)
                .listener(object : RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        holder.progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        holder.progressBar.visibility = View.GONE
                        return false
                    }

                })
                .into(holder.imageView)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        holder.imageView.setOnClickListener {
            model.image_url?.let { it1 -> listenerApi.onSuitBottomSheetButtonClicksApi(it1) }
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