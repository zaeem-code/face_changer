package com.apploop.face.changer.app.views.faceChangeScreen

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.apploop.face.changer.app.R
import com.apploop.face.changer.app.callBacks.AddStickerBottomSheetViewModelInterface
import com.apploop.face.changer.app.callBacks.BackgroundBottomSheetInterface
import com.apploop.face.changer.app.callBacks.BackgroundSkinToneInterface
import com.apploop.face.changer.app.callBacks.StickerViewModelInterface
import com.apploop.face.changer.app.databinding.ActivityFaceChangeBinding
import com.apploop.face.changer.app.helpers.EnumClass
import com.apploop.face.changer.app.helpers.EnumClass.*
import com.apploop.face.changer.app.helpers.onTouch.MultiTouchListener
import com.apploop.face.changer.app.helpers.stickerviewclass.StickerImageView
import com.apploop.face.changer.app.helpers.stickerviewclass.StickerView
import com.apploop.face.changer.app.utils.Extension
import com.apploop.face.changer.app.utils.Extension.faceChangeList
import com.apploop.face.changer.app.utils.Extension.getBitmapFromView
import com.apploop.face.changer.app.utils.Extension.initLists
import com.apploop.face.changer.app.utils.Extension.loadBitmap
import com.apploop.face.changer.app.utils.Extension.statusBarColor
import com.apploop.face.changer.app.utils.UtilsCons
import com.apploop.face.changer.app.viewModels.StickerViewModel
import com.apploop.face.changer.app.views.adapters.ShowSkinToneAdapter
import com.apploop.face.changer.app.views.adapters.ShowSkinToneColorsAdapter
import com.apploop.face.changer.app.views.saved.ImageAdsSavedActivity
import com.apploop.face.changer.app.views.stickers.StickerActivity
import java.util.Random

class FaceChangeActivity : AppCompatActivity(), StickerViewModelInterface,
    AddStickerBottomSheetViewModelInterface, BackgroundBottomSheetInterface,
    BackgroundSkinToneInterface {

    lateinit var binding: ActivityFaceChangeBinding
    private var sticker: StickerImageView? = null
    private lateinit var stickerViewModel: StickerViewModel
    private val multiTouchListener = MultiTouchListener()

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
        if (UtilsCons.originalBitmap != null) {
            loadBitmap(binding.ivSuit, UtilsCons.originalBitmap)
        }

        UtilsCons.faceType = "YOUNG"

        initLists()
        binding.lvFaceChange.visibility = View.VISIBLE
        binding.lvRec.visibility = View.GONE
        removeBorder()

        binding.faceChangeRV.layoutManager = GridLayoutManager(this, 3)
        var suitsBottomSheetAdapter = ShowSkinToneAdapter(
            Extension.objFaceChangeDetail,
            this@FaceChangeActivity,
            this@FaceChangeActivity
        )
        binding.faceChangeRV.adapter = suitsBottomSheetAdapter
        suitsBottomSheetAdapter.notifyDataSetChanged()



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
        if (binding.lvRoot.childCount > 2) {
            Toast.makeText(applicationContext, "Remove Previous Face First!", Toast.LENGTH_SHORT)
                .show()
            return
        }

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

            BACK -> {
                onBackPressed()
            }

            OLD_MAN -> {
//                removeBorder()
                UtilsCons.faceType = "OLD"
                binding.tvStickers.setTextColor(ContextCompat.getColor(this, R.color.light_grey))
                binding.tvBackground.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.purple_status
                    )
                )
                binding.tvZoom.setTextColor(ContextCompat.getColor(this, R.color.light_grey))
                binding.lvRec.visibility = View.GONE
                binding.lvFaceChange.visibility = View.GONE
                binding.lvOpacitySeekBarContainer.visibility = View.GONE

//                if (binding.lvStickersContainer.visibility == View.VISIBLE) {
//                    val touchListener = MultiTouchListener()
//                    binding.ivSuit.setOnTouchListener(touchListener)
//                    return
//                }
                binding.lvStickersContainer.visibility = View.VISIBLE
                binding.lvBackgroundContainer.visibility = View.GONE
            }

            STICKERS -> {
//                removeBorder()
                UtilsCons.faceType = "YOUNG"
                binding.tvStickers.setTextColor(ContextCompat.getColor(this, R.color.purple_status))
                binding.tvBackground.setTextColor(ContextCompat.getColor(this, R.color.light_grey))
                binding.tvZoom.setTextColor(ContextCompat.getColor(this, R.color.light_grey))
                binding.lvRec.visibility = View.GONE
                binding.lvFaceChange.visibility = View.GONE
                binding.lvOpacitySeekBarContainer.visibility = View.GONE

//                if (binding.lvStickersContainer.visibility == View.VISIBLE) {
//                    val touchListener = MultiTouchListener()
//                    binding.ivSuit.setOnTouchListener(touchListener)
//                    return
//                }
                binding.lvStickersContainer.visibility = View.VISIBLE
                binding.lvBackgroundContainer.visibility = View.GONE
            }

            KID -> {
//                removeBorder()
                UtilsCons.faceType = "KID"
                binding.tvStickers.setTextColor(ContextCompat.getColor(this, R.color.light_grey))
                binding.tvBackground.setTextColor(ContextCompat.getColor(this, R.color.light_grey))
                binding.tvZoom.setTextColor(ContextCompat.getColor(this, R.color.purple_status))
                binding.lvRec.visibility = View.GONE
                binding.lvFaceChange.visibility = View.GONE
                binding.lvOpacitySeekBarContainer.visibility = View.GONE

//                if (binding.lvStickersContainer.visibility == View.VISIBLE) {
//                    val touchListener = MultiTouchListener()
//                    binding.ivSuit.setOnTouchListener(touchListener)
//                    return
//                }
                binding.lvStickersContainer.visibility = View.VISIBLE
                binding.lvBackgroundContainer.visibility = View.GONE
            }

            SKIN_TONE -> {
                removeBorder()
                if (binding.lvRoot.childCount <= 2) {
                    Toast.makeText(applicationContext, "Please, Add Sticker", Toast.LENGTH_SHORT)
                        .show()
                    return
                }

                if (binding.lvRec.visibility == View.VISIBLE) {
                    binding.lvRec.visibility = View.GONE
                    binding.ivColorStickers.setColorFilter(
                        ContextCompat.getColor(
                            this,
                            R.color.light_grey
                        )
                    )
                    binding.tvColorStickers.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.light_grey
                        )
                    )
                    return
                }

                binding.ivAddStickers.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.light_grey
                    )
                )
                binding.ivColorStickers.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.purple_status
                    )
                )
                binding.ivOpacityStickers.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.light_grey
                    )
                )
                binding.tvAddStickers.setTextColor(ContextCompat.getColor(this, R.color.light_grey))
                binding.tvColorStickers.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.purple_status
                    )
                )
                binding.tvOpacityStickers.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.light_grey
                    )
                )

                binding.lvOpacitySeekBarContainer.visibility = View.GONE
                binding.lvFaceChange.visibility = View.GONE
                binding.lvRec.visibility = View.VISIBLE

                binding.skinToneRV.layoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                var suitsBottomSheetAdapter = ShowSkinToneColorsAdapter(
                    faceChangeList(),
                    this@FaceChangeActivity,
                    Extension.imageFilePath,
                    this@FaceChangeActivity
                )
                binding.skinToneRV.adapter = suitsBottomSheetAdapter
                suitsBottomSheetAdapter.notifyDataSetChanged()
            }

            ADD_STICKERS -> {
                removeBorder()
                binding.ivAddStickers.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.purple_status
                    )
                )
                binding.ivColorStickers.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.light_grey
                    )
                )
                binding.ivOpacityStickers.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.light_grey
                    )
                )
                binding.tvAddStickers.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.purple_status
                    )
                )
                binding.tvColorStickers.setTextColor(
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
                binding.lvOpacitySeekBarContainer.visibility = View.GONE
                binding.lvRec.visibility = View.GONE

                initLists()
                binding.lvFaceChange.visibility = View.VISIBLE

                binding.faceChangeRV.layoutManager = GridLayoutManager(this, 3)
                if (UtilsCons.faceType.contains("OLD")) {

                    var suitsBottomSheetAdapter = ShowSkinToneAdapter(
                        Extension.oldFaceChangeDetail,
                        this@FaceChangeActivity,
                        this@FaceChangeActivity
                    )
                    binding.faceChangeRV.adapter = suitsBottomSheetAdapter
                    suitsBottomSheetAdapter.notifyDataSetChanged()
                } else if (UtilsCons.faceType.contains("YOUNG")) {

                    var suitsBottomSheetAdapter = ShowSkinToneAdapter(
                        Extension.objFaceChangeDetail,
                        this@FaceChangeActivity,
                        this@FaceChangeActivity
                    )
                    binding.faceChangeRV.adapter = suitsBottomSheetAdapter
                    suitsBottomSheetAdapter.notifyDataSetChanged()
                } else {

                    var suitsBottomSheetAdapter = ShowSkinToneAdapter(
                        Extension.kidFaceChangeDetail,
                        this@FaceChangeActivity,
                        this@FaceChangeActivity
                    )
                    binding.faceChangeRV.adapter = suitsBottomSheetAdapter
                    suitsBottomSheetAdapter.notifyDataSetChanged()
                }
            }

            OPACITY -> {
                removeBorder()
                if (binding.lvRoot.childCount <= 2) {
                    Toast.makeText(applicationContext, "Please, Add Sticker", Toast.LENGTH_SHORT)
                        .show()
                    return
                }

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

                binding.lvOpacitySeekBarContainer.visibility = View.GONE
                binding.lvRec.visibility = View.GONE
                binding.lvFaceChange.visibility = View.GONE

                binding.ivAddStickers.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.light_grey
                    )
                )
                binding.ivColorStickers.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.light_grey
                    )
                )
                binding.ivOpacityStickers.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.purple_status
                    )
                )
                binding.tvAddStickers.setTextColor(ContextCompat.getColor(this, R.color.light_grey))
                binding.tvColorStickers.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.light_grey
                    )
                )
                binding.tvOpacityStickers.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.purple_status
                    )
                )

                val imageSticker5 = sticker
                val opacity = imageSticker5!!.opacity
                binding.seekbar.max = 255
                binding.seekbar.progress = (opacity * 255.0f).toInt()
                binding.lvOpacitySeekBarContainer.visibility = View.VISIBLE
            }

            DONE -> {


                removeBorder()
                Handler(Looper.getMainLooper()).postDelayed({
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
                            Intent(this@FaceChangeActivity, ImageAdsSavedActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }, 2000)

            }

            CAMERA -> TODO()
            GALLERY -> TODO()
            MY_CREATION -> TODO()
            FLIP_SUIT -> TODO()
            FLIP_IMAGE -> TODO()
            BLAZER -> TODO()
            SUIT -> TODO()
            FORMAL -> TODO()
            JACKET -> TODO()
            Police -> TODO()
            EDIT -> TODO()
            ERASE -> TODO()
            RESTORE -> TODO()
            AUTO_ERASE -> TODO()
            ZOOM -> TODO()
            UNDO -> TODO()
            REDO -> TODO()
            BACKGROUND -> TODO()
            STICKER_COLOR -> TODO()
            BACKGROUND_COLOR -> TODO()
            BEARD -> TODO()
            GOGGLES -> TODO()
            HAIR -> TODO()
            HAT -> TODO()
            MUSTACHE -> TODO()
            TIE -> TODO()
            BLACK -> TODO()
            RED -> TODO()
            BLUE -> TODO()
            GREEN -> TODO()
            YELLOW -> TODO()
            PURPLE -> TODO()
            GRAY -> TODO()
            BLACK1 -> TODO()
            RED1 -> TODO()
            BLUE1 -> TODO()
            GREEN1 -> TODO()
            YELLOW1 -> TODO()
            PURPLE1 -> TODO()
            GRAY1 -> TODO()
            BACKGROUND_IMAGES -> TODO()
            NONE -> TODO()
            PHOTO_SUIT -> TODO()
            PHOTO_REMOVE_BG -> TODO()
            PHOTO -> TODO()
            PHOTO_FACE -> TODO()
            PHOTO_MEN -> TODO()
            MOVE_SUIT -> TODO()
            KURTA -> TODO()
            BODY_BUILDER -> TODO()
            T_SHIRT -> TODO()
            CARD -> TODO()
            CHRISTMAS -> TODO()
            IPL -> TODO()
            AFRICA -> TODO()
            PSL -> TODO()
            BPL -> TODO()
            BBL -> TODO()
            WT -> TODO()
            FIFA -> TODO()
            SUPER -> TODO()
        }
    }

    override fun onBackgroundSkinToneInterfaceClicks(color: String) {
        sticker!!.applyColorFilter(color)
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