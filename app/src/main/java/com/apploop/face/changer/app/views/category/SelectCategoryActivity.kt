package com.apploop.face.changer.app.views.category

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.apploop.face.changer.app.R

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
    SuitBottomSheetViewModelInterface {

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
          EnumClass.BACK -> {
                onBackPressed()
            }
            else -> {}
        }
    }

    override fun onSuitBottomSheetButtonClicks(path: String) {
    }


}