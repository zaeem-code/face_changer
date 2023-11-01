package com.apploop.face.changer.app.views.crop

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.apploop.face.changer.app.R
import com.apploop.face.changer.app.cropModule.UCrop
import com.apploop.face.changer.app.cropModule.UCropFragment
import com.apploop.face.changer.app.cropModule.callback.UCropFragmentCallback
import com.apploop.face.changer.app.databinding.ActivityCropBinding
import com.apploop.face.changer.app.utils.Extension.handleCropError
import com.apploop.face.changer.app.utils.Extension.isOpenRecently
import com.apploop.face.changer.app.utils.Extension.statusBarColor
import com.apploop.face.changer.app.utils.Extension.uriToBitmap
import com.apploop.face.changer.app.utils.UtilsCons
import com.apploop.face.changer.app.views.MenPhotoScreen.MenPhotoActivity
import com.apploop.face.changer.app.views.RemoveBgScreen.RemoveBgActivity
import java.io.File

class CropActivity : AppCompatActivity(),
    com.apploop.face.changer.app.cropModule.callback.UCropFragmentCallback {

    private lateinit var binding: ActivityCropBinding
    private var fragment: com.apploop.face.changer.app.cropModule.UCropFragment =
        com.apploop.face.changer.app.cropModule.UCropFragment()
    private var mShowLoader = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_crop)
        statusBarColor(R.color.background)
        init()
        initClicks()
    }

    private fun init() {
        val imageUri = intent.getStringExtra("image_uri")
        startCrop(Uri.parse(imageUri))
    }

    private fun initClicks() {
        binding.ivDone.setOnClickListener {
            if (isOpenRecently()) {
                return@setOnClickListener
            }
            fragment.cropAndSaveImage(this@CropActivity)
        }
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun startCrop(uri: Uri) {
        val uCrop = com.apploop.face.changer.app.cropModule.UCrop.of(uri, Uri.fromFile(File(cacheDir, ".png")))
        setupFragment(uCrop)
    }

    private fun setupFragment(uCrop: com.apploop.face.changer.app.cropModule.UCrop) {
        fragment = uCrop.getFragment(uCrop.getIntent(this).extras)
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, fragment, com.apploop.face.changer.app.cropModule.UCropFragment.TAG)
            .commitAllowingStateLoss()
    }

    override fun loadingProgress(showLoader: Boolean) {
        mShowLoader = showLoader
        supportInvalidateOptionsMenu()
    }

    override fun onCropFinish(result: com.apploop.face.changer.app.cropModule.UCropFragment.UCropResult) {
        when (result.mResultCode) {
            RESULT_OK -> {
                handleCropResult(result.mResultData)
            }

            com.apploop.face.changer.app.cropModule.UCrop.RESULT_ERROR -> {
                handleCropError(result.mResultData)
            }
        }
    }

    private fun handleCropResult(result: Intent) {
        val resultUri = com.apploop.face.changer.app.cropModule.UCrop.getOutput(result)
        if (resultUri != null) {
            if (com.apploop.face.changer.app.utils.UtilsCons.chooseLayout.contains("PHOTO_REMOVE_BG")) {
                val intent = Intent(this@CropActivity, RemoveBgActivity::class.java)
                startActivity(intent)

            } else if (com.apploop.face.changer.app.utils.UtilsCons.chooseLayout.contains("PHOTO_MEN")) {
                val intent = Intent(this@CropActivity, MenPhotoActivity::class.java)
                startActivity(intent)

            } else {
                val intent = Intent(this@CropActivity, MenPhotoActivity::class.java)
                startActivity(intent)
            }

            com.apploop.face.changer.app.utils.UtilsCons.originalBitmap = uriToBitmap(resultUri)
            removeFragmentFromScreen()
            this.finish()
        } else {
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun removeFragmentFromScreen() {
        supportFragmentManager.beginTransaction()
            .remove(fragment)
            .commit()
    }
}