package com.apploop.face.changer.app.views.myCreation

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.apploop.face.changer.app.R
import com.apploop.face.changer.app.databinding.ActivityViewSavedImageBinding
import com.apploop.face.changer.app.manager.AdsManager
import com.apploop.face.changer.app.utils.Extension
import com.apploop.face.changer.app.utils.Extension.alertDialog
import com.apploop.face.changer.app.utils.Extension.share
import com.apploop.face.changer.app.utils.Extension.statusBarColor
import com.apploop.face.changer.app.utils.UtilsCons
import com.apploop.face.changer.app.views.handCrop.HandCropActivity

class ViewSavedImageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewSavedImageBinding
    private var path: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_saved_image)
        statusBarColor(R.color.background)
        init()
    }

    private fun init() {

        binding.shimmerFrameLayout.startShimmer()
        AdsManager.Companion.instance!!.showNativeAd(
            binding.frameLayout,
            binding.frameLayout,
            layoutInflater,
            R.layout.ad_media
        )
        {
            if (it) {
                binding.shimmerFrameLayout.visibility = View.INVISIBLE
            } else {
                binding.shimmerFrameLayout.visibility = View.GONE
                binding.frameLayout.visibility = View.GONE
            }
        }

        path = intent.extras!!.getString("fullImagePath")!!
        Thread {
            binding.ivSavedImage.post(Runnable {
                Glide.with(this)
                    .load(path)
                    .into(binding.ivSavedImage)
            })
        }.start()



        binding.shareIMG.setOnClickListener {
            share(path)
        }

        binding.deleteIMG.setOnClickListener {
            alertDialog(path)
        }

        binding.editIMG.setOnClickListener {
            EditCreation(path)
        }

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }


    }

    fun EditCreation(path: String) {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView: View =
            inflater.inflate(com.apploop.face.changer.app.R.layout.dialog_edit_img, null)
        dialogBuilder.setView(dialogView)

        val alertDialog = dialogBuilder.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()

        val faceChange =
            dialogView.findViewById<View>(R.id.faceChangeLL) as LinearLayout
        val menstyle =
            dialogView.findViewById<View>(R.id.menStyleLL) as LinearLayout
        val removebg =
            dialogView.findViewById<View>(R.id.removeBgLL) as LinearLayout
        val cross =
            dialogView.findViewById<View>(R.id.crossRL) as RelativeLayout

        cross.setOnClickListener {
            alertDialog.dismiss()
        }

        faceChange.setOnClickListener {
            UtilsCons.chooseLayout = "PHOTO_REMOVE_BG"
            val intent = Intent(this, com.apploop.face.changer.app.views.handCrop.HandCropActivity::class.java)
            startActivity(intent)
            UtilsCons.originalBitmap = null
            UtilsCons.originalPath = ""
            UtilsCons.fromGallery = ""
            UtilsCons.originalBitmap = BitmapFactory.decodeFile(path)
            UtilsCons.originalPath = Extension.imageFilePath
            alertDialog.dismiss()
        }

        menstyle.setOnClickListener {
            UtilsCons.chooseLayout = "PHOTO_MEN"
            val intent = Intent(this, com.apploop.face.changer.app.views.handCrop.HandCropActivity::class.java)
            startActivity(intent)
            UtilsCons.originalBitmap = null
            UtilsCons.originalPath = ""
            UtilsCons.fromGallery = ""
            UtilsCons.originalBitmap = BitmapFactory.decodeFile(path)
            UtilsCons.originalPath = Extension.imageFilePath
            alertDialog.dismiss()
        }

        removebg.setOnClickListener {
            UtilsCons.chooseLayout = "PHOTO_REMOVE_BG"
            val intent = Intent(this, com.apploop.face.changer.app.views.handCrop.HandCropActivity::class.java)
            val myUri = Uri.parse(path)
            intent.putExtra("image_uri", myUri.toString())
//            intent.putExtra("path", Extension.imageFilePath)
            intent.putExtra("path", myUri.toString())
            startActivity(intent)


            UtilsCons.originalBitmap = null
            UtilsCons.originalPath = ""
            UtilsCons.fromGallery = ""
            UtilsCons.originalBitmap = BitmapFactory.decodeFile(path)
            UtilsCons.originalPath = Extension.imageFilePath

            alertDialog.dismiss()
        }
    }

}