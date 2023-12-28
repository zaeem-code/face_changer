package com.apploop.face.changer.app.views.saved

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.apploop.face.changer.app.R
import com.apploop.face.changer.app.databinding.ActivityImageAdsSavedBinding
import com.apploop.face.changer.app.manager.AdsManager
import com.apploop.face.changer.app.utils.Extension.alertDiscardDialog
import com.apploop.face.changer.app.utils.Extension.createDirectoryAndSaveFile
import com.apploop.face.changer.app.utils.Extension.getBitmapFromView
import com.apploop.face.changer.app.utils.Extension.saveBitmapLast
import com.apploop.face.changer.app.utils.Extension.share
import com.apploop.face.changer.app.utils.Extension.statusBarColor
import com.apploop.face.changer.app.utils.SharedPrefHelper
import com.bumptech.glide.Glide

class ImageAdsSavedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImageAdsSavedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_ads_saved)
        statusBarColor(R.color.background)
        init()


        binding.shimmerFrameLayout.startShimmer()
        AdsManager.Companion.instance!!.showNativeAd(
            binding.frameLayout,
            binding.frameLayout,
            layoutInflater,
            R.layout.ad_media
        ) {
            if (it) {
                binding.shimmerFrameLayout.visibility = View.INVISIBLE
            } else {
                binding.shimmerFrameLayout.visibility = View.INVISIBLE
                binding.frameLayout.visibility = View.INVISIBLE
            }
        }

    }

    private fun init() {
        Thread {
            binding.ivSavedImage.post(Runnable {
                Glide.with(this)
                    .load(saveBitmapLast)
                    .into(binding.ivSavedImage)
            })
        }.start()

        binding.shareIMG.setOnClickListener {
            getBitmapFromView(binding.ivSavedImage)?.let {
                val savedPath = createDirectoryAndSaveFile(it)
                share(savedPath)
            }
        }

        binding.lvSave.setOnClickListener {
            alertDiscardDialog()

//            getBitmapFromView(binding.lvImageContainer)?.let {
//                val savedPath = createDirectoryAndSaveFile(it)
//                val intent =
//                    Intent(this@ImageAdsSavedActivity, ImageSavedActivity::class.java)
//                intent.putExtra("savedPath", savedPath)
//                startActivity(intent)
//                finish()
//            }
        }

        binding.lvAd.setOnClickListener {
            SharedPrefHelper.writeBoolean("lastAds",true)
            binding.progressBar.visibility = View.VISIBLE
            getBitmapFromView(binding.ivSavedImage)?.let {
                binding.progressBar.visibility = View.GONE
                val savedPath = createDirectoryAndSaveFile(it)
                val intent = Intent(this@ImageAdsSavedActivity, ProSavedActivity::class.java)
                intent.putExtra("savedPath", savedPath)
                startActivity(intent)
                finish()
            }



        }
    }
}