package com.apploop.face.changer.app.views.mainactivity

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet.Constraint
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import com.apploop.face.changer.app.BuildConfig
import com.apploop.face.changer.app.InAppBilling.SubscriptionBillingManager
import com.apploop.face.changer.app.R
import com.apploop.face.changer.app.api.apiRespoInterfaces.CallBackResponseJson
import com.apploop.face.changer.app.api.viewModel.ViewModelVideos
import com.apploop.face.changer.app.bottomsheets.CustomBSFragment
import com.apploop.face.changer.app.callBacks.AdapterPathInterface
import com.apploop.face.changer.app.callBacks.MainViewModelInterface
import com.apploop.face.changer.app.databinding.ActivityMainBinding
import com.apploop.face.changer.app.helpers.EnumClass
import com.apploop.face.changer.app.manager.AdsManager
import com.apploop.face.changer.app.utils.Extension
import com.apploop.face.changer.app.utils.Extension.isInternetAvailable
import com.apploop.face.changer.app.utils.Extension.rateApp
import com.apploop.face.changer.app.utils.Extension.shareApp
import com.apploop.face.changer.app.utils.Extension.statusBarColor
import com.apploop.face.changer.app.utils.SharedPrefHelper
import com.apploop.face.changer.app.utils.UtilsCons
import com.apploop.face.changer.app.viewModels.MainActivityViewModel
import com.apploop.face.changer.app.views.IntroduceDialog
import com.apploop.face.changer.app.views.myCreation.MyCreationActivity
import com.apploop.face.changer.app.views.premium.PremiumActivity
import com.apploop.face.changer.app.views.removeBackground.ImageRemoveBgActivity
import org.json.JSONObject


class MainActivity : AppCompatActivity(), View.OnClickListener, MainViewModelInterface,
    CallBackResponseJson, AdapterPathInterface {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainActivityViewModel: MainActivityViewModel
    private lateinit var introduceDialog: IntroduceDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        statusBarColor(R.color.background)
        init()
        hitApis()

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

    private fun hitApis() {
        if (Extension.iplList.isEmpty()) {
            if (isInternetAvailable()) {
                ViewModelVideos(this, this).getIplList()
            }
        }
        if (Extension.pslList.isEmpty()) {
            if (isInternetAvailable()) {
                ViewModelVideos(this, this).getPslVideos()
            }
        }

        if (Extension.africaList.isEmpty()) {
            if (isInternetAvailable()) {
                ViewModelVideos(this, this).getAfricaVideos()
            }
        }

        if (Extension.bplList.isEmpty()) {
            if (isInternetAvailable()) {
                ViewModelVideos(this, this).getBplVideos()
            }
        }
        if (Extension.bblList.isEmpty()) {
            if (isInternetAvailable()) {
                ViewModelVideos(this, this).getBblVideos()
            }
        }
        if (Extension.wtList.isEmpty()) {
            if (isInternetAvailable()) {
                ViewModelVideos(this, this).getWtVideos()
            }
        }
        if (Extension.fifaList.isEmpty()) {
            if (isInternetAvailable()) {
                ViewModelVideos(this, this).getFifaVideos()
            }
        }
        if (Extension.superList.isEmpty()) {
            if (isInternetAvailable()) {
                ViewModelVideos(this, this).getSuperVideos()
            }
        }
    }

    private fun init() {
        binding.ivPremium.setOnClickListener {
            startActivity(Intent(this, PremiumActivity::class.java))
        }

        binding.shimmerFrameLayout.startShimmer()
        AdsManager.getInstance()?.loadNativeAdCallback(
            this,
            binding.frameLayout,
            AdsManager.NativeAdType.REGULAR_TYPE
        ) {
            if (it) {
                binding.shimmerFrameLayout.visibility = View.INVISIBLE
            } else {
                binding.shimmerFrameLayout.visibility = View.INVISIBLE
                binding.frameLayout.visibility = View.INVISIBLE
            }
        }

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

    override fun onSuccessResponse(result: JSONObject) {
        Log.d("dash", "----success-------$result")
    }

    override fun onFailResponse(result: String) {
        Log.d("dash", "---error--------$result")
    }

    override fun onClick() {
        introduceDialog.closeDialog()
        showHeartRateBottomSheet()
    }
}