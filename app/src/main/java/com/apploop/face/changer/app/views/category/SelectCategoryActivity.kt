package com.apploop.face.changer.app.views.category

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.apploop.face.changer.app.R
import com.apploop.face.changer.app.api.apiRespoInterfaces.CallBackResponseJson
import com.apploop.face.changer.app.api.viewModel.ViewModelVideos
import com.apploop.face.changer.app.callBacks.CategoryModelInterface
import com.apploop.face.changer.app.callBacks.SuitBottomSheetViewModelInterface
import com.apploop.face.changer.app.databinding.ActivitySelectCategoryBinding
import com.apploop.face.changer.app.helpers.EnumClass
import com.apploop.face.changer.app.manager.AnalyticsManager
import com.apploop.face.changer.app.utils.Extension.africaList
import com.apploop.face.changer.app.utils.Extension.fifaList
import com.apploop.face.changer.app.utils.Extension.initSuitsLists
import com.apploop.face.changer.app.utils.Extension.isInternetAvailable
import com.apploop.face.changer.app.utils.Extension.statusBarColor
import com.apploop.face.changer.app.utils.Extension.superList
import com.apploop.face.changer.app.viewModels.CategoryViewModel
import com.apploop.face.changer.app.views.ShowAssertsActivity
import org.json.JSONObject

class SelectCategoryActivity : AppCompatActivity(), CategoryModelInterface,
    SuitBottomSheetViewModelInterface,
    com.apploop.face.changer.app.api.apiRespoInterfaces.CallBackResponseJson {

    private lateinit var binding: ActivitySelectCategoryBinding
    private lateinit var categoryViewModel: CategoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_category)
        statusBarColor(R.color.background)



        init()
    }

    private fun init() {

        if (!isInternetAvailable()) {
            binding.lvIpl.visibility = View.GONE
            binding.lvFifa.visibility = View.GONE
            binding.lvSuper.visibility = View.GONE
            binding.lvAfrican.visibility = View.GONE
        }


//        binding.shimmerFrameLayout.startShimmer()
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
//        binding.shimmerFrameLayout1.startShimmer()
//        AdsManager.Companion.instance!!.showNativeAd(
//            binding.frameLayout1,
//            binding.frameLayout1,
//            layoutInflater,
//            R.layout.ad_media
//        )
//        {
//            if (it) {
//                binding.shimmerFrameLayout1.visibility = View.INVISIBLE
//            } else {
//                binding.shimmerFrameLayout1.visibility = View.GONE
//                binding.frameLayout1.visibility = View.GONE
//            }
//        }

//        binding.shimmerFrameLayout2.startShimmer()
//        AdsManager.Companion.instance!!.showNativeAd(
//            binding.frameLayout2,
//            binding.frameLayout2,
//            layoutInflater,
//            R.layout.ad_media
//        )
//        {
//            if (it) {
//                binding.shimmerFrameLayout2.visibility = View.INVISIBLE
//            } else {
//                binding.shimmerFrameLayout2.visibility = View.GONE
//                binding.frameLayout2.visibility = View.GONE
//            }
//        }

//        binding.shimmerFrameLayout3.startShimmer()
//        AdsManager.Companion.instance!!.showNativeAd(
//            binding.frameLayout3,
//            binding.frameLayout3,
//            layoutInflater,
//            R.layout.ad_media
//        )
//        {
//            if (it) {
//                binding.shimmerFrameLayout3.visibility = View.INVISIBLE
//            } else {
//                binding.shimmerFrameLayout3.visibility = View.GONE
//                binding.frameLayout3.visibility = View.GONE
//            }
//        }
        categoryViewModel = CategoryViewModel(this)
        binding.categoryViewModel = categoryViewModel
        initSuitsLists()
    }

    override fun onCategoryButtonClicks(type: EnumClass) {
        when (type) {
            EnumClass.CARD -> {
                com.apploop.face.changer.app.manager.AnalyticsManager.getInstance().sendAnalytics("SelectCategoryActivity","christmas")
                val intent =
                    Intent(this@SelectCategoryActivity, ShowAssertsActivity::class.java)
                intent.putExtra("listType", "christmas")
                startActivity(intent)


            }
            EnumClass.BLAZER -> {
                com.apploop.face.changer.app.manager.AnalyticsManager.getInstance().sendAnalytics("SelectCategoryActivity","blazer")

                val intent = Intent(this, ShowAssertsActivity::class.java)
                intent.putExtra("listType", "blazer")
                startActivity(intent)
            }
            EnumClass.SUIT -> {

                com.apploop.face.changer.app.manager.AnalyticsManager.getInstance().sendAnalytics("SelectCategoryActivity","suits")

                val intent = Intent(this, ShowAssertsActivity::class.java)
                intent.putExtra("listType", "suits")
                startActivity(intent)
            }
            EnumClass.FORMAL -> {

                com.apploop.face.changer.app.manager.AnalyticsManager.getInstance().sendAnalytics("SelectCategoryActivity","formal")

                val intent = Intent(this, ShowAssertsActivity::class.java)
                intent.putExtra("listType", "formal")
                startActivity(intent)
            }
            EnumClass.JACKET -> {

                com.apploop.face.changer.app.manager.AnalyticsManager.getInstance().sendAnalytics("SelectCategoryActivity","jacket")

                val intent = Intent(this, ShowAssertsActivity::class.java)
                intent.putExtra("listType", "jacket")
                startActivity(intent)
            }
            EnumClass.Police -> {

                com.apploop.face.changer.app.manager.AnalyticsManager.getInstance().sendAnalytics("SelectCategoryActivity","police")

                val intent = Intent(this, ShowAssertsActivity::class.java)
                intent.putExtra("listType", "police")
                startActivity(intent)
            }
            EnumClass.KURTA -> {

                com.apploop.face.changer.app.manager.AnalyticsManager.getInstance().sendAnalytics("SelectCategoryActivity","kurta")

                val intent = Intent(this, ShowAssertsActivity::class.java)
                intent.putExtra("listType", "kurta")
                startActivity(intent)
            }
            EnumClass.BODY_BUILDER -> {
                com.apploop.face.changer.app.manager.AnalyticsManager.getInstance().sendAnalytics("SelectCategoryActivity","bodybuilder")


                val intent = Intent(this, ShowAssertsActivity::class.java)
                intent.putExtra("listType", "bodybuilder")
                startActivity(intent)
            }
            EnumClass.T_SHIRT -> {
                com.apploop.face.changer.app.manager.AnalyticsManager.getInstance().sendAnalytics("SelectCategoryActivity","tshirt")


                val intent = Intent(this, ShowAssertsActivity::class.java)
                intent.putExtra("listType", "tshirt")
                startActivity(intent)
            }
            EnumClass.IPL -> {

                com.apploop.face.changer.app.manager.AnalyticsManager.getInstance().sendAnalytics("SelectCategoryActivity","CricketActivity")

                val intent = Intent(this, CricketActivity::class.java)
                startActivity(intent)


            }

            EnumClass.AFRICA -> {
                com.apploop.face.changer.app.manager.AnalyticsManager.getInstance().sendAnalytics("SelectCategoryActivity","AFRICA")


                if (africaList.isEmpty()) {
                    if (isInternetAvailable()) {
                        ViewModelVideos(this, this).getAfricaVideos()
                    }
                }
                val intent = Intent(this, ShowAssertsActivity::class.java)
                intent.putExtra("listType", "AS")
                startActivity(intent)
            }


//            EnumClass.PSL -> {
//                if (pslList.isEmpty()) {
//                    if (isInternetAvailable()) {
//                        ViewModelVideos(this, this).getPslVideos()
//                    }
//                }
//                val intent = Intent(this, ShowAssertsActivity::class.java)
//                intent.putExtra("listType", "psl")
//                startActivity(intent)
//            }
//            EnumClass.BPL -> {
//                if (bplList.isEmpty()) {
//                    if (isInternetAvailable()) {
//                        ViewModelVideos(this, this).getBplVideos()
//                    }
//                }
//                val intent = Intent(this, ShowAssertsActivity::class.java)
//                intent.putExtra("listType", "bpl")
//                startActivity(intent)
//            }
//            EnumClass.BBL -> {
//                if (bblList.isEmpty()) {
//                    if (isInternetAvailable()) {
//                        ViewModelVideos(this, this).getBblVideos()
//                    }
//                }
//                val intent = Intent(this, ShowAssertsActivity::class.java)
//                intent.putExtra("listType", "bbl")
//                startActivity(intent)
//            }
//            EnumClass.WT -> {
//                if (wtList.isEmpty()) {
//                    if (isInternetAvailable()) {
//                        ViewModelVideos(this, this).getWtVideos()
//                    }
//                }
//                val intent = Intent(this, ShowAssertsActivity::class.java)
//                intent.putExtra("listType", "wt")
//                startActivity(intent)
//            }
            EnumClass.FIFA -> {

                com.apploop.face.changer.app.manager.AnalyticsManager.getInstance().sendAnalytics("SelectCategoryActivity","FIFA")

                if (fifaList.isEmpty()) {
                    if (isInternetAvailable()) {
                        ViewModelVideos(this, this).getFifaVideos()
                    }
                }
                val intent = Intent(this, ShowAssertsActivity::class.java)
                intent.putExtra("listType", "fifa")
                startActivity(intent)
            }
            EnumClass.SUPER -> {

                com.apploop.face.changer.app.manager.AnalyticsManager.getInstance().sendAnalytics("SelectCategoryActivity","SUPER")


                if (superList.isEmpty()) {
                    if (isInternetAvailable()) {
                        ViewModelVideos(this, this).getSuperVideos()
                    }
                }
                val intent = Intent(this, ShowAssertsActivity::class.java)
                intent.putExtra("listType", "super")
                startActivity(intent)
            }

            EnumClass.BACK -> {
                onBackPressed()
            }
            else -> {}
        }
    }

    override fun onSuitBottomSheetButtonClicks(path: String) {
//        val filePath = "file:///android_asset/$path"
//        suitPath = filePath
//        OpenCameraBottomSheet(this).apply {
//            show(supportFragmentManager, tag)
//        }
    }

    override fun onSuccessResponse(result: JSONObject) {

    }

    override fun onFailResponse(result: String) {

    }
}