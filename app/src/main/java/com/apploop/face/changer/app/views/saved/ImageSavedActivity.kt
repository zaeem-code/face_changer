package com.apploop.face.changer.app.views.saved

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.apploop.face.changer.app.R
import com.apploop.face.changer.app.databinding.ActivityImageSavedBinding
import com.apploop.face.changer.app.manager.AdsManager
import com.apploop.face.changer.app.utils.Constants
import com.apploop.face.changer.app.utils.Extension.rateApp
import com.apploop.face.changer.app.utils.Extension.share
import com.apploop.face.changer.app.utils.Extension.statusBarColor
import com.apploop.face.changer.app.utils.SharedPrefHelper
import com.apploop.face.changer.app.views.mainactivity.MainActivity

class ImageSavedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImageSavedBinding
    private var path: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_saved)
        statusBarColor(R.color.background)
        init()
    }

    private fun init() {
//        if (!SharedPrefHelper.readBoolean("isOpened")) {
//            Handler(Looper.getMainLooper()).postDelayed(Runnable {
//                alertDialogRate()
//            }, 3000)
//        }
        binding.shimmerFrameLayout.startShimmer()
        AdsManager.Companion.instance!!.showNativeAd(
            binding!!.frameLayout,
            binding!!.frameLayout,
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
        path = intent.extras!!.getString("savedPath")!!
        Thread {
            binding.ivSavedImage.post(Runnable {
                Glide.with(this)
                    .load(path)
                    .into(binding.ivSavedImage)
            })
        }.start()

        binding.ivBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
//
//        binding.lvCamera.setOnClickListener {
//            share(path)
//        }
//
//        binding.lvGallery.setOnClickListener {
//            rateApp()
//        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }


    override fun onDestroy() {
        super.onDestroy()
        SharedPrefHelper.writeBoolean(Constants.IN_APP_KEY, true)
    }
}