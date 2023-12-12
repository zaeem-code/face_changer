package com.apploop.face.changer.app.views.myCreation

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.apploop.face.changer.app.R
import com.apploop.face.changer.app.databinding.ActivityViewSavedImageBinding
import com.apploop.face.changer.app.utils.Extension
import com.apploop.face.changer.app.utils.Extension.alertDialog
import com.apploop.face.changer.app.utils.Extension.share
import com.apploop.face.changer.app.utils.Extension.statusBarColor
import com.apploop.face.changer.app.utils.UtilsCons
import com.apploop.face.changer.app.views.faceChangeScreen.FaceChangeActivity
import com.apploop.face.changer.app.views.MenPhotoScreen.MenPhotoActivity
import com.apploop.face.changer.app.views.removeBackground.ImageRemoveBgActivity
import com.apploop.face.changer.app.views.removeBackground.StoreManager
import com.theartofdev.edmodo.cropper.CropImage
import java.io.IOException

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

//        binding.shimmerFrameLayout.startShimmer()
//        AdsManager.Companion.instance!!.showNativeAd(
//            binding!!.frameLayout,
//            binding!!.frameLayout,
//            layoutInflater,
//            R.layout.ad_media
//        )
//        {
//            if (it) {
//                binding.shimmerFrameLayout.visibility = View.INVISIBLE
//            } else {
//                binding.shimmerFrameLayout.visibility = View.GONE
//                binding.frameLayout.visibility = View.GONE
//            }
//        }

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

        val faceChange = dialogView.findViewById<View>(R.id.faceChangeLL) as LinearLayout
        val menstyle = dialogView.findViewById<View>(R.id.menStyleLL) as LinearLayout
        val removebg = dialogView.findViewById<View>(R.id.removeBgLL) as LinearLayout
        val cross = dialogView.findViewById<View>(R.id.crossRL) as RelativeLayout

        cross.setOnClickListener {
            alertDialog.dismiss()
        }

        faceChange.setOnClickListener {
            UtilsCons.chooseLayout = "PHOTO_FACE"
//            val myUri = Uri.parse(path)
            val intent = Intent(this@ViewSavedImageActivity, FaceChangeActivity::class.java)
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
            val uri = Uri.parse(path)
            val intent = Intent(this@ViewSavedImageActivity, MenPhotoActivity::class.java)
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
//            val intent = Intent(this, com.apploop.face.changer.app.views.handCrop.HandCropActivity::class.java)
            call()
//            val myUri = Uri.parse(path)
//            intent.putExtra("image_uri", myUri.toString())
//            intent.putExtra("path", myUri.toString())
//            startActivity(intent)

            UtilsCons.originalBitmap = null
            UtilsCons.originalPath = ""
            UtilsCons.fromGallery = ""
            UtilsCons.originalBitmap = BitmapFactory.decodeFile(path)
            UtilsCons.originalPath = Extension.imageFilePath

            alertDialog.dismiss()
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            try {
                when (requestCode) {

                    CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                        val result: CropImage.ActivityResult = CropImage.getActivityResult(data)

                        var resultUri = result.uri
                        var bitmap: Bitmap? = null
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, resultUri)
                            UtilsCons.originalBitmap = bitmap
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }

                        if (UtilsCons.chooseLayout.contains("PHOTO_REMOVE_BG")) {
                            call()
                        } else if (UtilsCons.chooseLayout.contains("PHOTO_MEN")) {
                            val intent = Intent(this@ViewSavedImageActivity, MenPhotoActivity::class.java)
                            startActivity(intent)
                        } else {
                            val intent = Intent(this@ViewSavedImageActivity, FaceChangeActivity::class.java)
                            startActivity(intent)
                        }

                        UtilsCons.originalPath = ""
                        UtilsCons.fromGallery = ""
//                        UtilsCons.originalBitmap = resultUri
                        Log.e("originalBitmap", UtilsCons.originalBitmap.toString())
                        UtilsCons.originalPath = Extension.imageFilePath
                        Log.e("originalPath", UtilsCons.originalPath.toString())
                    }

                    CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE -> {
                        Toast.makeText(this@ViewSavedImageActivity, "error", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: java.lang.Exception) {
                Log.e("originalPathException", e.message.toString())
                Toast.makeText(this@ViewSavedImageActivity, "" + e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun call() {
        try {
            StoreManager.setCurrentCropedBitmap(this@ViewSavedImageActivity, null as Bitmap?)
            StoreManager.setCurrentCroppedMaskBitmap(this@ViewSavedImageActivity, null as Bitmap?)
            ImageRemoveBgActivity.setFaceBitmap(UtilsCons.originalBitmap)
            StoreManager.setCurrentOriginalBitmap(this@ViewSavedImageActivity, UtilsCons.originalBitmap)
            startActivity(Intent(this@ViewSavedImageActivity, ImageRemoveBgActivity::class.java))
            finish()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

}