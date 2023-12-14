package com.apploop.face.changer.app.views.RemoveBgScreen

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.apploop.face.changer.app.R
import com.apploop.face.changer.app.bottomsheets.ShowBackgroundsBottomSheet
import com.apploop.face.changer.app.callBacks.AddStickerBottomSheetViewModelInterface
import com.apploop.face.changer.app.callBacks.BackgroundBottomSheetInterface
import com.apploop.face.changer.app.callBacks.SelectSuitViewModelInterface
import com.apploop.face.changer.app.callBacks.StickerViewModelInterface
import com.apploop.face.changer.app.databinding.ActivityRemoveBg2Binding
import com.apploop.face.changer.app.helpers.EnumClass
import com.apploop.face.changer.app.helpers.onTouch.MultiTouchListener
import com.apploop.face.changer.app.helpers.stickerviewclass.StickerImageView
import com.apploop.face.changer.app.helpers.stickerviewclass.StickerView
import com.apploop.face.changer.app.utils.Extension
import com.apploop.face.changer.app.utils.Extension.getBitmapFromView
import com.apploop.face.changer.app.utils.Extension.initLists
import com.apploop.face.changer.app.utils.Extension.loadBitmap
import com.apploop.face.changer.app.utils.Extension.saveBitmapLast
import com.apploop.face.changer.app.utils.Extension.statusBarColor
import com.apploop.face.changer.app.utils.UtilsCons
import com.apploop.face.changer.app.viewModels.SelectSuitViewModel
import com.apploop.face.changer.app.viewModels.StickerViewModel
import com.apploop.face.changer.app.views.saved.ImageAdsSavedActivity
import com.apploop.face.changer.app.views.stickers.StickerActivity
import java.util.Random

class RemoveBgActivity : AppCompatActivity() , StickerViewModelInterface,
    AddStickerBottomSheetViewModelInterface, BackgroundBottomSheetInterface,
    SelectSuitViewModelInterface {

    lateinit var binding : ActivityRemoveBg2Binding
    private lateinit var stickerViewModel: StickerViewModel
    private var sticker: StickerImageView? = null
    private val multiTouchListener =
        MultiTouchListener()
    private lateinit var selectSuitViewModel: SelectSuitViewModel
    interface OnStickerTouch {
        fun onTouch(action: Int)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_remove_bg2)
        statusBarColor(R.color.background)
        init()
    }

    private fun init() {
//        binding.shimmerFrameLayout.startShimmer()
//        AdsManager.Companion.instance!!.showAdMobBanner(this, binding.frameLayout){
//            if (it)
//            {
//                binding.shimmerFrameLayout.visibility = View.INVISIBLE
//            }
//            else
//            {
//                binding.shimmerFrameLayout.visibility = View.GONE
//                binding.frameLayout.visibility = View.GONE
//            }
//        }

        selectSuitViewModel = SelectSuitViewModel(this)
        stickerViewModel = StickerViewModel(this)
        binding.selectSuitViewModel = selectSuitViewModel
        binding.stickerViewModel = stickerViewModel
        if (UtilsCons.originalBitmap != null) {
            loadBitmap(binding.ivSuit, UtilsCons.originalBitmap)
        }

        binding.lvEdit1.setOnClickListener {

//            getBitmapFromView(binding.lvRoot)?.let {
//                binding.progressBar.visibility = View.VISIBLE
//                window.setFlags(
//                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
//                )
//
//                if (saveBitmapLast != null) {
//                    saveBitmapLast?.recycle()
//                }
//                saveBitmapLast = it
//                val intent = Intent(this@RemoveBgActivity, ImageAdsSavedActivity::class.java)
//                startActivity(intent)
//                finish()
//            }
        }

        multiTouchListener.setOnMultiTouch(object : StickerActivity.OnStickerTouch {
            override fun onTouch(action: Int) {
                if (action == MotionEvent.ACTION_DOWN) {
                    removeBorder()
                } else if (action == MotionEvent.ACTION_UP) {
                    removeBorder()
                }
            }
        })

        binding.ivSuit.setOnTouchListener(multiTouchListener)

        binding.lvRoot.setOnClickListener {
            removeBorder()
        }
    }
    private val onTouchSticker: StickerView.OnTouchSticker =
        StickerView.OnTouchSticker { stickerImageView ->
            sticker = stickerImageView as StickerImageView
            removeBorder()
        }

    private fun removeBorder() {
        try {
            if (Extension.stickerViewId.size > 0) {
                for (i in Extension.stickerViewId.indices) {
                    if (Extension.stickerViewId.isNotEmpty()) {
                        val view: View = binding.lvRoot.findViewById<View>(Extension.stickerViewId[i])
                        if (view is StickerImageView) {
                            val stickerView: StickerImageView = view
                            stickerView.setControlItemsHidden(true)
                        }
                    }
                }
            }
        } catch (e: NullPointerException) {
            e.localizedMessage
        }
    }

    override fun onAddStickerBottomSheetButtonClicks(path: String) {

        binding.ivSuit.setOnTouchListener(multiTouchListener)
        sticker = StickerImageView(
            this,
            onTouchSticker
        )
        Glide.with(applicationContext)
            .asBitmap()
            .load(path)
            .into(object : SimpleTarget<Bitmap?>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap?>?
                ) {
                    sticker!!.imageBitmap = resource
                }
            })

        val r = Random()
        Extension.viewId = r.nextInt()
        if (Extension.viewId < 0) {
            Extension.viewId -= Extension.viewId * 2
        }
        sticker!!.id = Extension.viewId

        Extension.stickerViewId.add(Extension.viewId)

        sticker!!.setOnClickListener(View.OnClickListener {
            sticker!!.setControlItemsHidden(true)
        })
        binding.lvRoot.addView(sticker)
    }

    override fun onBackgroundBottomSheetButtonClicks(path: String) {
        binding.ivBackGround.colorFilter = null
        val filePath = "file:///android_asset/$path"
        Glide.with(applicationContext)
            .load(filePath)
            .into(binding.ivBackGround)

        binding.ivBackGround.invalidate()
    }

    override fun onStickerButtonClicks(type: EnumClass) {
//        removeBorder()
        when (type) {

            EnumClass.STICKERS -> {
                binding.lvBackgroundContainer.visibility = View.GONE
                binding.ivSuit.setOnTouchListener(null)
            }

            EnumClass.BACKGROUND -> {

                if (binding.lvBackgroundContainer.visibility == View.VISIBLE) {
                    binding.lvBackgroundContainer.visibility = View.GONE
                    val touchListener =
                        MultiTouchListener()
                    binding.ivSuit.setOnTouchListener(touchListener)
                    return
                }
                binding.lvBackgroundContainer.visibility = View.VISIBLE

                binding.ivSuit.setOnTouchListener(null)
            }

            EnumClass.ZOOM -> {
                binding.lvBackgroundContainer.visibility = View.GONE
                val touchListener =
                    MultiTouchListener()
                binding.ivSuit.setOnTouchListener(touchListener)
            }

            EnumClass.BACK -> {
                onBackPressed()
            }

            EnumClass.STICKER_COLOR -> {

                onBackPressed()
            }

            EnumClass.OPACITY -> {

                onBackPressed()
            }

            EnumClass.BACKGROUND_COLOR -> {
                if (binding.lvColorsCodeContainer.visibility == View.VISIBLE) {
                    binding.lvColorsCodeContainer.visibility = View.GONE
                    binding.ivColorBackground.setColorFilter(
                        ContextCompat.getColor(
                            this,
                            R.color.light_grey
                        )
                    )
                    binding.tvColorBackground.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.light_grey
                        )
                    )
                    return
                }
                binding.ivAddImage.setColorFilter(ContextCompat.getColor(this, R.color.light_grey))
                binding.ivColorBackground.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.purple_status
                    )
                )
                binding.ivNone.setColorFilter(ContextCompat.getColor(this, R.color.light_grey))
                binding.tvAddImage.setTextColor(ContextCompat.getColor(this, R.color.light_grey))
                binding.tvColorBackground.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.purple_status
                    )
                )
                binding.tvNone.setTextColor(ContextCompat.getColor(this, R.color.light_grey))
                binding.lvColorsCodeContainer.visibility = View.VISIBLE
            }

            EnumClass.BLACK1 -> {
                binding.ivBlack1.visibility = View.VISIBLE
                binding.ivRed1.visibility = View.GONE
                binding.ivBlue1.visibility = View.GONE
                binding.ivGreen1.visibility = View.GONE
                binding.ivYellow1.visibility = View.GONE
                binding.ivPurple1.visibility = View.GONE
                binding.ivGray1.visibility = View.GONE
                binding.ivBackGround.colorFilter = null
                val filePath = ""
                Glide.with(this)
                    .load(filePath)
                    .into(binding.ivBackGround)
                binding.lvRoot.setBackgroundColor(resources.getColor(R.color.black))
            }

            EnumClass.RED1 -> {
                binding.ivBlack1.visibility = View.GONE
                binding.ivRed1.visibility = View.VISIBLE
                binding.ivBlue1.visibility = View.GONE
                binding.ivGreen1.visibility = View.GONE
                binding.ivYellow1.visibility = View.GONE
                binding.ivPurple1.visibility = View.GONE
                binding.ivGray1.visibility = View.GONE
                binding.ivBackGround.colorFilter = null
                val filePath = ""
                Glide.with(this)
                    .load(filePath)
                    .into(binding.ivBackGround)
                binding.lvRoot.setBackgroundColor(resources.getColor(R.color.purple_status))
            }

            EnumClass.BLUE1 -> {
                binding.ivBlack1.visibility = View.GONE
                binding.ivRed1.visibility = View.GONE
                binding.ivBlue1.visibility = View.VISIBLE
                binding.ivGreen1.visibility = View.GONE
                binding.ivYellow1.visibility = View.GONE
                binding.ivPurple1.visibility = View.GONE
                binding.ivGray1.visibility = View.GONE
                binding.ivBackGround.colorFilter = null
                val filePath = ""
                Glide.with(applicationContext)
                    .load(filePath)
                    .into(binding.ivBackGround)
                binding.lvRoot.setBackgroundColor(resources.getColor(R.color.blue))
            }

            EnumClass.GREEN1 -> {
                binding.ivBlack1.visibility = View.GONE
                binding.ivRed1.visibility = View.GONE
                binding.ivBlue1.visibility = View.GONE
                binding.ivGreen1.visibility = View.VISIBLE
                binding.ivYellow1.visibility = View.GONE
                binding.ivPurple1.visibility = View.GONE
                binding.ivGray1.visibility = View.GONE
                binding.ivBackGround.colorFilter = null
                val filePath = ""
                Glide.with(this)
                    .load(filePath)
                    .into(binding.ivBackGround)
                binding.lvRoot.setBackgroundColor(resources.getColor(R.color.green))
            }

            EnumClass.YELLOW1 -> {
                binding.ivBlack1.visibility = View.GONE
                binding.ivRed1.visibility = View.GONE
                binding.ivBlue1.visibility = View.GONE
                binding.ivGreen1.visibility = View.GONE
                binding.ivYellow1.visibility = View.VISIBLE
                binding.ivPurple1.visibility = View.GONE
                binding.ivGray1.visibility = View.GONE
                binding.ivBackGround.colorFilter = null
                val filePath = ""
                Glide.with(this)
                    .load(filePath)
                    .into(binding.ivBackGround)
                binding.lvRoot.setBackgroundColor(resources.getColor(R.color.yellow))
            }

            EnumClass.PURPLE1 -> {
                binding.ivBlack1.visibility = View.GONE
                binding.ivRed1.visibility = View.GONE
                binding.ivBlue1.visibility = View.GONE
                binding.ivGreen1.visibility = View.GONE
                binding.ivYellow1.visibility = View.GONE
                binding.ivPurple1.visibility = View.VISIBLE
                binding.ivGray1.visibility = View.GONE
                binding.ivBackGround.colorFilter = null
                val filePath = ""
                Glide.with(this)
                    .load(filePath)
                    .into(binding.ivBackGround)
                binding.lvRoot.setBackgroundColor(resources.getColor(R.color.purple))
            }

            EnumClass.GRAY1 -> {
                binding.ivBackGround.colorFilter = null
                val filePath = ""
                Glide.with(this)
                    .load(filePath)
                    .into(binding.ivBackGround)
                binding.ivBlack1.visibility = View.GONE
                binding.ivRed1.visibility = View.GONE
                binding.ivBlue1.visibility = View.GONE
                binding.ivGreen1.visibility = View.GONE
                binding.ivYellow1.visibility = View.GONE
                binding.ivPurple1.visibility = View.GONE
                binding.ivGray1.visibility = View.VISIBLE
                binding.lvRoot.setBackgroundColor(resources.getColor(R.color.gray))
            }

            EnumClass.NONE -> {
                binding.ivAddImage.setColorFilter(ContextCompat.getColor(this, R.color.light_grey))
                binding.ivColorBackground.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.light_grey
                    )
                )
                binding.ivNone.setColorFilter(ContextCompat.getColor(this, R.color.light_grey))
                binding.tvAddImage.setTextColor(ContextCompat.getColor(this, R.color.light_grey))
                binding.tvColorBackground.setTextColor(ContextCompat.getColor(this, R.color.light_grey))
                binding.tvNone.setTextColor(ContextCompat.getColor(this, R.color.light_grey))
                binding.ivBackGround.colorFilter = null
                val filePath = ""
                Glide.with(this)
                    .load(filePath)
                    .into(binding.ivBackGround)
                binding.lvRoot.setBackgroundColor(resources.getColor(R.color.white))
            }

            EnumClass.BACKGROUND_IMAGES -> {
                binding.ivAddImage.setColorFilter(ContextCompat.getColor(this, R.color.purple_status))
                binding.ivColorBackground.setColorFilter(ContextCompat.getColor(this, R.color.light_grey))
                binding.ivNone.setColorFilter(ContextCompat.getColor(this, R.color.light_grey))
                binding.tvAddImage.setTextColor(ContextCompat.getColor(this, R.color.purple_status))
                binding.tvColorBackground.setTextColor(ContextCompat.getColor(this, R.color.light_grey))
                binding.tvNone.setTextColor(ContextCompat.getColor(this, R.color.light_grey))
                initLists()

                binding.lvColorsCodeContainer.visibility = View.GONE
                ShowBackgroundsBottomSheet(
                    this@RemoveBgActivity,
                    Extension.objSuitOptions,
                    this
                ).apply {
                    show(supportFragmentManager, tag)
                }
            }

            EnumClass.DONE -> {
//                removeBorder()
                getBitmapFromView(binding.lvRoot)?.let {
                    binding.progressBar.visibility = View.VISIBLE
                    window.setFlags(
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    )

                    if (saveBitmapLast != null) {
                        saveBitmapLast?.recycle()
                    }
                    saveBitmapLast = it
                    val intent = Intent(this@RemoveBgActivity, ImageAdsSavedActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

            else -> {}
        }

    }

    override fun onSelectSuitButtonClicks(type: EnumClass) {
        when (type) {
            EnumClass.FLIP_IMAGE -> {
                binding.ivSuit.rotationY =
                    if (binding.ivSuit.rotationY == -180f) 0f else -180f
                binding.ivSuit.invalidate()
            }

            else -> {}
        }
    }

}