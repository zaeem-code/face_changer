package com.apploop.face.changer.app.views.adapters

import android.content.Context
import android.graphics.Color
import android.provider.CalendarContract.Colors
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.apploop.face.changer.app.R
import com.apploop.face.changer.app.callBacks.AdapterPathInterface
import com.apploop.face.changer.app.callBacks.AddStickerBottomSheetViewModelInterface
import com.apploop.face.changer.app.callBacks.BackgroundBottomSheetInterface
import com.apploop.face.changer.app.callBacks.BackgroundSkinToneInterface
import com.apploop.face.changer.app.models.ObjStickerImageDetail
import com.apploop.face.changer.app.utils.Extension.faceColors

class ShowSkinToneColorsAdapter(
    val list: ArrayList<String>,
    val context: Context,
    val imagePAth : String,
    val listener: BackgroundSkinToneInterface
) : RecyclerView.Adapter<ShowSkinToneColorsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cus_suit_skin_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.progressBar.visibility = View.GONE

        try {

            holder.lv_card_skin_tone.setCardBackgroundColor(Color.parseColor(list[position]))
//            holder.lv_card_skin_tone.setCardBackgroundColor(list[position].toInt())

            val filePath = imagePAth
            Glide.with(context)
                .load(filePath)
                .into(holder.imageView)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        holder.imageView.setOnClickListener {
            listener.onBackgroundSkinToneInterfaceClicks(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: AppCompatImageView = itemView.findViewById(R.id.iv_suits)
        val lv_card_skin_tone: CardView = itemView.findViewById(R.id.lv_card_skin_tone)
        val progressBar: ProgressBar = itemView.findViewById(R.id.progress_bar)

    }
}