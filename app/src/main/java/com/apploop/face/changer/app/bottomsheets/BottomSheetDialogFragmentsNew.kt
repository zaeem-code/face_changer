package com.apploop.face.changer.app.bottomsheets

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.apploop.face.changer.app.BottomSheetFragmentListner
import com.apploop.face.changer.app.BuildConfig
import com.apploop.face.changer.app.R
import com.apploop.face.changer.app.databinding.CusOpenCameraBottomSheetBinding
import com.apploop.face.changer.app.utils.Extension
import com.apploop.face.changer.app.utils.Extension.createImageFile
import com.apploop.face.changer.app.utils.UtilsCons
import com.apploop.face.changer.app.views.faceChangeScreen.FaceChangeActivity
import com.apploop.face.changer.app.views.MenPhotoScreen.MenPhotoActivity
import com.apploop.face.changer.app.views.removeBackground.ImageRemoveBgActivity
import com.apploop.face.changer.app.views.removeBackground.StoreManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.theartofdev.edmodo.cropper.CropImage
import com.yalantis.ucrop.UCrop
import java.io.File
import java.util.UUID


class CustomBSFragment() : BottomSheetDialogFragment() {
    companion object {
        val TAG = "HeartRateDetailBSFragment"
    }

    private var _binding: CusOpenCameraBottomSheetBinding? = null

    //    private static final String ARG_PARAM1 = "param1";
    //    private String type;
    private var bottomSheetFragmentListner: BottomSheetFragmentListner? = null
    private val binding get() = _binding
    private val MY_CAMERA_PERMISSION_CODE = 100
    private val MY_GALLERY_PERMISSION_CODE = 101
    var mSelectedImageUri: Uri? = null
    var resultUri: Uri? = null
    var mSelectedImagePath: String? = null
    var mSelectedOutputPath: String? = null
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    var onClick: ((Boolean) -> Unit)? = null
    private val GALLERY_REQUEST_CODE = 1234
    private val WRITE_EXTERNAL_STORAGE_CODE = 1

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        _binding = CusOpenCameraBottomSheetBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            bottomSheetFragmentListner = activity as BottomSheetFragmentListner
        } catch (e: ClassCastException) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement TextClicked");
            Log.d("Error BottomS", e.message!!)
        }

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
//                binding?.shimmerFrameLayout?.visibility = View.INVISIBLE
//            } else {
//                binding?.shimmerFrameLayout?.visibility = View.INVISIBLE
//                binding?.frameLayout?.visibility = View.INVISIBLE
//            }
//        }

        binding?.lvBlazer?.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),MY_CAMERA_PERMISSION_CODE)
            } else {
                try {
                    bottomSheetFragmentListner!!.onCameraClick()
                    closeBottomSheet()

                } catch (e: Exception) {
                }
            }
        }

        binding?.lvJacket?.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissions(arrayOf(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.WRITE_EXTERNAL_STORAGE), MY_GALLERY_PERMISSION_CODE)
            } else {
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), MY_GALLERY_PERMISSION_CODE)
            }
        }
    }

    private fun launchImageCrop(uri: Uri) {
        var destination: String = StringBuilder(UUID.randomUUID().toString()).toString()
        var options: UCrop.Options = UCrop.Options()

        UCrop.of(
            Uri.parse(uri.toString()),
            Uri.fromFile(File(requireActivity().cacheDir, destination))
        )
            .withOptions(options)
            .withAspectRatio(0F, 0F)
            .useSourceImageAspectRatio()
            .withMaxResultSize(2000, 2000)
            .start(requireActivity())
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
                    try {
                        bottomSheetFragmentListner!!.onGalleryClick()
                        closeBottomSheet()
                    } catch (e: Exception) {
                    }
                } else {
                    Toast.makeText(requireActivity(), "camera permission denied", Toast.LENGTH_LONG)
                        .show()
                }
            } else if (requestCode == MY_GALLERY_PERMISSION_CODE) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    pickFromGallery()
                    bottomSheetFragmentListner!!.onGalleryClick()
                    closeBottomSheet()

                } else {
                    Toast.makeText(requireActivity(), "camera permission denied", Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: ArrayIndexOutOfBoundsException) {
            e.localizedMessage
        }
    }

    @SuppressLint("WrongConstant")
    private fun openCamera() {
//        try {
//            var values = ContentValues()
//            values.put(MediaStore.Images.Media.TITLE, "New Picture")
//            values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera")
//            mSelectedImageUri = requireActivity().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
//            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, mSelectedImageUri)
//            startActivityForResult(
//                intent,
//                102)
//        } catch (e: java.lang.Exception) {
//
//        }
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        var file: File? = null
        try {
            file = requireActivity().createImageFile()
        } catch (unused: Exception) {
            Toast.makeText(
                requireActivity(),
                "Error occurred while creating the File",
                Toast.LENGTH_SHORT
            )
                .show()
        }
        if (file == null) {
            Toast.makeText(
                requireActivity(),
                "Error occurred while creating the File",
                Toast.LENGTH_SHORT
            )
                .show()
            return
        }
        val uriForFile = FileProvider.getUriForFile(
            requireActivity(),
            BuildConfig.APPLICATION_ID.toString() + ".provider",
            file
        )
        try {
            if (intent.resolveActivity(activity?.packageManager!!) != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uriForFile)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        } catch (e: ActivityNotFoundException) {
            e.localizedMessage
        }
        getResultFromCamera.launch(intent)
    }

    private fun pickFromGallery() {
//        try {
//            val galleryIntent =
//                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            startActivityForResult(galleryIntent, 101)
//        } catch (e: ActivityNotFoundException) {
//            e.localizedMessage
//        }

        try {
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            getResultFromGallery.launch(galleryIntent)
        } catch (e: ActivityNotFoundException) {
            e.localizedMessage
        }
    }

  /*  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            try {
                when (requestCode) {
                    101 -> {
                        var selectedImage = data!!.data

                        val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, selectedImage)
                        UtilsCons.originalPath = ""
                        UtilsCons.fromGallery = "yes"
                        UtilsCons.originalBitmap = bitmap

                        CropImage.activity(selectedImage).setInitialCropWindowPaddingRatio(0F).start(requireActivity())
                    }

                    CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                        val result: CropImage.ActivityResult = CropImage.getActivityResult(data)

                            resultUri = result.uri
                            if (UtilsCons.chooseLayout.contains("PHOTO_REMOVE_BG")) {
                                val intent = Intent(requireActivity(), RemoveBgActivity::class.java)
                                startActivity(intent)
                                requireActivity().finish()
                            } else if (UtilsCons.chooseLayout.contains("PHOTO_MEN")) {
                                val intent = Intent(requireActivity(), MenPhotoActivity::class.java)
                                startActivity(intent)
                                requireActivity().finish()
                            } else {
                                val intent = Intent(requireActivity(), FaceChangeActivity::class.java)
                                startActivity(intent)
                                requireActivity().finish()
                            }

                            UtilsCons.originalPath = ""
                            UtilsCons.fromGallery = ""
                            UtilsCons.originalBitmap = BitmapFactory.decodeFile(Extension.imageFilePath)
                            UtilsCons.originalPath = Extension.imageFilePath

                    }
                    CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE -> {
                        Toast.makeText(context, "error" , Toast.LENGTH_SHORT).show()
                    }
                   102 ->
                       CropImage.activity(
                       mSelectedImageUri
                    ).setInitialCropWindowPaddingRatio(0F).start(requireActivity())
                }
            } catch (e: java.lang.Exception) {
                Toast.makeText(context, "" + e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }*/

    private val getResultFromCamera =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == AppCompatActivity.RESULT_OK) {
//            val intent = Intent(requireActivity(), com.apploop.face.changer.app.views.handCrop.HandCropActivity::class.java)
            var uri: Uri? = null
            try {
                uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    FileProvider.getUriForFile(
                        requireActivity(),
                        BuildConfig.APPLICATION_ID + ".provider", File(Extension.imageFilePath)
                    )
                } else {
                    Uri.fromFile(File((Extension.imageFilePath)))
                }
//                launchImageCrop(uri)

            } catch (e: Exception) {
                e.localizedMessage
            }

                CropImage.activity(uri)
                    .setFixAspectRatio(true)
                    .setAspectRatio(400, 540)
                    .start(requireActivity())
//            intent.putExtra("image_uri", uri.toString())
//            intent.putExtra("path", uri.toString())

            UtilsCons.originalBitmap = null
            UtilsCons.originalPath = ""
            UtilsCons.fromGallery = ""
            UtilsCons.originalBitmap = BitmapFactory.decodeFile(Extension.imageFilePath)
            UtilsCons.originalPath = Extension.imageFilePath
            if (UtilsCons.originalBitmap == null) {
                dismiss()
                return@registerForActivityResult
            }
//            startActivity(intent)
            dismiss()
        }
}

    private val getResultFromGallery =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {

            if (it.resultCode == Activity.RESULT_OK) {
                try {
                    val uri: Uri = it.data?.data!!

                    val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(
                        requireActivity().contentResolver,
                        uri)

                    UtilsCons.originalBitmap = bitmap

//                    val intent = Intent(requireActivity(), com.apploop.face.changer.app.views.handCrop.HandCropActivity::class.java)
//                    intent.putExtra("image_uri", uri.toString())
//                    intent.putExtra("path", uri.toString())
//                    startActivity(intent)
//

                    CropImage.activity(uri)
                        .setFixAspectRatio(true)
                        .setAspectRatio(400, 540)
                        .start(requireActivity())

                    UtilsCons.originalPath = ""
                    UtilsCons.fromGallery = "yes"
                    dismiss()
                } catch (e: Exception) {
                    e.localizedMessage
                }

                val result = CropImage.getActivityResult(it.data)
                if (it.resultCode == Activity.RESULT_OK && result != null) {
                    UtilsCons.originalBitmap = result.bitmap
                    if (UtilsCons.originalBitmap != null) {
                        if (UtilsCons.chooseLayout.contains("PHOTO_REMOVE_BG")) {

                            call()
                        } else if (UtilsCons.chooseLayout.contains("PHOTO_MEN")) {

                            val intent = Intent(requireActivity(), MenPhotoActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()
                        } else {

                            val intent = Intent(requireActivity(), FaceChangeActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()
                        }
                    }
                } else if (it.resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    val error = result!!.error
                }
            }
        }

    public fun closeBottomSheet(){
        dismiss()
    }

    fun call() {
        try {
            StoreManager.setCurrentCropedBitmap(requireActivity(), null as Bitmap?)
            StoreManager.setCurrentCroppedMaskBitmap(requireActivity(), null as Bitmap?)
            ImageRemoveBgActivity.setFaceBitmap(UtilsCons.originalBitmap)
            StoreManager.setCurrentOriginalBitmap(requireActivity(), UtilsCons.originalBitmap)
            startActivity(Intent(requireActivity(), ImageRemoveBgActivity::class.java))
            requireActivity().finish()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
    override fun getTheme(): Int {
        return R.style.SheetDialog
    }

}
