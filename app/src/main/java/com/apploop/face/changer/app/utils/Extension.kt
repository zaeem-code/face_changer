package com.apploop.face.changer.app.utils

import android.R
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaScannerConnection
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.SystemClock
import android.provider.MediaStore
import android.view.View
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.apploop.face.changer.app.BuildConfig
import com.apploop.face.changer.app.cropModule.UCrop
import com.apploop.face.changer.app.models.ObjStickerImage
import com.apploop.face.changer.app.models.ObjStickerImageDetail
import com.apploop.face.changer.app.models.ObjSuitImageOption
import com.apploop.face.changer.app.models.SuitList
import com.apploop.face.changer.app.views.mainactivity.MainActivity
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object Extension {

    var imageFilePath: String = ""
    var bitmapSuit: Bitmap? = null
    var saveBitmapLast: Bitmap? = null
    var suitPath = "file:///android_asset/suits/s15.webp"
    var Edit_Folder_name = "Man Jacket Editor"
    var imageGallery: ArrayList<String?> = ArrayList()
    var mLastClickTime = 0L
    var stickerViewId = ArrayList<Int>()
    var viewId = 0
    var africaList: ArrayList<SuitList?> = ArrayList()
    var iplList: ArrayList<SuitList?> = ArrayList()
    var pslList: ArrayList<SuitList?> = ArrayList()
    var bplList: ArrayList<SuitList?> = ArrayList()
    var bblList: ArrayList<SuitList?> = ArrayList()
    var wtList: ArrayList<SuitList?> = ArrayList()
    var fifaList: ArrayList<SuitList?> = ArrayList()
    var superList: ArrayList<SuitList?> = ArrayList()
    private val ProjectFolderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath + File.separator + Edit_Folder_name + File.separator
    var objSuitOptions: ArrayList<ObjSuitImageOption> = ArrayList()
    var objStickerDetailsTie: ArrayList<ObjStickerImageDetail> = ArrayList()
    var objStickerDetailsBeard: ArrayList<ObjStickerImageDetail> = ArrayList()
    var objFaceChangeDetail: ArrayList<ObjStickerImageDetail> = ArrayList()
    var oldFaceChangeDetail: ArrayList<ObjStickerImageDetail> = ArrayList()
    var kidFaceChangeDetail: ArrayList<ObjStickerImageDetail> = ArrayList()
    var objStickerDetailsMustache: ArrayList<ObjStickerImageDetail> = ArrayList()
    var objStickerDetailsHat: ArrayList<ObjStickerImageDetail> = ArrayList()
    var objStickerDetailsGoggle: ArrayList<ObjStickerImageDetail> = ArrayList()
    var objStickerDetailsPomp: ArrayList<ObjStickerImageDetail> = ArrayList()
    var objStickerDetailLong: ArrayList<ObjStickerImageDetail> = ArrayList()
    var objStickerDetailsShort: ArrayList<ObjStickerImageDetail> = ArrayList()
    var objStickerDetailsSpike: ArrayList<ObjStickerImageDetail> = ArrayList()
    var objStickerDetailsColor: ArrayList<ObjStickerImageDetail> = ArrayList()
    var objStickerDetailsBald: ArrayList<ObjStickerImageDetail> = ArrayList()
    var objStickerDetailsHair: ArrayList<ObjStickerImageDetail> = ArrayList()
    var faceColors: ArrayList<String> = ArrayList()

    var hairColors = arrayOf(
        "#FF000000",
        "#FF505F",
        "#007DFF",
        "#4CAF50",
        "#FF9800",
        "#7B61FF",
        "#616161"
    )

    fun Activity.faceChangeList() :ArrayList<String> {
        faceColors.add("#F8E4CB")
        faceColors.add("#F5DDC3")
        faceColors.add("#F6D1B7")
        faceColors.add("#EEC0A6")
        faceColors.add("#F7C89F")
        faceColors.add("#7B61FF")
        faceColors.add("#F1B994")
        faceColors.add("#EFB98F")
        faceColors.add("#E0AB83")
        faceColors.add("#DFB295")
        faceColors.add("#E2A684")
        faceColors.add("#F0C4A9")
        faceColors.add("#ECB284")
        faceColors.add("#E7A57A")
        faceColors.add("#E7A57A")
        faceColors.add("#D29E7A")
        faceColors.add("#D39773")
        faceColors.add("#DFB484")
        faceColors.add("#C99A6C")
        faceColors.add("#B07855")
        faceColors.add("#AF6D3B")
        faceColors.add("#AF8359")
        faceColors.add("#AF6D3B")
        faceColors.add("#AF8359")
        faceColors.add("#B07453")
        faceColors.add("#A96D4E")
        faceColors.add("#A66C47")
        faceColors.add("#9C6138")
        faceColors.add("#985936")
        faceColors.add("#8B6140")
        faceColors.add("#825533")
        faceColors.add("#78523F")
        faceColors.add("#664333")

        return faceColors
    }

    var objSuitOptions1: ArrayList<ObjSuitImageOption> = ArrayList()
    var objSuitOptions2: ArrayList<ObjSuitImageOption> = ArrayList()
    var objSuitOptions3: ArrayList<ObjSuitImageOption> = ArrayList()
    var objSuitOptions4: ArrayList<ObjSuitImageOption> = ArrayList()
    var objSuitOptions5: ArrayList<ObjSuitImageOption> = ArrayList()
    var bodyBuildersList: ArrayList<ObjSuitImageOption> = ArrayList()
    var kurtaList: ArrayList<ObjSuitImageOption> = ArrayList()
    var policeList: ArrayList<ObjSuitImageOption> = ArrayList()
    var tShirtList: ArrayList<ObjSuitImageOption> = ArrayList()
    var christmasList: ArrayList<ObjSuitImageOption> = ArrayList()


    fun Context.getImageUri(context: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.PNG, 0, bytes)
        val path = MediaStore.Images.Media.insertImage(
            context.contentResolver,
            inImage,
            "Title",
            null
        )
        return Uri.parse(path)
    }

    fun Context.createImageFile(): File {
        val uniqueString = UUID.randomUUID().toString()
        val file = File(
            File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    .toString()
            ), "temp$uniqueString.jpg"
        )
        imageFilePath = file.absolutePath
        return file
    }

    fun Activity.handleCropError(result: Intent) {
        val cropError = UCrop.getError(result)
        if (cropError != null) {
            Toast.makeText(this, cropError.message, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, cropError?.message, Toast.LENGTH_LONG).show()
        }
    }

    fun Activity.loadBitmap(imageView: AppCompatImageView, bitmap: Bitmap) {
        try {
            Thread {
                imageView.post(Runnable {
                    Glide.with(applicationContext)
                        .asBitmap()
                        .load(bitmap)
                        .into(imageView)
                })
            }.start()
        } catch (e: NullPointerException) {
            Thread {
                imageView.post(Runnable {
                    Glide.with(applicationContext)
                        .load(com.apploop.face.changer.app.R.drawable.suit_main)
                        .into(imageView)
                })
            }.start()
            e.localizedMessage
        }
    }

    fun Activity.listSticker(
        dirFrom: String?,
        path: String,
        emojiLists: ArrayList<ObjSuitImageOption>
    ): ArrayList<ObjSuitImageOption> {
        var emojiLists: ArrayList<ObjSuitImageOption> = emojiLists
        emojiLists = ArrayList<ObjSuitImageOption>()
        val res: Resources = resources
        val am = res.assets
        var fileList: Array<String?>? = arrayOfNulls(0)
        try {
            fileList = am.list(dirFrom!!)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        if (fileList != null) {
            for (i in fileList.indices) {
                emojiLists.add(ObjSuitImageOption(path + fileList[i]))
            }
        }
        return emojiLists
    }

    fun Activity.loadImage(image: AppCompatImageView) {
        Glide.with(applicationContext)
            .asBitmap()
            .load(suitPath).into(object : SimpleTarget<Bitmap?>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap?>?
                ) {
                    bitmapSuit = resource
                    image.setImageBitmap(resource)
                }
            })
    }

    @SuppressLint("ResourceAsColor")
    fun Activity.getBitmapFromView(view: View): Bitmap? {
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        val bgDrawable = view.background
        if (bgDrawable != null) {
            bgDrawable.draw(canvas)
        } else {
            canvas.drawColor(R.color.transparent)
        }
        view.draw(canvas)
        return returnedBitmap
    }

    fun Activity.uriToBitmap(uri: Uri): Bitmap {
        var bitmap: Bitmap? = null
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bitmap!!
    }

    fun Activity.statusBarColor(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor =
                resources.getColor(color, this.theme)
        } else {
            window.statusBarColor =
                resources.getColor(color)
        }
    }

    private fun Activity.listSticker1(
        dirFrom: String?,
        path: String,
        emojiLists: ArrayList<ObjStickerImageDetail>
    ): ArrayList<ObjStickerImageDetail> {
        var emojiLists: ArrayList<ObjStickerImageDetail> = emojiLists
        emojiLists = ArrayList<ObjStickerImageDetail>()
        val res: Resources = resources
        val am = res.assets
        var fileList: Array<String?>? = arrayOfNulls(0)
        try {
            fileList = am.list(dirFrom!!)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        if (fileList != null) {
            for (i in fileList.indices) {
                emojiLists.add(ObjStickerImageDetail("file:///android_asset/" + path + fileList[i]))
            }
        }
        return emojiLists
    }

    private fun Activity.listSticker2(
        dirFrom: String?,
        path: String,
        emojiLists: ArrayList<ObjStickerImage>
    ): ArrayList<ObjStickerImage> {
        var emojiLists: ArrayList<ObjStickerImage> = emojiLists
        emojiLists = ArrayList<ObjStickerImage>()
        val res: Resources = resources
        val am = res.assets
        var fileList: Array<String?>? = arrayOfNulls(0)
        try {
            fileList = am.list(dirFrom!!)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        if (fileList != null) {
            for (i in fileList.indices) {
                fileList[i]?.let {
                    ObjStickerImage(it, "file:///android_asset/" + path + fileList[i])
                }?.let {
                    emojiLists.add(it)
                }
            }
        }
        return emojiLists
    }

    fun Activity.initLists() {
        objSuitOptions = listSticker("backgroundBG", "backgroundBG/", objSuitOptions)
        objStickerDetailsTie = listSticker1("symbol/TieBow", "symbol/TieBow/", objStickerDetailsTie)
        objStickerDetailsBeard =
            listSticker1("symbol/Beard", "symbol/Beard/", objStickerDetailsBeard)
        objFaceChangeDetail = listSticker1("faceChange", "faceChange/", objFaceChangeDetail)
        kidFaceChangeDetail = listSticker1("faceChange", "faceChange/", kidFaceChangeDetail)
        oldFaceChangeDetail = listSticker1("oldFaceChange", "oldFaceChange/", oldFaceChangeDetail)
        objStickerDetailsMustache =
            listSticker1("symbol/Mustaches", "symbol/Mustaches/", objStickerDetailsMustache)
        objStickerDetailsGoggle = listSticker1(
            "symbol/GogglesSunglass",
            "symbol/GogglesSunglass/",
            objStickerDetailsGoggle
        )
        objStickerDetailsHat = listSticker1("symbol/Hat", "symbol/Hat/", objStickerDetailsHat)
        objStickerDetailsHair =
            listSticker1("hairstyles/Coiffure", "hairstyles/Coiffure/", objStickerDetailsHair)

        objStickerDetailsHair.addAll(objStickerDetailsHair)

        objStickerDetailsPomp =
            listSticker1("hairstyles/Coiffure", "hairstyles/Coiffure/", objStickerDetailsPomp)
        objStickerDetailsHair.addAll(objStickerDetailsPomp)
        objStickerDetailLong =
            listSticker1("hairstyles/Longhair", "hairstyles/Longhair/", objStickerDetailLong)
        objStickerDetailsHair.addAll(objStickerDetailLong)
        objStickerDetailsShort =
            listSticker1("hairstyles/Crewcut", "hairstyles/Crewcut/", objStickerDetailsShort)
        objStickerDetailsHair.addAll(objStickerDetailsShort)
        objStickerDetailsSpike =
            listSticker1("hairstyles/Spiky", "hairstyles/Spiky/", objStickerDetailsSpike)
        objStickerDetailsHair.addAll(objStickerDetailsSpike)
        objStickerDetailsColor =
            listSticker1("hairstyles/Colored", "hairstyles/Colored/", objStickerDetailsColor)
        objStickerDetailsHair.addAll(objStickerDetailsColor)
        objStickerDetailsBald =
            listSticker1("hairstyles/Bald", "hairstyles/Bald/", objStickerDetailsBald)
        objStickerDetailsHair.addAll(objStickerDetailsBald)
    }

    fun Activity.createDirectoryAndSaveFile(bitmap2: Bitmap): String {
        val file = File(ProjectFolderPath)
        if (!file.exists()) {
            file.mkdirs()
        }
        val ts = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val fileName = "Img-$ts.jpeg"
        val file2 = File(file, fileName)
        if (file2.exists()) {
            file2.delete()
        }
        try {
            val fileOutputStream = FileOutputStream(file2)
            bitmap2.compress(Bitmap.CompressFormat.JPEG, 80, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        MediaScannerConnection.scanFile(this, arrayOf(file2.absolutePath.toString()), null, null)
        if (Build.VERSION.SDK_INT >= 19) {
            val intent6 = Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE")
            intent6.data = Uri.fromFile(file2)
            sendBroadcast(intent6)
        } else {
            val sb = StringBuilder()
            sb.append("file://")
            sb.append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES))
            sendBroadcast(Intent("android.intent.action.MEDIA_MOUNTED", Uri.parse(sb.toString())))
        }
        return file2.absolutePath
    }

    fun Activity.share(path: String) {
        try {
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "image/*"
            sharingIntent.putExtra(
                Intent.EXTRA_TEXT,
                "Men's Jacket Editor\nCreated By AppLoop : Check out the App at: https://play.google.com/store/apps/details?id=$packageName"
            )
            sharingIntent.putExtra(
                Intent.EXTRA_STREAM,
                FileProvider.getUriForFile(
                    applicationContext,
                    BuildConfig.APPLICATION_ID.toString() + ".provider",
                    File(path)
                )
            )
            startActivity(Intent.createChooser(sharingIntent, "Share Image using"))
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun Activity.fetchImage() {
        imageGallery.clear()
        listAllImages(File(ProjectFolderPath))
    }

    private fun listAllImages(filepath: File) {
        val files = filepath.listFiles()
        if (files != null) {
            for (j in files.indices.reversed()) {
                val ss = files[j].toString()
                val check = File(ss)
                if (check.length() <= 1 shl 10) {
                } else if (check.toString().contains(".jpg") || check.toString()
                        .contains(".png") || check.toString().contains(".jpeg")
                ) {
                    imageGallery.add(ss)
                }
            }
            return
        }
    }

    fun Activity.alertDialog(path: String) {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView: View =
            inflater.inflate(com.apploop.face.changer.app.R.layout.cus_delete_dialog, null)
        dialogBuilder.setView(dialogView)

        val alertDialog = dialogBuilder.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()

        val lvCancel =
            dialogView.findViewById<View>(com.apploop.face.changer.app.R.id.lv_cancel) as ConstraintLayout
        val lvDelete =
            dialogView.findViewById<View>(com.apploop.face.changer.app.R.id.lv_delete) as ConstraintLayout

        lvCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        lvDelete.setOnClickListener {
            val fD: File = File(path)
            if (fD.exists()) {
                if (fD.delete()) {
                    imageGallery.remove(path)
                    if (imageGallery.size <= 0) {
                        finish()
                    }
                    sendBroadcast(
                        Intent(
                            "android.intent.action.MEDIA_SCANNER_SCAN_FILE",
                            Uri.fromFile(fD)
                        )
                    )
                }
                alertDialog.dismiss()
                finish()
            }
        }
    }

    fun Activity.alertDiscardDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView: View =
            inflater.inflate(com.apploop.face.changer.app.R.layout.cus_discard_dialog, null)
        dialogBuilder.setView(dialogView)

        val alertDialog = dialogBuilder.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()

        val lvCancel =
            dialogView.findViewById<View>(com.apploop.face.changer.app.R.id.lv_cancel) as ConstraintLayout
        val lv_yes =
            dialogView.findViewById<View>(com.apploop.face.changer.app.R.id.lv_yes) as ConstraintLayout

        lvCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        lv_yes.setOnClickListener {
            alertDialog.dismiss()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }

    fun Activity.initSuitsLists() {
        objSuitOptions1 = listSticker("blazeres", "blazeres/", objSuitOptions1)
        objSuitOptions2 = listSticker("formales", "formales/", objSuitOptions2)
        objSuitOptions3 = listSticker("jacketes", "jacketes/", objSuitOptions3)
        objSuitOptions4 = listSticker("suits", "suits/", objSuitOptions4)
        objSuitOptions5 = listSticker("police", "police/", objSuitOptions5)
        kurtaList = listSticker("kurta", "kurta/", kurtaList)
        bodyBuildersList = listSticker("bodybuilder", "bodybuilder/", bodyBuildersList)
        tShirtList = listSticker("tshirt", "tshirt/", bodyBuildersList)
        christmasList = listSticker("christmas", "christmas/", bodyBuildersList)
    }

    fun Activity.alertDialogRate() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView: View =
            inflater.inflate(com.apploop.face.changer.app.R.layout.cus_rate_dialog, null)
        dialogBuilder.setView(dialogView)

        val rating =
            dialogView.findViewById<RatingBar>(com.apploop.face.changer.app.R.id.rating_bar)
        val ivReaction =
            dialogView.findViewById<AppCompatImageView>(com.apploop.face.changer.app.R.id.iv_reaction)

        val btnRate =
            dialogView.findViewById<AppCompatButton>(com.apploop.face.changer.app.R.id.btn_rateUs)

        btnRate.setOnClickListener {
            SharedPrefHelper.writeBoolean("isOpened", true)
            rateApp()
        }

        rating.onRatingBarChangeListener =
            RatingBar.OnRatingBarChangeListener { p0, p1, p2 ->
                when (p1) {
                    1f -> {
                        Glide.with(applicationContext)
                            .load(com.apploop.face.changer.app.R.drawable.ic_emoji2)
                            .into(ivReaction)
                    }

                    2f -> {
                        Glide.with(applicationContext)
                            .load(com.apploop.face.changer.app.R.drawable.ic_emoji3)
                            .into(ivReaction)
                    }

                    3f -> {
                        Glide.with(applicationContext)
                            .load(com.apploop.face.changer.app.R.drawable.ic_emoji4)
                            .into(ivReaction)
                    }

                    4f -> {
                        Glide.with(applicationContext)
                            .load(com.apploop.face.changer.app.R.drawable.ic_emoji5)
                            .into(ivReaction)
                    }

                    5f -> {
                        Glide.with(applicationContext)
                            .load(com.apploop.face.changer.app.R.drawable.ic_emoji6)
                            .into(ivReaction)
                    }
                }
            }

        runOnUiThread(Runnable {
            if (!isFinishing) {
                val alertDialog = dialogBuilder.create()
                alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                alertDialog.show()
            }
        })
    }

    fun Activity.isOpenRecently(): Boolean {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return true
        }
        mLastClickTime = SystemClock.elapsedRealtime()
        return false
    }

    fun Activity.shareApp() {
        val appPackageName: String = packageName
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(
            Intent.EXTRA_TEXT,
            "Check out the App at: https://play.google.com/store/apps/details?id=$appPackageName"
        )
        sendIntent.type = "text/plain"
        startActivity(sendIntent)
    }

    fun Activity.rateApp() {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
        } catch (activity: ActivityNotFoundException) {
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                    )
                )
            } catch (activity: ActivityNotFoundException) {
//                Toast.makeText(this, R.string.no_app_event, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun Context.isInternetAvailable(): Boolean {
        var result = false
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }
        return result
    }

}