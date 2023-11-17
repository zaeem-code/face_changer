package com.apploop.face.changer.app.views.stickers

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.apploop.face.changer.app.R
import com.apploop.face.changer.app.bottomsheets.ShowBackgroundsBottomSheet
import com.apploop.face.changer.app.callBacks.AddStickerBottomSheetViewModelInterface
import com.apploop.face.changer.app.callBacks.BackgroundBottomSheetInterface
import com.apploop.face.changer.app.callBacks.StickerViewModelInterface
import com.apploop.face.changer.app.databinding.ActivityStickerBinding
import com.apploop.face.changer.app.helpers.EnumClass
import com.apploop.face.changer.app.helpers.onTouch.MultiTouchListener
import com.apploop.face.changer.app.helpers.stickerviewclass.StickerImageView
import com.apploop.face.changer.app.helpers.stickerviewclass.StickerView
import com.apploop.face.changer.app.utils.Extension
import com.apploop.face.changer.app.utils.Extension.getBitmapFromView
import com.apploop.face.changer.app.utils.Extension.hairColors
import com.apploop.face.changer.app.utils.Extension.initLists
import com.apploop.face.changer.app.utils.Extension.loadBitmap
import com.apploop.face.changer.app.utils.Extension.saveBitmapLast
import com.apploop.face.changer.app.utils.Extension.statusBarColor
import com.apploop.face.changer.app.utils.Extension.stickerViewId
import com.apploop.face.changer.app.utils.Extension.viewId
import com.apploop.face.changer.app.utils.UtilsCons
import com.apploop.face.changer.app.viewModels.StickerViewModel
import com.apploop.face.changer.app.views.saved.ImageAdsSavedActivity
import java.util.*

class StickerActivity : AppCompatActivity(), StickerViewModelInterface,
    AddStickerBottomSheetViewModelInterface, BackgroundBottomSheetInterface {

    private lateinit var binding: ActivityStickerBinding
    private lateinit var stickerViewModel: StickerViewModel
    private var sticker: com.apploop.face.changer.app.helpers.stickerviewclass.StickerImageView? = null
    private val multiTouchListener =
        com.apploop.face.changer.app.helpers.onTouch.MultiTouchListener()

    interface OnStickerTouch {
        fun onTouch(action: Int)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sticker)
        statusBarColor(R.color.background)
        init()
        initClicks()
    }

    private fun init() {
//        binding.shimmerFrameLayout.startShimmer()
//        AdsManager.Companion.instance!!.showAdMobBanner(this, binding.frameLayout) {
//            if (it) {
//                binding.shimmerFrameLayout.visibility = View.INVISIBLE
//            } else {
//                binding.shimmerFrameLayout.visibility = View.GONE
//                binding.frameLayout.visibility = View.GONE
//            }
//        }

        stickerViewModel = StickerViewModel(this)
        binding.stickerViewModel = stickerViewModel
        if (com.apploop.face.changer.app.utils.UtilsCons.originalBitmap != null) {
            loadBitmap(binding.ivSuit, com.apploop.face.changer.app.utils.UtilsCons.originalBitmap)
        }
    }

    private fun initClicks() {
        multiTouchListener.setOnMultiTouch(object : OnStickerTouch {
            override fun onTouch(action: Int) {
                if (action == MotionEvent.ACTION_DOWN) {
                    removeBorder()
                } else if (action == MotionEvent.ACTION_UP) {
                    removeBorder()
                }
            }
        })

        binding.ivSuit.setOnTouchListener(multiTouchListener)

        binding.seekbar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (binding.lvRoot.childCount <= 2) {
                    binding.lvOpacitySeekBarContainer.visibility = View.GONE
                    return
                }
                removeBorder()
                sticker!!.opacity = progress.toFloat() / 255.0f
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        binding.lvRoot.setOnClickListener {
            removeBorder()
        }
    }

    private val onTouchSticker: com.apploop.face.changer.app.helpers.stickerviewclass.StickerView.OnTouchSticker =
        com.apploop.face.changer.app.helpers.stickerviewclass.StickerView.OnTouchSticker { stickerImageView ->
            sticker = stickerImageView as com.apploop.face.changer.app.helpers.stickerviewclass.StickerImageView
            removeBorder()
        }

    private fun removeBorder() {
        try {
            if (stickerViewId.size > 0) {
                for (i in stickerViewId.indices) {
                    if (stickerViewId.isNotEmpty()) {
                        val view: View = binding.lvRoot.findViewById<View>(stickerViewId[i])
                        if (view is com.apploop.face.changer.app.helpers.stickerviewclass.StickerImageView) {
                            val stickerView: com.apploop.face.changer.app.helpers.stickerviewclass.StickerImageView = view
                            stickerView.setControlItemsHidden(true)
                        }
                    }
                }
            }
        } catch (e: NullPointerException) {
            e.localizedMessage
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onAddStickerBottomSheetButtonClicks(path: String) {
        binding.lvBackgroundContainer.visibility = View.GONE
        binding.lvStickersContainer.visibility = View.GONE
//        val touchListener = MultiTouchListener()
        binding.ivSuit.setOnTouchListener(multiTouchListener)
        sticker = com.apploop.face.changer.app.helpers.stickerviewclass.StickerImageView(
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
        viewId = r.nextInt()
        if (viewId < 0) {
            viewId -= viewId * 2
        }
        sticker!!.id = viewId

        stickerViewId.add(viewId)

        sticker!!.setOnClickListener(View.OnClickListener {
            sticker!!.setControlItemsHidden(true)
        })
        binding.lvRoot.addView(sticker)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onStickerButtonClicks(type: EnumClass) {
        removeBorder()
        when (type) {
            EnumClass.STICKERS -> {
                binding.ivStickers.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.purple_status
                    )
                )
                binding.ivBackGrounds.setColorFilter(ContextCompat.getColor(this, R.color.black))
                binding.ivZoom.setColorFilter(ContextCompat.getColor(this, R.color.black))
                binding.tvStickers.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.purple_status
                    )
                )
                binding.tvBackground.setTextColor(ContextCompat.getColor(this, R.color.black))
                binding.tvZoom.setTextColor(ContextCompat.getColor(this, R.color.black))
                if (binding.lvStickersContainer.visibility == View.VISIBLE) {
                    binding.lvStickersContainer.visibility = View.GONE
                    val touchListener =
                        com.apploop.face.changer.app.helpers.onTouch.MultiTouchListener()
                    binding.ivSuit.setOnTouchListener(touchListener)
                    return
                }
                binding.lvStickersContainer.visibility = View.VISIBLE
                binding.lvBackgroundContainer.visibility = View.GONE
                binding.ivSuit.setOnTouchListener(null)
            }

            EnumClass.BACKGROUND -> {
                binding.ivStickers.setColorFilter(ContextCompat.getColor(this, R.color.black))
                binding.ivBackGrounds.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.purple_status
                    )
                )
                binding.ivZoom.setColorFilter(ContextCompat.getColor(this, R.color.black))
                binding.tvStickers.setTextColor(ContextCompat.getColor(this, R.color.black))
                binding.tvBackground.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.purple_status
                    )
                )
                binding.tvZoom.setTextColor(ContextCompat.getColor(this, R.color.black))
                if (binding.lvBackgroundContainer.visibility == View.VISIBLE) {
                    binding.lvBackgroundContainer.visibility = View.GONE
                    val touchListener =
                        com.apploop.face.changer.app.helpers.onTouch.MultiTouchListener()
                    binding.ivSuit.setOnTouchListener(touchListener)
                    return
                }
                binding.lvBackgroundContainer.visibility = View.VISIBLE
                binding.lvStickersContainer.visibility = View.GONE
                binding.ivSuit.setOnTouchListener(null)
            }

            EnumClass.ZOOM -> {
                binding.ivStickers.setColorFilter(ContextCompat.getColor(this, R.color.black))
                binding.ivBackGrounds.setColorFilter(ContextCompat.getColor(this, R.color.black))
                binding.ivZoom.setColorFilter(ContextCompat.getColor(this, R.color.purple_status))
                binding.tvStickers.setTextColor(ContextCompat.getColor(this, R.color.black))
                binding.tvBackground.setTextColor(ContextCompat.getColor(this, R.color.black))
                binding.tvZoom.setTextColor(ContextCompat.getColor(this, R.color.purple_status))
                binding.lvBackgroundContainer.visibility = View.GONE
                binding.lvStickersContainer.visibility = View.GONE
                val touchListener =
                    com.apploop.face.changer.app.helpers.onTouch.MultiTouchListener()
                binding.ivSuit.setOnTouchListener(touchListener)
            }

            EnumClass.BACK -> {
                onBackPressed()
            }

            EnumClass.STICKER_COLOR -> {
                if (binding.lvColorsCode.visibility == View.VISIBLE) {
                    binding.lvColorsCode.visibility = View.GONE
                    binding.ivColorStickers.setColorFilter(
                        ContextCompat.getColor(
                            this,
                            R.color.black
                        )
                    )
                    binding.tvColorStickers.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.black
                        )
                    )
                    return
                }
                binding.ivAddStickers.setColorFilter(ContextCompat.getColor(this, R.color.black))
                binding.ivColorStickers.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.purple_status
                    )
                )
                binding.ivOpacityStickers.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.black
                    )
                )
                binding.tvAddStickers.setTextColor(ContextCompat.getColor(this, R.color.black))
                binding.tvColorStickers.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.purple_status
                    )
                )
                binding.tvOpacityStickers.setTextColor(ContextCompat.getColor(this, R.color.black))
                binding.lvColorsCode.visibility = View.VISIBLE
                binding.lvOpacitySeekBarContainer.visibility = View.GONE
            }

            EnumClass.OPACITY -> {
                if (binding.lvOpacitySeekBarContainer.visibility == View.VISIBLE) {
                    binding.lvOpacitySeekBarContainer.visibility = View.GONE
                    binding.ivOpacityStickers.setColorFilter(
                        ContextCompat.getColor(
                            this,
                            R.color.black
                        )
                    )
                    binding.tvOpacityStickers.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.black
                        )
                    )
                    return
                }

                binding.ivAddStickers.setColorFilter(ContextCompat.getColor(this, R.color.black))
                binding.ivColorStickers.setColorFilter(ContextCompat.getColor(this, R.color.black))
                binding.ivOpacityStickers.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.purple_status
                    )
                )
                binding.tvAddStickers.setTextColor(ContextCompat.getColor(this, R.color.black))
                binding.tvColorStickers.setTextColor(ContextCompat.getColor(this, R.color.black))
                binding.tvOpacityStickers.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.purple_status
                    )
                )

                if (binding.lvRoot.childCount <= 2) {
                    Toast.makeText(applicationContext, "Please, Add Sticker", Toast.LENGTH_SHORT)
                        .show()
                    return
                }

                val imageSticker5 = sticker
                val opacity = imageSticker5!!.opacity
                binding.seekbar.max = 255
                binding.seekbar.progress = (opacity * 255.0f).toInt()
                binding.lvOpacitySeekBarContainer.visibility = View.VISIBLE
                binding.lvColorsCode.visibility = View.GONE
            }

            EnumClass.BACKGROUND_COLOR -> {
                if (binding.lvColorsCodeContainer.visibility == View.VISIBLE) {
                    binding.lvColorsCodeContainer.visibility = View.GONE
                    binding.ivColorBackground.setColorFilter(
                        ContextCompat.getColor(
                            this,
                            R.color.black
                        )
                    )
                    binding.tvColorBackground.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.black
                        )
                    )
                    return
                }
                binding.ivAddImage.setColorFilter(ContextCompat.getColor(this, R.color.black))
                binding.ivColorBackground.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.purple_status
                    )
                )
                binding.ivNone.setColorFilter(ContextCompat.getColor(this, R.color.black))
                binding.tvAddImage.setTextColor(ContextCompat.getColor(this, R.color.black))
                binding.tvColorBackground.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.purple_status
                    )
                )
                binding.tvNone.setTextColor(ContextCompat.getColor(this, R.color.black))
                binding.lvColorsCodeContainer.visibility = View.VISIBLE
            }

            EnumClass.ADD_STICKERS -> {
                binding.ivAddStickers.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.purple_status
                    )
                )
                binding.ivColorStickers.setColorFilter(ContextCompat.getColor(this, R.color.black))
                binding.ivOpacityStickers.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.black
                    )
                )
                binding.tvAddStickers.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.purple_status
                    )
                )
                binding.tvColorStickers.setTextColor(ContextCompat.getColor(this, R.color.black))
                binding.tvOpacityStickers.setTextColor(ContextCompat.getColor(this, R.color.black))
//                AddStickerBottomSheet( this).apply {
//                    show(supportFragmentManager, tag)
//                }
            }

            EnumClass.BLACK -> {
                if (binding.lvRoot.childCount <= 2) {
                    Toast.makeText(applicationContext, "Please, Add Sticker", Toast.LENGTH_SHORT)
                        .show()
                    return
                }
                binding.ivBlack.visibility = View.VISIBLE
                binding.ivRed.visibility = View.GONE
                binding.ivBlue.visibility = View.GONE
                binding.ivGreen.visibility = View.GONE
                binding.ivYellow.visibility = View.GONE
                binding.ivPurple.visibility = View.GONE
                binding.ivGray.visibility = View.GONE
                sticker!!.applyColorFilter(hairColors[0])
            }

            EnumClass.RED -> {
                if (binding.lvRoot.childCount <= 2) {
                    Toast.makeText(applicationContext, "Please, Add Sticker", Toast.LENGTH_SHORT)
                        .show()
                    return
                }
                binding.ivBlack.visibility = View.GONE
                binding.ivRed.visibility = View.VISIBLE
                binding.ivBlue.visibility = View.GONE
                binding.ivGreen.visibility = View.GONE
                binding.ivYellow.visibility = View.GONE
                binding.ivPurple.visibility = View.GONE
                binding.ivGray.visibility = View.GONE
                sticker!!.applyColorFilter(hairColors[1])
            }

            EnumClass.BLUE -> {
                if (binding.lvRoot.childCount <= 2) {
                    Toast.makeText(applicationContext, "Please, Add Sticker", Toast.LENGTH_SHORT)
                        .show()
                    return
                }
                binding.ivBlack.visibility = View.GONE
                binding.ivRed.visibility = View.GONE
                binding.ivBlue.visibility = View.VISIBLE
                binding.ivGreen.visibility = View.GONE
                binding.ivYellow.visibility = View.GONE
                binding.ivPurple.visibility = View.GONE
                binding.ivGray.visibility = View.GONE
                sticker!!.applyColorFilter(hairColors[2])
            }

            EnumClass.GREEN -> {
                if (binding.lvRoot.childCount <= 2) {
                    Toast.makeText(applicationContext, "Please, Add Sticker", Toast.LENGTH_SHORT)
                        .show()
                    return
                }
                binding.ivBlack.visibility = View.GONE
                binding.ivRed.visibility = View.GONE
                binding.ivBlue.visibility = View.GONE
                binding.ivGreen.visibility = View.VISIBLE
                binding.ivYellow.visibility = View.GONE
                binding.ivPurple.visibility = View.GONE
                binding.ivGray.visibility = View.GONE
                sticker!!.applyColorFilter(hairColors[3])
            }

            EnumClass.YELLOW -> {
                if (binding.lvRoot.childCount <= 2) {
                    Toast.makeText(applicationContext, "Please, Add Sticker", Toast.LENGTH_SHORT).show()
                    return
                }
                binding.ivBlack.visibility = View.GONE
                binding.ivRed.visibility = View.GONE
                binding.ivBlue.visibility = View.GONE
                binding.ivGreen.visibility = View.GONE
                binding.ivYellow.visibility = View.VISIBLE
                binding.ivPurple.visibility = View.GONE
                binding.ivGray.visibility = View.GONE
                sticker!!.applyColorFilter(hairColors[4])
            }

            EnumClass.PURPLE -> {
                if (binding.lvRoot.childCount <= 2) {
                    Toast.makeText(applicationContext, "Please, Add Sticker", Toast.LENGTH_SHORT)
                        .show()
                    return
                }
                binding.ivBlack.visibility = View.GONE
                binding.ivRed.visibility = View.GONE
                binding.ivBlue.visibility = View.GONE
                binding.ivGreen.visibility = View.GONE
                binding.ivYellow.visibility = View.GONE
                binding.ivPurple.visibility = View.VISIBLE
                binding.ivGray.visibility = View.GONE
                sticker!!.applyColorFilter(hairColors[5])
            }

            EnumClass.GRAY -> {
                if (binding.lvRoot.childCount <= 2) {
                    Toast.makeText(applicationContext, "Please, Add Sticker", Toast.LENGTH_SHORT)
                        .show()
                    return
                }
                binding.ivBlack.visibility = View.GONE
                binding.ivRed.visibility = View.GONE
                binding.ivBlue.visibility = View.GONE
                binding.ivGreen.visibility = View.GONE
                binding.ivYellow.visibility = View.GONE
                binding.ivPurple.visibility = View.GONE
                binding.ivGray.visibility = View.VISIBLE
                sticker!!.applyColorFilter(hairColors[6])
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
                binding.ivAddImage.setColorFilter(ContextCompat.getColor(this, R.color.black))
                binding.ivColorBackground.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.black
                    )
                )
                binding.ivNone.setColorFilter(ContextCompat.getColor(this, R.color.black))
                binding.tvAddImage.setTextColor(ContextCompat.getColor(this, R.color.black))
                binding.tvColorBackground.setTextColor(ContextCompat.getColor(this, R.color.black))
                binding.tvNone.setTextColor(ContextCompat.getColor(this, R.color.black))
                binding.ivBackGround.colorFilter = null
                val filePath = ""
                Glide.with(this)
                    .load(filePath)
                    .into(binding.ivBackGround)
                binding.lvRoot.setBackgroundColor(resources.getColor(R.color.white))
            }

            EnumClass.BACKGROUND_IMAGES -> {
                binding.ivAddImage.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.purple_status
                    )
                )
                binding.ivColorBackground.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.black
                    )
                )
                binding.ivNone.setColorFilter(ContextCompat.getColor(this, R.color.black))
                binding.tvAddImage.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.purple_status
                    )
                )
                binding.tvColorBackground.setTextColor(ContextCompat.getColor(this, R.color.black))
                binding.tvNone.setTextColor(ContextCompat.getColor(this, R.color.black))
                initLists()
                ShowBackgroundsBottomSheet(
                    this,
                    Extension.objSuitOptions,
                    this
                ).apply {
                    show(supportFragmentManager, tag)
                }
            }

            EnumClass.DONE -> {
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
                    val intent =
                        Intent(this@StickerActivity, ImageAdsSavedActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

            else -> {}
        }
    }

    override fun onBackgroundBottomSheetButtonClicks(path: String) {
        binding.ivBackGround.colorFilter = null
        val filePath = "file:///android_asset/$path"
        Glide.with(applicationContext)
            .load(filePath)
            .into(binding.ivBackGround)

        binding.ivBackGround.invalidate()
    }

}