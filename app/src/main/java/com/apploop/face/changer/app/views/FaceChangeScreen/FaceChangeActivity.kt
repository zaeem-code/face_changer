package com.apploop.face.changer.app.views.FaceChangeScreen

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.apploop.face.changer.app.R
import com.apploop.face.changer.app.bottomsheets.ShowStickersBottomSheet
import com.apploop.face.changer.app.callBacks.AddStickerBottomSheetViewModelInterface
import com.apploop.face.changer.app.callBacks.BackgroundBottomSheetInterface
import com.apploop.face.changer.app.callBacks.StickerViewModelInterface
import com.apploop.face.changer.app.databinding.ActivityFaceChangeBinding
import com.apploop.face.changer.app.helpers.EnumClass
import com.apploop.face.changer.app.helpers.onTouch.MultiTouchListener
import com.apploop.face.changer.app.helpers.stickerviewclass.StickerImageView
import com.apploop.face.changer.app.helpers.stickerviewclass.StickerView
import com.apploop.face.changer.app.utils.Extension
import com.apploop.face.changer.app.utils.Extension.getBitmapFromView
import com.apploop.face.changer.app.utils.Extension.loadBitmap
import com.apploop.face.changer.app.utils.Extension.statusBarColor
import com.apploop.face.changer.app.utils.UtilsCons
import com.apploop.face.changer.app.viewModels.StickerViewModel
import com.apploop.face.changer.app.views.saved.ImageAdsSavedActivity
import java.util.Random

class FaceChangeActivity : AppCompatActivity(), StickerViewModelInterface
    ,AddStickerBottomSheetViewModelInterface, BackgroundBottomSheetInterface {

    lateinit var binding: ActivityFaceChangeBinding
    private var sticker: com.apploop.face.changer.app.helpers.stickerviewclass.StickerImageView? = null
    private lateinit var stickerViewModel: StickerViewModel
    private val multiTouchListener =
        com.apploop.face.changer.app.helpers.onTouch.MultiTouchListener()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaceChangeBinding.inflate(layoutInflater)
        statusBarColor(R.color.background)
        setContentView(binding.root)

        init()

        binding.seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
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
    }

    private fun init() {
        stickerViewModel = StickerViewModel(this)
        binding.stickerViewModel = stickerViewModel
        if (com.apploop.face.changer.app.utils.UtilsCons.originalBitmap != null) {
            loadBitmap(binding.ivSuit, com.apploop.face.changer.app.utils.UtilsCons.originalBitmap)
        }
    }

    private val onTouchSticker: com.apploop.face.changer.app.helpers.stickerviewclass.StickerView.OnTouchSticker =
        com.apploop.face.changer.app.helpers.stickerviewclass.StickerView.OnTouchSticker { stickerImageView ->
            sticker = stickerImageView as com.apploop.face.changer.app.helpers.stickerviewclass.StickerImageView
            removeBorder()
        }

    private fun removeBorder() {
        try {
            if (Extension.stickerViewId.size > 0) {
                for (i in Extension.stickerViewId.indices) {
                    if (Extension.stickerViewId.isNotEmpty()) {
                        val view: View =
                            binding.lvRoot.findViewById<View>(Extension.stickerViewId[i])
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

    override fun onAddStickerBottomSheetButtonClicks(path: String) {
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

    override fun onStickerButtonClicks(type: EnumClass) {
        when (type) {

            EnumClass.BACK -> {
                onBackPressed()
            }

            EnumClass.STICKERS -> {
                binding.tvStickers.setTextColor(ContextCompat.getColor(this, R.color.purple_status))
                binding.tvBackground.setTextColor(ContextCompat.getColor(this, R.color.light_grey))
                binding.tvZoom.setTextColor(ContextCompat.getColor(this, R.color.light_grey))
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
                binding.tvStickers.setTextColor(ContextCompat.getColor(this, R.color.light_grey))
                binding.tvBackground.setTextColor(ContextCompat.getColor(this, R.color.purple_status))
                binding.tvZoom.setTextColor(ContextCompat.getColor(this, R.color.light_grey))
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

            EnumClass.ZOOM -> {
                binding.tvStickers.setTextColor(ContextCompat.getColor(this, R.color.light_grey))
                binding.tvBackground.setTextColor(ContextCompat.getColor(this, R.color.light_grey))
                binding.tvZoom.setTextColor(ContextCompat.getColor(this, R.color.purple_status))
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

            EnumClass.ADD_STICKERS -> {
                binding.ivAddStickers.setColorFilter(ContextCompat.getColor(this, R.color.purple_status))
                binding.ivColorStickers.setColorFilter(ContextCompat.getColor(this, R.color.light_grey))
                binding.ivOpacityStickers.setColorFilter(ContextCompat.getColor(this, R.color.light_grey))
                binding.tvAddStickers.setTextColor(ContextCompat.getColor(this, R.color.purple_status))
                binding.tvColorStickers.setTextColor(ContextCompat.getColor(this, R.color.light_grey))
                binding.tvOpacityStickers.setTextColor(ContextCompat.getColor(this, R.color.light_grey))

                ShowStickersBottomSheet(
                    this,
                    Extension.objStickerDetailsBeard,
                    this
                ).apply {
                    show(supportFragmentManager, tag)
                }
            }

            EnumClass.OPACITY -> {
                if (binding.lvOpacitySeekBarContainer.visibility == View.VISIBLE) {
                    binding.lvOpacitySeekBarContainer.visibility = View.GONE
                    binding.ivOpacityStickers.setColorFilter(
                        ContextCompat.getColor(
                            this,
                            R.color.light_grey
                        )
                    )
                    binding.tvOpacityStickers.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.light_grey
                        )
                    )
                    return
                }

                binding.ivAddStickers.setColorFilter(ContextCompat.getColor(this, R.color.light_grey))
                binding.ivColorStickers.setColorFilter(ContextCompat.getColor(this, R.color.light_grey))
                binding.ivOpacityStickers.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.purple_status
                    )
                )
                binding.tvAddStickers.setTextColor(ContextCompat.getColor(this, R.color.light_grey))
                binding.tvColorStickers.setTextColor(ContextCompat.getColor(this, R.color.light_grey))
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
            }

            EnumClass.DONE -> {
                getBitmapFromView(binding.lvRoot)?.let {
                    binding.progressBar.visibility = View.VISIBLE
                    window.setFlags(
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    )
                    if (Extension.saveBitmapLast != null) {
                        Extension.saveBitmapLast?.recycle()
                    }
                    Extension.saveBitmapLast = it
                    val intent = Intent(this@FaceChangeActivity, ImageAdsSavedActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
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