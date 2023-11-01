package com.apploop.face.changer.app.views

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.apploop.face.changer.app.R

class IntroduceDialog(
    var pContext: Context,
){
    private var context=pContext
    var close: ImageView?
    var dialog= Dialog(context)

    init
    {
        dialog.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_introduce)

        close= dialog.findViewById(R.id.imv_close)
        initClicks()
    }


    private fun initClicks() {
        close?.setOnClickListener(View.OnClickListener {
            closeDialog()
        })
    }

    fun showDialog()
    {
        if(!dialog.isShowing)
            dialog.show();
    }
    fun closeDialog()
    {
        if(dialog.isShowing)
            dialog.dismiss()
    }
}