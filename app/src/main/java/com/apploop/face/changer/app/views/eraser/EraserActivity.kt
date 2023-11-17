package com.apploop.face.changer.app.views.eraser

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.apploop.face.changer.app.R
import com.apploop.face.changer.app.callBacks.EraseViewModelInterface
import com.apploop.face.changer.app.databinding.ActivityEraserBinding
import com.apploop.face.changer.app.helpers.EnumClass
import com.apploop.face.changer.app.helpers.eraser.DrawingView
import com.apploop.face.changer.app.helpers.eraser.ImageUtilsEraser
import com.apploop.face.changer.app.helpers.onTouch.MultiTouchListener
import com.apploop.face.changer.app.utils.Extension.bitmapSuit
import com.apploop.face.changer.app.utils.Extension.getBitmapFromView
import com.apploop.face.changer.app.utils.Extension.statusBarColor
import com.apploop.face.changer.app.utils.UtilsCons
import com.apploop.face.changer.app.viewModels.EraseViewModel
import com.apploop.face.changer.app.views.handCrop.HandCropActivity
import com.apploop.face.changer.app.views.stickers.StickerActivity

class EraserActivity : AppCompatActivity(), EraseViewModelInterface {

    private lateinit var binding: ActivityEraserBinding
    private lateinit var eraseViewModel: EraseViewModel
    var flag = 0
    private var dv: com.apploop.face.changer.app.helpers.eraser.DrawingView? = null
    private var dv1: com.apploop.face.changer.app.helpers.eraser.DrawingView? = null
    var ringProgressDialog: ProgressDialog? = null

    interface OnEraseTouch {
        fun OnTouchErase(action: Int)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_eraser)
        statusBarColor(R.color.background)
        init()
        initClicks()


    }

    private fun init() {
        binding.shimmerFrameLayout.startShimmer()
//        AdsManager.Companion.instance!!.showNativeAd(
//            binding.frameLayout,
//            binding.frameLayout,
//            layoutInflater,
//            R.layout.ad_media
//        )
//        {
//            if (it) {
//                binding.shimmerFrameLayout.visibility = View.INVISIBLE
//            } else {
//                binding.shimmerFrameLayout.visibility = View.GONE
//                binding.frameLayout.visibility = View.GONE
//            }
//        }




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



        eraseViewModel = EraseViewModel(this)
        binding.eraseViewModel = eraseViewModel
        try {
            if (com.apploop.face.changer.app.utils.UtilsCons.originalBitmap != null) {
                binding.ivCropped.post(Runnable { setCropImage(com.apploop.face.changer.app.utils.UtilsCons.originalBitmap) })
            }
        } catch (e: NullPointerException) {
            e.localizedMessage
        }
        binding.seekOffSet.progress = 400
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initClicks() {
        binding.ivCropped.setOnTouchListener(com.apploop.face.changer.app.helpers.onTouch.MultiTouchListener())

        binding.seekOffSet.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                try {
                    dv?.offset = progress - 300
                    dv?.invalidate()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        binding.seekSize.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                try {
                    dv?.setRadius(progress + 20)
                    dv?.invalidate()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        binding.seekthreshold.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                try {
                    dv?.offset = progress - 300
                    dv?.invalidate()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

    }

    private fun setCropImage(bit: Bitmap) {
        var bitmap: Bitmap? = bit
        dv = com.apploop.face.changer.app.helpers.eraser.DrawingView(this)
        dv1 = com.apploop.face.changer.app.helpers.eraser.DrawingView(this)
        bitmap = com.apploop.face.changer.app.helpers.eraser.ImageUtilsEraser.resizeBitmap(
            bitmap,
            binding.ivCropped.width,
            binding.ivCropped.height
        )
        dv?.setImageBitmap(bitmap)
        dv?.enableTouchClear(false)
        dv?.setMODE(0)
        dv?.invalidate()
        binding.seekOffSet.progress = 400
        binding.seekSize.progress = 18
        binding.seekthreshold.progress = 400
        binding.ivCropped.removeAllViews()
        binding.ivCropped.scaleX = 1.0f
        binding.ivCropped.scaleY = 1.0f
        binding.ivCropped.addView(dv1)
        binding.ivCropped.addView(dv)
        dv1?.setMODE(5)
        dv1?.enableTouchClear(false)
        dv?.invalidate()
        dv1?.visibility = View.GONE
        dv?.setOnEraseTouch(object : OnEraseTouch {
            override fun OnTouchErase(action: Int) {
                if (action == MotionEvent.ACTION_DOWN) {
                    val bmpDrawable = BitmapDrawable(resources, bitmapSuit)
                    bmpDrawable.alpha = 150
//                    binding.ivSuit.setImageDrawable(bmpDrawable)
                } else if (action == MotionEvent.ACTION_UP) {
//                    Glide.with(this@EraserActivity)
//                        .load(bitmapSuit)
//                        .into(binding.ivSuit)
                }
            }
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onEraseButtonClicks(type: EnumClass) {
        when (type) {
            EnumClass.BACK -> {
                onBackPressed()
            }
            EnumClass.ERASE -> {
                binding.ivErase.setColorFilter(ContextCompat.getColor(this, R.color.purple_status))
                binding.ivRestore.setColorFilter(ContextCompat.getColor(this, R.color.black))
                binding.ivAutoErase.setColorFilter(ContextCompat.getColor(this, R.color.black))
                binding.ivZoom.setColorFilter(ContextCompat.getColor(this, R.color.black))
                binding.tvErase.setTextColor(ContextCompat.getColor(this, R.color.purple_status))
                binding.tvRestore.setTextColor(ContextCompat.getColor(this, R.color.black))
                binding.tvAutoErase.setTextColor(ContextCompat.getColor(this, R.color.black))
                binding.tvZoom.setTextColor(ContextCompat.getColor(this, R.color.black))
                flag = 1
                binding.lytWidthContainer.visibility = View.GONE
                binding.lytThresholdcontainer.visibility = View.GONE
                binding.lytWidthContainer.visibility = View.VISIBLE
                dv1?.visibility = View.GONE
                try {
                    binding.seekOffSet.progress = dv?.offset!! + 300
                }
                catch (e:Exception)
                {
                    e.printStackTrace()
                }
                dv?.enableTouchClear(true)
                binding.ivCropped.setOnTouchListener(null)
                dv?.setMODE(1)
                dv?.invalidate()
            }
            EnumClass.RESTORE -> {
                binding.ivErase.setColorFilter(ContextCompat.getColor(this, R.color.black))
                binding.ivRestore.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.purple_status
                    )
                )
                binding.ivAutoErase.setColorFilter(ContextCompat.getColor(this, R.color.black))
                binding.ivZoom.setColorFilter(ContextCompat.getColor(this, R.color.black))
                binding.tvErase.setTextColor(ContextCompat.getColor(this, R.color.black))
                binding.tvRestore.setTextColor(ContextCompat.getColor(this, R.color.purple_status))
                binding.tvAutoErase.setTextColor(ContextCompat.getColor(this, R.color.black))
                binding.tvZoom.setTextColor(ContextCompat.getColor(this, R.color.black))
                flag = 2
                binding.lytWidthContainer.visibility = View.GONE
                binding.lytThresholdcontainer.visibility = View.GONE
                binding.lytWidthContainer.visibility = View.VISIBLE
                dv1?.visibility = View.VISIBLE
                binding.seekOffSet.progress = dv?.offset!! + 170
                dv!!.enableTouchClear(true)
                binding.ivCropped.setOnTouchListener(null)
                dv!!.setMODE(4)
                dv!!.invalidate()
            }
            EnumClass.AUTO_ERASE -> {
                binding.ivErase.setColorFilter(ContextCompat.getColor(this, R.color.black))
                binding.ivRestore.setColorFilter(ContextCompat.getColor(this, R.color.black))
                binding.ivAutoErase.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.purple_status
                    )
                )
                binding.ivZoom.setColorFilter(ContextCompat.getColor(this, R.color.black))
                binding.tvErase.setTextColor(ContextCompat.getColor(this, R.color.black))
                binding.tvRestore.setTextColor(ContextCompat.getColor(this, R.color.black))
                binding.tvAutoErase.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.purple_status
                    )
                )
                binding.tvZoom.setTextColor(ContextCompat.getColor(this, R.color.black))
                binding.lytWidthContainer.visibility = View.GONE
                binding.lytThresholdcontainer.visibility = View.VISIBLE
                dv1?.visibility = View.GONE
                binding.seekthreshold.progress = dv?.offset!! + 150
                dv?.enableTouchClear(true)
                binding.ivCropped.setOnTouchListener(null)
                dv?.setMODE(2)
                dv?.invalidate()
            }
            EnumClass.ZOOM -> {
                binding.ivErase.setColorFilter(ContextCompat.getColor(this, R.color.black))
                binding.ivRestore.setColorFilter(ContextCompat.getColor(this, R.color.black))
                binding.ivAutoErase.setColorFilter(ContextCompat.getColor(this, R.color.black))
                binding.ivZoom.setColorFilter(ContextCompat.getColor(this, R.color.purple_status))
                binding.tvErase.setTextColor(ContextCompat.getColor(this, R.color.black))
                binding.tvRestore.setTextColor(ContextCompat.getColor(this, R.color.black))
                binding.tvAutoErase.setTextColor(ContextCompat.getColor(this, R.color.black))
                binding.tvZoom.setTextColor(ContextCompat.getColor(this, R.color.purple_status))
                binding.lytWidthContainer.visibility = View.GONE
                binding.lytThresholdcontainer.visibility = View.GONE
                dv1?.visibility = View.GONE
                dv?.enableTouchClear(false)
                val touchListener =
                    com.apploop.face.changer.app.helpers.onTouch.MultiTouchListener()
                binding.ivCropped.setOnTouchListener(touchListener)
                dv?.setMODE(0)
                dv?.invalidate()
            }
            EnumClass.DONE -> {

                binding.lytWidthContainer.visibility = View.GONE
                binding.lytThresholdcontainer.visibility = View.GONE
                dv?.setMODE(0)


                if (intent.hasExtra("select"))
                {

                    if(intent.getStringExtra("select").equals("manual_crop"))
                    {
                        try {

                            com.apploop.face.changer.app.utils.UtilsCons.originalBitmap = getBitmapFromView(binding.ivCropped)
                            val intent = Intent(this@EraserActivity, com.apploop.face.changer.app.views.handCrop.HandCropActivity::class.java)
                            startActivity(intent)
                            finish()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                }
                else
                {
                    try {

                        com.apploop.face.changer.app.utils.UtilsCons.originalBitmap = getBitmapFromView(binding.ivCropped)
                        val intent = Intent(this@EraserActivity, StickerActivity::class.java)
                        startActivity(intent)
                        finish()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }





            }
            EnumClass.REDO -> {
                binding.lvRedo.isEnabled = false
                binding.lvUndo.isEnabled = false
                binding.lytWidthContainer.visibility = View.GONE
                binding.lytThresholdcontainer.visibility = View.GONE
                if (com.apploop.face.changer.app.helpers.eraser.DrawingView.flagredo === true) {
                    return
                }
                ringProgressDialog =
                    ProgressDialog(this@EraserActivity, R.style.MyAlertDialogStyle)
                ringProgressDialog!!.setMessage("Image Processing")
                ringProgressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
                ringProgressDialog!!.isIndeterminate = true
                ringProgressDialog!!.show()

                ringProgressDialog!!.setCancelable(false)
                Thread {
                    try {
                        runOnUiThread { dv?.redoChange() }
                        Thread.sleep(500)
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                    ringProgressDialog!!.dismiss()
                    runOnUiThread(Runnable {
                        binding.lvRedo.isEnabled = true
                        binding.lvUndo.isEnabled = true
                    })
                }.start()
            }
            EnumClass.UNDO -> {
                binding.lvRedo.isEnabled = false
                binding.lvUndo.isEnabled = false
                binding.lytWidthContainer.visibility = View.GONE
                binding.lytThresholdcontainer.visibility = View.GONE
                if (com.apploop.face.changer.app.helpers.eraser.DrawingView.flag === true) {
                    return
                }
                ringProgressDialog =
                    ProgressDialog(this@EraserActivity, R.style.MyAlertDialogStyle)
                ringProgressDialog!!.setMessage("Image Processing")
                ringProgressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
                ringProgressDialog!!.isIndeterminate = true
                ringProgressDialog!!.show()

                ringProgressDialog!!.setCancelable(false)
                Thread {
                    try {
                        runOnUiThread { dv!!.undoChange() }
                        Thread.sleep(500)
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                    ringProgressDialog!!.dismiss()
                    runOnUiThread(Runnable {
                        binding.lvRedo.isEnabled = true
                        binding.lvUndo.isEnabled = true
                    })
                }.start()
            }
            else -> {}
        }
    }
}