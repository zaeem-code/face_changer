package com.apploop.face.changer.app.views.mainactivity

import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import com.apploop.face.changer.app.BottomSheetFragmentListner
import com.apploop.face.changer.app.BuildConfig
import com.apploop.face.changer.app.InAppBilling.SubscriptionBillingManager
import com.apploop.face.changer.app.R
import com.apploop.face.changer.app.bottomsheets.CustomBSFragment
import com.apploop.face.changer.app.callBacks.AdapterPathInterface
import com.apploop.face.changer.app.callBacks.MainViewModelInterface
import com.apploop.face.changer.app.databinding.ActivityMainBinding
import com.apploop.face.changer.app.helpers.EnumClass
import com.apploop.face.changer.app.utils.Extension
import com.apploop.face.changer.app.utils.Extension.isInternetAvailable
import com.apploop.face.changer.app.utils.Extension.rateApp
import com.apploop.face.changer.app.utils.Extension.shareApp
import com.apploop.face.changer.app.utils.Extension.statusBarColor
import com.apploop.face.changer.app.utils.SharedPrefHelper
import com.apploop.face.changer.app.utils.UtilsCons
import com.apploop.face.changer.app.viewModels.MainActivityViewModel
import com.apploop.face.changer.app.views.faceChangeScreen.FaceChangeActivity
import com.apploop.face.changer.app.views.IntroduceDialog
import com.apploop.face.changer.app.views.MenPhotoScreen.MenPhotoActivity
import com.apploop.face.changer.app.views.myCreation.MyCreationActivity
import com.apploop.face.changer.app.views.premium.PremiumActivity
import com.apploop.face.changer.app.views.removeBackground.ImageRemoveBgActivity
import com.apploop.face.changer.app.views.removeBackground.StoreManager
import com.theartofdev.edmodo.cropper.CropImage
import org.json.JSONObject
import java.io.IOException


class MainActivity : AppCompatActivity(), View.OnClickListener, MainViewModelInterface,
     AdapterPathInterface, BottomSheetFragmentListner {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainActivityViewModel: MainActivityViewModel
    private lateinit var introduceDialog: IntroduceDialog
    private val MY_CAMERA_PERMISSION_CODE = 100
    private val MY_GALLERY_PERMISSION_CODE = 101
    var mSelectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        statusBarColor(R.color.background)
        init()
        SharedPrefHelper.writeBoolean("splashLoaded",false)


        SubscriptionBillingManager(this, this)

        introduceDialog = IntroduceDialog(this, this@MainActivity)

        binding.ivPremium.setOnClickListener {

            startActivity(Intent(this, PremiumActivity::class.java))

//            if (!OpenAdManager.getInstance().enabledNoAds) {
//                startActivity(Intent(this, PremiumActivity::class.java))
//            } else {
//                binding.ivPremium.visibility = View.INVISIBLE
//            }
        }
    }


    private fun init() {
        binding.ivPremium.setOnClickListener {
            startActivity(Intent(this, PremiumActivity::class.java))
        }

//        binding.shimmerFrameLayout.startShimmer()
//        AdsManager.instance?.showNativeAd(
//            binding.frameLayout,
//            binding.frameLayout,
//            layoutInflater,
//            R.layout.ad_with_media
//        ) {
//            if (it) {
//                binding.shimmerFrameLayout.visibility = View.INVISIBLE
//            } else {
//                binding.shimmerFrameLayout.visibility = View.INVISIBLE
//                binding.frameLayout.visibility = View.INVISIBLE
//            }
//        }

        mainActivityViewModel = MainActivityViewModel(this)
        binding.mainActivityViewModel = mainActivityViewModel
        binding.layoutNavigationView.tvVersion.text =
            "Version = ${BuildConfig.VERSION_CODE.toString()}"

        initClicks()


    }

    private fun initClicks() {
        binding.layoutNavigationView.ivCross.setOnClickListener(this)
        binding.layoutNavigationView.lvPremium.setOnClickListener(this)
        binding.layoutNavigationView.lvCreation.setOnClickListener(this)
        binding.layoutNavigationView.lvShare.setOnClickListener(this)
        binding.layoutNavigationView.lvRateUs.setOnClickListener(this)
        binding.layoutNavigationView.lvMore.setOnClickListener(this)
        binding.layoutNavigationView.ivButton.setOnClickListener(this)
        binding.ivDrawer.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.lv_creation -> {
                startActivity(Intent(this, MyCreationActivity::class.java))
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }

            R.id.iv_cross -> {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }

            R.id.lv_premium -> {
                startActivity(Intent(this, PremiumActivity::class.java))
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }

            R.id.lv_share -> {
                shareApp()
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }

            R.id.lv_rateUs -> {
                rateApp()
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }

            R.id.lv_more -> {
                openAppInPlayStore(BuildConfig.APPLICATION_ID)
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }

            R.id.iv_drawer -> {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }

            R.id.iv_button -> {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }
    }


    fun openAppInPlayStore(appPackageName: String) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:AppLoop")))
        } catch (exception: android.content.ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                )
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onMainActivityButtonClicks(type: EnumClass) {
        when (type) {
            EnumClass.MY_CREATION -> {
                startActivity(Intent(this, MyCreationActivity::class.java))
            }

            EnumClass.PHOTO_MEN -> {
                UtilsCons.chooseLayout = "PHOTO_MEN"
                showHeartRateBottomSheet()
            }

            EnumClass.PHOTO_REMOVE_BG -> {
                UtilsCons.chooseLayout = "PHOTO_REMOVE_BG"
                showHeartRateBottomSheet()
            }

            EnumClass.PHOTO_FACE -> {
                UtilsCons.chooseLayout = "PHOTO_FACE"
                if (!SharedPrefHelper.readBoolean("isTrue")) {
                    introduceDialog.showDialog()
                    SharedPrefHelper.writeBoolean("isTrue", true)
                } else {
                    showHeartRateBottomSheet()
                }
            }

            else -> {}
        }
    }

    fun showHeartRateBottomSheet() {
        CustomBSFragment().apply {
            show(supportFragmentManager, tag)
            onClick = { _it ->
                if (_it) {
                    this.dismiss()
                } else {
                    this.dismiss()
                }
            }
        }
    }



    override fun onClick() {
        introduceDialog.closeDialog()
        showHeartRateBottomSheet()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            try {
                when (requestCode) {
                    101 -> {
                        var selectedImage = data!!.data
                        CropImage.activity(selectedImage).setInitialCropWindowPaddingRatio(0F)
                            .start(this@MainActivity)

                        val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(
                            this@MainActivity.contentResolver, selectedImage
                        )
                        UtilsCons.originalPath = ""
                        UtilsCons.fromGallery = "yes"
                        UtilsCons.originalBitmap = bitmap
                    }

                    CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                        val result: CropImage.ActivityResult = CropImage.getActivityResult(data)

                        var resultUri = result.uri
                        var bitmap: Bitmap? = null
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, resultUri)
                            UtilsCons.originalBitmap = bitmap
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }

                        if (UtilsCons.chooseLayout.contains("PHOTO_REMOVE_BG")) {
                            call()
                        } else if (UtilsCons.chooseLayout.contains("PHOTO_MEN")) {

                            val intent = Intent(this@MainActivity, MenPhotoActivity::class.java)
                            startActivity(intent)

                        } else {

                            val intent = Intent(this@MainActivity, FaceChangeActivity::class.java)
                            startActivity(intent)
                        }

                        UtilsCons.originalPath = ""
                        UtilsCons.fromGallery = ""
//                        UtilsCons.originalBitmap = resultUri
                        Log.e("originalBitmap", UtilsCons.originalBitmap.toString())
                        UtilsCons.originalPath = Extension.imageFilePath
                        Log.e("originalPath", UtilsCons.originalPath.toString())
                    }

                    CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE -> {
                        Toast.makeText(this@MainActivity, "error", Toast.LENGTH_SHORT).show()
                    }

                    102 -> CropImage.activity(mSelectedImageUri
                    ).setInitialCropWindowPaddingRatio(0F).start(this@MainActivity)
                }
            } catch (e: java.lang.Exception) {
                Log.e("originalPathException", e.message.toString())
                Toast.makeText(this@MainActivity, "" + e.message, Toast.LENGTH_SHORT).show()
            }
        }

//        val result = CropImage.getActivityResult(data)
//        if (resultCode == RESULT_OK && result != null) {
//            UtilsCons.originalBitmap = result.bitmap
//            if (UtilsCons.originalBitmap != null) {
//                if (UtilsCons.chooseLayout.contains("PHOTO_REMOVE_BG")) {
//                    call()
//                } else if (UtilsCons.chooseLayout.contains("PHOTO_MEN")) {
//                    val intent = Intent(this@MainActivity, MenPhotoActivity::class.java)
//                    startActivity(intent)
//                    finish()
//                } else {
//                    val intent = Intent(this@MainActivity, FaceChangeActivity::class.java)
//                    startActivity(intent)
//                    finish()
//                }
//            }
//        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//            val error = result!!.error
//        }
    }

    fun call() {
        try {
            StoreManager.setCurrentCropedBitmap(this@MainActivity, null as Bitmap?)
            StoreManager.setCurrentCroppedMaskBitmap(this@MainActivity, null as Bitmap?)
            ImageRemoveBgActivity.setFaceBitmap(UtilsCons.originalBitmap)
            StoreManager.setCurrentOriginalBitmap(this@MainActivity, UtilsCons.originalBitmap)
            startActivity(Intent(this@MainActivity, ImageRemoveBgActivity::class.java))
            finish()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    override fun onCameraClick() {
        try {
            var values = ContentValues()
            values.put(MediaStore.Images.Media.TITLE, "New Picture")
            values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera")
            mSelectedImageUri =
                contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mSelectedImageUri)
            startActivityForResult(
                intent, 102
            )

//            CustomBSFragment().apply {
//                closeBottomSheet()
//            }
        } catch (e: java.lang.Exception) {
        }
    }

    override fun onGalleryClick() {
        try {
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, 101)

//            CustomBSFragment().apply {
//                closeBottomSheet()
//            }
        } catch (e: ActivityNotFoundException) {
            e.localizedMessage
        }
    }

    override fun onClose() {
        CustomBSFragment().apply {
           closeBottomSheet()
        }
    }
}