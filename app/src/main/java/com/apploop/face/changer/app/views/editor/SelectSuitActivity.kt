package com.apploop.face.changer.app.views.editor

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.apploop.face.changer.app.R
import com.apploop.face.changer.app.bottomsheets.SelectSuitBottomSheet
import com.apploop.face.changer.app.callBacks.SelectSuitViewModelInterface
import com.apploop.face.changer.app.callBacks.SuitBottomSheetViewModelInterface
import com.apploop.face.changer.app.databinding.ActivityEditorBinding
import com.apploop.face.changer.app.helpers.EnumClass
import com.apploop.face.changer.app.helpers.onTouch.MultiTouchListener
import com.apploop.face.changer.app.manager.AdsManager
import com.apploop.face.changer.app.manager.OnAdLoaded
import com.apploop.face.changer.app.utils.Extension.getBitmapFromView
import com.apploop.face.changer.app.utils.Extension.loadBitmap
import com.apploop.face.changer.app.utils.Extension.loadImage
import com.apploop.face.changer.app.utils.Extension.statusBarColor
import com.apploop.face.changer.app.utils.Extension.suitPath
import com.apploop.face.changer.app.utils.UtilsCons
import com.apploop.face.changer.app.viewModels.SelectSuitViewModel
import com.apploop.face.changer.app.views.eraser.EraserActivity
import com.apploop.face.changer.app.views.stickers.StickerActivity

class SelectSuitActivity : AppCompatActivity(), SelectSuitViewModelInterface,
    SuitBottomSheetViewModelInterface {

    private lateinit var binding: ActivityEditorBinding
    private lateinit var selectSuitViewModel: SelectSuitViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_editor)
        statusBarColor(R.color.background)
        init()
        initClicks()
    }

    private fun init() {
        binding.shimmerFrameLayout.startShimmer()
        AdsManager.Companion.instance!!.showAdMobBanner(this, binding.frameLayout){
            if (it)
            {
                binding.shimmerFrameLayout.visibility = View.INVISIBLE
            }
            else
            {
                binding.shimmerFrameLayout.visibility = View.GONE
                binding.frameLayout.visibility = View.GONE
            }
        }
        selectSuitViewModel = SelectSuitViewModel(this)
        binding.selectSuitViewModel = selectSuitViewModel
        if (UtilsCons.originalBitmap != null) {
            loadBitmap(binding.ivCropped, UtilsCons.originalBitmap)
            loadImage(binding.ivSuit)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initClicks() {
        binding.ivCropped.setOnTouchListener(com.apploop.face.changer.app.helpers.onTouch.MultiTouchListener())
    }

    private fun colorDefault() {
        binding.ivSelectSuit.setColorFilter(
            ContextCompat.getColor(
                applicationContext,
                R.color.white
            )
        )
        binding.tvSuit.setTextColor(resources.getColor(R.color.white))
    }


    override fun onSelectSuitButtonClicks(type: EnumClass) {
        when (type) {
            EnumClass.FLIP_SUIT -> {
                binding.ivSelectSuit.setColorFilter(ContextCompat.getColor(this, R.color.black))
                binding.ivFlipSuit.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.purple_status
                    )
                )
                binding.ivFlipImage.setColorFilter(ContextCompat.getColor(this, R.color.black))
                binding.ivEdit.setColorFilter(ContextCompat.getColor(this, R.color.black))
                binding.tvSuit.setTextColor(ContextCompat.getColor(this, R.color.black))
                binding.tvFlipSuit.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.purple_status
                    )
                )
                binding.tvFlipImage.setTextColor(ContextCompat.getColor(this, R.color.black))
                binding.tvEdit.setTextColor(ContextCompat.getColor(this, R.color.black))
                binding.ivSuit.rotationY = if (binding.ivSuit.rotationY == -180f) 0f else -180f
                binding.ivSuit.invalidate()
            }
            EnumClass.FLIP_IMAGE -> {
                binding.ivSelectSuit.setColorFilter(ContextCompat.getColor(this, R.color.black))
                binding.ivFlipSuit.setColorFilter(ContextCompat.getColor(this, R.color.black))
                binding.ivFlipImage.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.purple_status
                    )
                )
                binding.ivEdit.setColorFilter(ContextCompat.getColor(this, R.color.black))
                binding.tvSuit.setTextColor(ContextCompat.getColor(this, R.color.black))
                binding.tvFlipSuit.setTextColor(ContextCompat.getColor(this, R.color.black))
                binding.tvFlipImage.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.purple_status
                    )
                )
                binding.tvEdit.setTextColor(ContextCompat.getColor(this, R.color.black))
                binding.ivCropped.rotationY =
                    if (binding.ivCropped.rotationY == -180f) 0f else -180f
                binding.ivCropped.invalidate()
            }
            EnumClass.BACK -> {
                finish()
            }
            EnumClass.DONE -> {
                if (UtilsCons.originalBitmap != null) {
//                    UtilsCons.originalBitmap.recycle()
                }
                UtilsCons.originalBitmap = getBitmapFromView(binding.fySuit)
                val intent = Intent(this, StickerActivity::class.java)
                startActivity(intent)
                finish()
            }
            EnumClass.SUIT -> {
                binding.ivSelectSuit.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.purple_status
                    )
                )
                binding.ivFlipSuit.setColorFilter(ContextCompat.getColor(this, R.color.black))
                binding.ivFlipImage.setColorFilter(ContextCompat.getColor(this, R.color.black))
                binding.ivEdit.setColorFilter(ContextCompat.getColor(this, R.color.black))
                binding.tvSuit.setTextColor(ContextCompat.getColor(this, R.color.purple_status))
                binding.tvFlipSuit.setTextColor(ContextCompat.getColor(this, R.color.black))
                binding.tvFlipImage.setTextColor(ContextCompat.getColor(this, R.color.black))
                binding.tvEdit.setTextColor(ContextCompat.getColor(this, R.color.black))
                SelectSuitBottomSheet(this, this).apply {
                    show(supportFragmentManager, tag)
                }
            }
            EnumClass.EDIT -> {
                binding.ivSelectSuit.setColorFilter(ContextCompat.getColor(this, R.color.black))
                binding.ivFlipSuit.setColorFilter(ContextCompat.getColor(this, R.color.black))
                binding.ivFlipImage.setColorFilter(ContextCompat.getColor(this, R.color.black))
                binding.ivEdit.setColorFilter(ContextCompat.getColor(this, R.color.purple_status))
                binding.tvSuit.setTextColor(ContextCompat.getColor(this, R.color.black))
                binding.tvFlipSuit.setTextColor(ContextCompat.getColor(this, R.color.black))
                binding.tvFlipImage.setTextColor(ContextCompat.getColor(this, R.color.black))
                binding.tvEdit.setTextColor(ContextCompat.getColor(this, R.color.purple_status))
                try {
                    val bitmapSuit = getBitmapFromView(binding.fySuit)
                    if (bitmapSuit != null) {

                        binding.progressBar.visibility = View.VISIBLE
                        window.setFlags(
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                        )
                        AdsManager.Companion.instance!!.showInterstitialAd(this, object : OnAdLoaded {
                            override fun OnAdLoadedCallBack(loaded: Boolean?) {
                                binding.progressBar.visibility = View.GONE
                                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                                UtilsCons.originalBitmap = bitmapSuit
                                val i = Intent(this@SelectSuitActivity, EraserActivity::class.java)
                                startActivity(i)
                            }
                        })
                    } else {
                        Toast.makeText(this, "please, select image", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: NullPointerException) {
                    e.localizedMessage
                }
            }
            EnumClass.PHOTO -> {
                val touchListener =
                    com.apploop.face.changer.app.helpers.onTouch.MultiTouchListener()
                binding.ivCropped.setOnTouchListener(touchListener)
                binding.ivSuit.setOnTouchListener(null)
                binding.ivButton.setBackgroundResource(R.drawable.cus_light_purple_bg_rounded)
                binding.tvUpgrade.setTextColor(resources.getColor(R.color.white))
                binding.ivSuitMove.setBackgroundResource(R.drawable.cus_white_bg_rounded)
                binding.tvSuitMove.setTextColor(resources.getColor(R.color.black))
            }
            EnumClass.MOVE_SUIT -> {
                val touchListener =
                    com.apploop.face.changer.app.helpers.onTouch.MultiTouchListener()
                binding.ivSuit.setOnTouchListener(touchListener)
                binding.ivCropped.setOnTouchListener(null)
                binding.ivButton.setBackgroundResource(R.drawable.cus_white_bg_rounded)
                binding.tvUpgrade.setTextColor(resources.getColor(R.color.black))
                binding.ivSuitMove.setBackgroundResource(R.drawable.cus_light_purple_bg_rounded)
                binding.tvSuitMove.setTextColor(resources.getColor(R.color.white))
            }
            else -> {}
        }
    }

    override fun onSuitBottomSheetButtonClicks(path: String) {
        val filePath = "file:///android_asset/$path"
        suitPath = filePath
        loadImage(binding.ivSuit)
    }
}