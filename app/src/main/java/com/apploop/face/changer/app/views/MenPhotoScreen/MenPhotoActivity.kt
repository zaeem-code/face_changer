package com.apploop.face.changer.app.views.MenPhotoScreen

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.apploop.face.changer.app.R
import com.apploop.face.changer.app.bottomsheets.AddStickerBottomSheet
import com.apploop.face.changer.app.callBacks.AddStickerBottomSheetViewModelInterface
import com.apploop.face.changer.app.callBacks.StickerViewModelInterface
import com.apploop.face.changer.app.databinding.ActivityMenPhotoBinding
import com.apploop.face.changer.app.helpers.EnumClass
import com.apploop.face.changer.app.helpers.onTouch.MultiTouchListener
import com.apploop.face.changer.app.helpers.stickerviewclass.StickerImageView
import com.apploop.face.changer.app.helpers.stickerviewclass.StickerView
import com.apploop.face.changer.app.manager.AdsManager
import com.apploop.face.changer.app.utils.Extension
import com.apploop.face.changer.app.utils.Extension.getBitmapFromView
import com.apploop.face.changer.app.utils.Extension.loadBitmap
import com.apploop.face.changer.app.utils.Extension.statusBarColor
import com.apploop.face.changer.app.utils.UtilsCons
import com.apploop.face.changer.app.viewModels.StickerViewModel
import com.apploop.face.changer.app.views.saved.ImageAdsSavedActivity
import com.apploop.face.changer.app.views.stickers.StickerActivity
import java.util.Random

class MenPhotoActivity : AppCompatActivity(), StickerViewModelInterface,
    AddStickerBottomSheetViewModelInterface {

    lateinit var binding: ActivityMenPhotoBinding

    private lateinit var stickerViewModel: StickerViewModel
    private var sticker: StickerImageView? = null
    private val multiTouchListener = MultiTouchListener()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_men_photo)
        statusBarColor(R.color.background)
        init()
        initClicks()
    }

    private fun init() {
        binding.shimmerFrameLayout.startShimmer()
        AdsManager.instance!!.showAdMobBanner(this, binding.frameLayout) {
            if (it) {
                binding.shimmerFrameLayout.visibility = View.INVISIBLE
            } else {
                binding.shimmerFrameLayout.visibility = View.GONE
                binding.frameLayout.visibility = View.GONE
            }
        }

        stickerViewModel = StickerViewModel(this)
        binding.stickerViewModel = stickerViewModel
        if (UtilsCons.originalBitmap != null) {
            loadBitmap(binding.ivSuit, UtilsCons.originalBitmap)
        }
    }

    private fun initClicks() {
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
                        val view: View =
                            binding.lvRoot.findViewById<View>(Extension.stickerViewId[i])
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

    override fun onStickerButtonClicks(type: EnumClass) {
        when (type) {

            EnumClass.BACK -> {
                onBackPressed()
            }

            EnumClass.ADD_STICKERS -> {
                binding.ivAddStickers.setColorFilter(ContextCompat.getColor(this, R.color.purple_status))
                binding.ivColorStickers.setColorFilter(ContextCompat.getColor(this, R.color.light_grey))
                binding.ivOpacityStickers.setColorFilter(ContextCompat.getColor(this, R.color.light_grey))
                binding.tvAddStickers.setTextColor(ContextCompat.getColor(this, R.color.purple_status))
                binding.tvColorStickers.setTextColor(ContextCompat.getColor(this, R.color.light_grey))
                binding.tvOpacityStickers.setTextColor(ContextCompat.getColor(this, R.color.light_grey))
                AddStickerBottomSheet( this@MenPhotoActivity,this).apply {
                    show(supportFragmentManager, tag)
                }
            }

            EnumClass.BLACK -> {

                removeBorder()
                if (binding.lvRoot.childCount <= 2) {
                    Toast.makeText(applicationContext, "Please, Add Sticker", Toast.LENGTH_SHORT).show()
                    return
                }
                binding.ivBlack.visibility = View.VISIBLE
                binding.ivRed.visibility = View.GONE
                binding.ivBlue.visibility = View.GONE
                binding.ivGreen.visibility = View.GONE
                binding.ivYellow.visibility = View.GONE
                binding.ivPurple.visibility = View.GONE
                binding.ivGray.visibility = View.GONE
                sticker!!.applyColorFilter(Extension.hairColors[0])
            }

            EnumClass.RED -> {
                removeBorder()
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
                sticker!!.applyColorFilter(Extension.hairColors[1])
            }

            EnumClass.BLUE -> {
                removeBorder()
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
                sticker!!.applyColorFilter(Extension.hairColors[2])
            }

            EnumClass.GREEN -> {
                removeBorder()
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
                sticker!!.applyColorFilter(Extension.hairColors[3])
            }

            EnumClass.YELLOW -> {
                removeBorder()
                if (binding.lvRoot.childCount <= 2) {
                    Toast.makeText(applicationContext, "Please, Add Sticker", Toast.LENGTH_SHORT)
                        .show()
                    return
                }
                binding.ivBlack.visibility = View.GONE
                binding.ivRed.visibility = View.GONE
                binding.ivBlue.visibility = View.GONE
                binding.ivGreen.visibility = View.GONE
                binding.ivYellow.visibility = View.VISIBLE
                binding.ivPurple.visibility = View.GONE
                binding.ivGray.visibility = View.GONE
                sticker!!.applyColorFilter(Extension.hairColors[4])
            }

            EnumClass.PURPLE -> {
                removeBorder()
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
                sticker!!.applyColorFilter(Extension.hairColors[5])
            }

            EnumClass.GRAY -> {
                removeBorder()
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
                sticker!!.applyColorFilter(Extension.hairColors[6])
            }

            EnumClass.STICKER_COLOR -> {
                removeBorder()
                if (binding.lvColorsCode.visibility == View.VISIBLE) {
                    binding.lvColorsCode.visibility = View.GONE
                    binding.ivColorStickers.setColorFilter(ContextCompat.getColor(this, R.color.light_grey))
                    binding.tvColorStickers.setTextColor(ContextCompat.getColor(this, R.color.light_grey))
                    return
                }
                binding.ivAddStickers.setColorFilter(ContextCompat.getColor(this, R.color.light_grey))
                binding.ivColorStickers.setColorFilter(ContextCompat.getColor(this, R.color.purple_status))
                binding.ivOpacityStickers.setColorFilter(ContextCompat.getColor(this, R.color.light_grey))
                binding.tvAddStickers.setTextColor(ContextCompat.getColor(this, R.color.light_grey))
                binding.tvColorStickers.setTextColor(ContextCompat.getColor(this, R.color.purple_status))
                binding.tvOpacityStickers.setTextColor(ContextCompat.getColor(this, R.color.light_grey))
                binding.lvColorsCode.visibility = View.VISIBLE
                binding.lvOpacitySeekBarContainer.visibility = View.GONE
            }

            EnumClass.OPACITY -> {
                removeBorder()
                if (binding.lvOpacitySeekBarContainer.visibility == View.VISIBLE) {
                    binding.lvOpacitySeekBarContainer.visibility = View.GONE
                    binding.ivOpacityStickers.setColorFilter(ContextCompat.getColor(this, R.color.light_grey))
                    binding.tvOpacityStickers.setTextColor(ContextCompat.getColor(this, R.color.light_grey))
                    return
                }

                binding.ivAddStickers.setColorFilter(ContextCompat.getColor(this, R.color.light_grey))
                binding.ivColorStickers.setColorFilter(ContextCompat.getColor(this, R.color.light_grey))
                binding.ivOpacityStickers.setColorFilter(ContextCompat.getColor(this, R.color.purple_status))
                binding.tvAddStickers.setTextColor(ContextCompat.getColor(this, R.color.light_grey))
                binding.tvColorStickers.setTextColor(ContextCompat.getColor(this, R.color.light_grey))
                binding.tvOpacityStickers.setTextColor(ContextCompat.getColor(this, R.color.purple_status))

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

            EnumClass.DONE -> {
                removeBorder()
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
                    val intent =
                        Intent(this@MenPhotoActivity, ImageAdsSavedActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}