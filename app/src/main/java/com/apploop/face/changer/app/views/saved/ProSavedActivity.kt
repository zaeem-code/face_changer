package com.apploop.face.changer.app.views.saved

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.apploop.face.changer.app.R
import com.apploop.face.changer.app.app.App
import com.apploop.face.changer.app.databinding.ActivityProSavedBinding
import com.apploop.face.changer.app.manager.AdsManager
import com.apploop.face.changer.app.utils.Extension
import com.apploop.face.changer.app.utils.Extension.createDirectoryAndSaveFile
import com.apploop.face.changer.app.utils.Extension.getBitmapFromView
import com.apploop.face.changer.app.utils.Extension.statusBarColor
import com.apploop.face.changer.app.views.mainactivity.MainActivity
import com.bumptech.glide.Glide

class ProSavedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProSavedBinding
    private lateinit var path: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProSavedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        statusBarColor(R.color.background)
        init()

        path = intent.extras!!.getString("savedPath")!!

        Thread {
            binding.ivSavedImage.post(Runnable {
                Glide.with(this)
                    .load(Extension.saveBitmapLast)
                    .into(binding.ivSavedImage)
            })

            binding.ivSavedImage1.post(Runnable {
                Glide.with(this)
                    .load(Extension.saveBitmapLast)
                    .into(binding.ivSavedImage1)
            })
        }.start()

    }

    private fun init() {

        binding.btnContinue.setOnClickListener {
            getBitmapFromView(binding.lvImageContainer)?.let {
                val savedPath = createDirectoryAndSaveFile(it)
                val intent = Intent(this@ProSavedActivity, ImageSavedActivity::class.java)
                intent.putExtra("savedPath", savedPath)
                startActivity(intent)
                finish()
            }
        }

        binding.closeIMG.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        binding.btnWatchAd.setOnClickListener {
            binding.mainLay.visibility= View.GONE
            binding.adsLayout.visibility=View.VISIBLE
            AdsManager.instance?.showInterstitialAd(this) {
                startSavedActivity()
                binding.adsLayout.visibility=View.GONE

            }



        }
    }

    private fun startSavedActivity() {
        val intent = Intent(this, ImageSavedActivity::class.java)
        intent.putExtra("savedPath", path)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
}