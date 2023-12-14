package com.apploop.face.changer.app.bottomsheets

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.apploop.face.changer.app.BuildConfig
import com.apploop.face.changer.app.R
import com.apploop.face.changer.app.databinding.CusOpenCameraBottomSheetBinding
import com.apploop.face.changer.app.utils.Extension.createImageFile
import com.apploop.face.changer.app.views.ShowAssertsActivity
import java.io.File

class OpenCameraBottomSheet(
    var activity: ShowAssertsActivity
) : BottomSheetDialogFragment() {

    private lateinit var binding: CusOpenCameraBottomSheetBinding
    private val MY_CAMERA_PERMISSION_CODE = 100
    private val MY_GALLERY_PERMISSION_CODE = 101

    public fun OpenCameraBottomSheet(){
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.cus_open_camera_bottom_sheet,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {

//        AdsManager.Companion.instance!!.showNativeAd(
//            binding!!.frameLayout,
//            binding!!.frameLayout,
//            layoutInflater,
//            R.layout.ad_media
//        )  {
//            if (it) {
//                binding.shimmerFrameLayout.visibility = View.INVISIBLE
//            } else {
//                binding.shimmerFrameLayout.visibility = View.INVISIBLE
//                binding.frameLayout.visibility = View.INVISIBLE
//            }
//        }

        binding.lvBlazer.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ),
                    MY_CAMERA_PERMISSION_CODE
                )
            } else {
                openCamera()
            }
        }

        binding.lvJacket.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ),
                    MY_GALLERY_PERMISSION_CODE
                )
            } else {
                openGallery()
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        try {
            if (requestCode == MY_CAMERA_PERMISSION_CODE) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                } else {
                    Toast.makeText(activity, "camera permission denied", Toast.LENGTH_LONG).show()
                }
            } else if (requestCode == MY_GALLERY_PERMISSION_CODE) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery()
                } else {
                    Toast.makeText(activity, "camera permission denied", Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: ArrayIndexOutOfBoundsException) {
            e.localizedMessage
        }
    }

    @SuppressLint("WrongConstant")
    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        var file: File? = null
        try {
            file = activity.createImageFile()
        } catch (unused: Exception) {
            Toast.makeText(activity, "Error occurred while creating the File", Toast.LENGTH_SHORT)
                .show()
        }
        if (file == null) {
            Toast.makeText(activity, "Error occurred while creating the File", Toast.LENGTH_SHORT)
                .show()
            return
        }
        val uriForFile = FileProvider.getUriForFile(
            activity,
            BuildConfig.APPLICATION_ID.toString() + ".provider",
            file
        )
        intent.putExtra("output", uriForFile)
        if (Build.VERSION.SDK_INT <= 21) {
            intent.clipData = ClipData.newRawUri("", uriForFile)
            intent.addFlags(3)
        }
        getResultFromCamera.launch(intent)
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        getResultFromGallery.launch(galleryIntent)
    }

    private val getResultFromCamera =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
//            val intent = Intent(activity, RemoveBgActivity::class.java)
//            var uri: Uri? = null
//            uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                FileProvider.getUriForFile(
//                    activity,
//                    BuildConfig.APPLICATION_ID + ".provider", File(imageFilePath)
//                )
//            } else {
//                Uri.fromFile(File((imageFilePath)))
//            }
//            intent.putExtra("image_uri", uri.toString())
//            UtilsCons.originalBitmap = null
//            UtilsCons.originalPath = ""
//            UtilsCons.fromGallery = ""
//            UtilsCons.originalBitmap = BitmapFactory.decodeFile(imageFilePath)
//            UtilsCons.originalPath = imageFilePath
//            if (UtilsCons.originalBitmap == null) {
//                dismiss()
//                return@registerForActivityResult
//            }
//            startActivity(intent)
//            dismiss()
        }


    private val getResultFromGallery =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                try {
                    val selectedUri: Uri = it.data?.data!!
                    com.apploop.face.changer.app.utils.UtilsCons.originalBitmap = null
                    com.apploop.face.changer.app.utils.UtilsCons.originalPath = ""
                    com.apploop.face.changer.app.utils.UtilsCons.fromGallery = "yes"
//                    val bitmap: Bitmap = if (Build.VERSION.SDK_INT >= 29) {
//                        val source: ImageDecoder.Source = ImageDecoder.createSource(
//                            activity.contentResolver, selectedUri
//                        )
//                        ImageDecoder.decodeBitmap(source)
//                    } else {
//                        MediaStore.Images.Media.getBitmap(activity.contentResolver, selectedUri)
//                    }
//                    val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(activity.contentResolver, selectedUri)
//                    UtilsCons.originalBitmap = bitmap
//                    val intent = Intent(activity, RemoveBgActivity::class.java)
//                    intent.putExtra("image_uri", selectedUri.toString())
//                    startActivity(intent)
//                    dismiss()
                } catch (e: Exception) {
                    e.localizedMessage
                }
            }
        }

    override fun getTheme(): Int {
        return R.style.SheetDialog
    }
}