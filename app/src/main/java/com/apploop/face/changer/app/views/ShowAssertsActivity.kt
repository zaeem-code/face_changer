package com.apploop.face.changer.app.views

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.apploop.face.changer.app.R
import com.apploop.face.changer.app.bottomsheets.CustomBSFragment
import com.apploop.face.changer.app.callBacks.AdapterPathInterface
import com.apploop.face.changer.app.callBacks.SuitBottomSheetViewModelInterface
import com.apploop.face.changer.app.callBacks.SuitBottomSheetViewModelInterfaceApi
import com.apploop.face.changer.app.databinding.ActivityShowAssertsAcitivtyBinding
import com.apploop.face.changer.app.models.ObjSuitImageOption
import com.apploop.face.changer.app.models.SuitList
import com.apploop.face.changer.app.utils.Extension
import com.apploop.face.changer.app.utils.Extension.africaList
import com.apploop.face.changer.app.utils.Extension.bblList
import com.apploop.face.changer.app.utils.Extension.bodyBuildersList
import com.apploop.face.changer.app.utils.Extension.bplList
import com.apploop.face.changer.app.utils.Extension.christmasList
import com.apploop.face.changer.app.utils.Extension.fifaList
import com.apploop.face.changer.app.utils.Extension.iplList
import com.apploop.face.changer.app.utils.Extension.kurtaList
import com.apploop.face.changer.app.utils.Extension.objSuitOptions1
import com.apploop.face.changer.app.utils.Extension.objSuitOptions2
import com.apploop.face.changer.app.utils.Extension.objSuitOptions3
import com.apploop.face.changer.app.utils.Extension.objSuitOptions4
import com.apploop.face.changer.app.utils.Extension.objSuitOptions5
import com.apploop.face.changer.app.utils.Extension.pslList
import com.apploop.face.changer.app.utils.Extension.statusBarColor
import com.apploop.face.changer.app.utils.Extension.superList
import com.apploop.face.changer.app.utils.Extension.tShirtList
import com.apploop.face.changer.app.utils.Extension.wtList
import com.apploop.face.changer.app.views.adapters.SuitsBottomSheetAdapter
import com.apploop.face.changer.app.views.adapters.SuitsBottomSheetAdapter1

class ShowAssertsActivity : AppCompatActivity(),
    SuitBottomSheetViewModelInterface, AdapterPathInterface, SuitBottomSheetViewModelInterfaceApi {

    private lateinit var binding: ActivityShowAssertsAcitivtyBinding
    private lateinit var suitsBottomSheetAdapter: SuitsBottomSheetAdapter
    private lateinit var suitsBottomSheetAdapter1: SuitsBottomSheetAdapter1
    private var list: List<ObjSuitImageOption> = ArrayList()
    private var list1: List<SuitList> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_show_asserts_acitivty)
        statusBarColor(R.color.background)
        init()
    }

    private fun init() {
//        binding.shimmerFrameLayout.startShimmer()
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
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        when (intent.getStringExtra("listType")) {
            "christmas" -> {
                list = christmasList
            }
            "blazer" -> {
                list = objSuitOptions1
            }
            "suits" -> {
                list = objSuitOptions4
            }
            "formal" -> {
                list = objSuitOptions2
            }
            "jacket" -> {
                list = objSuitOptions3
            }
            "police" -> {
                list = objSuitOptions5
            }
            "kurta" -> {
                list = kurtaList
            }
            "bodybuilder" -> {
                list = bodyBuildersList
            }
            "tshirt" -> {
                list = tShirtList
            }
            "ipl" -> {
                list1 = iplList as List<SuitList>
                binding.rvSuits.layoutManager = GridLayoutManager(this, 2)
                suitsBottomSheetAdapter1 = SuitsBottomSheetAdapter1(list1, this, this, this, this)
                binding.rvSuits.adapter = suitsBottomSheetAdapter1
                return
            }
            "psl" -> {
                list1 = pslList as List<SuitList>
                binding.rvSuits.layoutManager = GridLayoutManager(this, 2)
                suitsBottomSheetAdapter1 = SuitsBottomSheetAdapter1(list1, this, this, this, this)
                binding.rvSuits.adapter = suitsBottomSheetAdapter1
                return
            }

            "AS" -> {
                list1 = africaList as List<SuitList>
                binding.rvSuits.layoutManager = GridLayoutManager(this, 2)
                suitsBottomSheetAdapter1 = SuitsBottomSheetAdapter1(list1, this, this, this, this)
                binding.rvSuits.adapter = suitsBottomSheetAdapter1
                return
            }
            "bpl" -> {
                list1 = bplList as List<SuitList>
                binding.rvSuits.layoutManager = GridLayoutManager(this, 2)
                suitsBottomSheetAdapter1 = SuitsBottomSheetAdapter1(list1, this, this, this, this)
                binding.rvSuits.adapter = suitsBottomSheetAdapter1
                return
            }
            "bbl" -> {
                list1 = bblList as List<SuitList>
                binding.rvSuits.layoutManager = GridLayoutManager(this, 2)
                suitsBottomSheetAdapter1 = SuitsBottomSheetAdapter1(list1, this, this, this, this)
                binding.rvSuits.adapter = suitsBottomSheetAdapter1
                return
            }
            "wt" -> {
                list1 = wtList as List<SuitList>
                binding.rvSuits.layoutManager = GridLayoutManager(this, 2)
                suitsBottomSheetAdapter1 = SuitsBottomSheetAdapter1(list1, this, this, this, this)
                binding.rvSuits.adapter = suitsBottomSheetAdapter1
                return
            }
            "fifa" -> {
                list1 = fifaList as List<SuitList>
                binding.rvSuits.layoutManager = GridLayoutManager(this, 2)
                suitsBottomSheetAdapter1 = SuitsBottomSheetAdapter1(list1, this, this, this, this)
                binding.rvSuits.adapter = suitsBottomSheetAdapter1
                return
            }
            "super" -> {
                list1 = superList as List<SuitList>
                binding.rvSuits.layoutManager = GridLayoutManager(this, 2)
                suitsBottomSheetAdapter1 = SuitsBottomSheetAdapter1(list1, this, this, this, this)
                binding.rvSuits.adapter = suitsBottomSheetAdapter1
                return
            }
        }

        binding.rvSuits.layoutManager = GridLayoutManager(this, 2)
        suitsBottomSheetAdapter = SuitsBottomSheetAdapter(list, this, this, this)
        binding.rvSuits.adapter = suitsBottomSheetAdapter
    }

    override fun onSuitBottomSheetButtonClicks(path: String) {
        val filePath = "file:///android_asset/$path"
        Extension.suitPath = filePath
        try {
//            OpenCameraBottomSheet(this@ShowAssertsActivity).apply {
//                show(this@ShowAssertsActivity.supportFragmentManager, tag)
//            }
            showHeartRateBottomSheet()
        } catch (e: RuntimeException) {
            e.printStackTrace()
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

    }

    override fun onSuitBottomSheetButtonClicksApi(path: String) {
        Extension.suitPath = path
        try {
            showHeartRateBottomSheet()
        } catch (e: RuntimeException) {
            e.printStackTrace()
        }
    }
}