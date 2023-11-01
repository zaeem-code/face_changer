package com.apploop.face.changer.app.views.category

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.apploop.face.changer.app.R
import com.apploop.face.changer.app.api.apiRespoInterfaces.CallBackResponseJson
import com.apploop.face.changer.app.api.viewModel.ViewModelVideos
import com.apploop.face.changer.app.databinding.ActivityCricketBinding
import com.apploop.face.changer.app.manager.AdsManager
import com.apploop.face.changer.app.manager.AnalyticsManager
import com.apploop.face.changer.app.models.SuitList
import com.apploop.face.changer.app.utils.Extension.isInternetAvailable
import com.apploop.face.changer.app.utils.Extension.statusBarColor
import com.apploop.face.changer.app.views.ShowAssertsActivity
import org.json.JSONObject
import java.util.ArrayList

class CricketActivity : AppCompatActivity(),
    com.apploop.face.changer.app.api.apiRespoInterfaces.CallBackResponseJson {

    private lateinit var binding: ActivityCricketBinding
    var iplList: ArrayList<SuitList?> = ArrayList()
    var pslList: ArrayList<SuitList?> = ArrayList()
    var bplList: ArrayList<SuitList?> = ArrayList()
    var bblList: ArrayList<SuitList?> = ArrayList()
    var wtList: ArrayList<SuitList?> = ArrayList()
    var fifaList: ArrayList<SuitList?> = ArrayList()
    var superList: ArrayList<SuitList?> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cricket)
        statusBarColor(R.color.background)


        if (!isInternetAvailable()) {
            binding.lvIpl.visibility = View.GONE
            binding.lvPsl.visibility = View.GONE
            binding.lvBpl.visibility = View.GONE
            binding.lvBbl.visibility = View.GONE
            binding.lvWt.visibility = View.GONE

        }



        binding.shimmerFrameLayout.startShimmer()
        AdsManager.Companion.instance!!.showNativeAd(
            binding.frameLayout3,
            binding.frameLayout3,
            layoutInflater,
            R.layout.ad_media
        )
        {
            if (it) {
                binding.shimmerFrameLayout.visibility = View.INVISIBLE
            } else {
                binding.shimmerFrameLayout.visibility = View.GONE
                binding.frameLayout3.visibility = View.GONE
            }
        }

        binding.ivBack.setOnClickListener {
            finish()
        }


        binding.lvIpl.setOnClickListener {

            com.apploop.face.changer.app.manager.AnalyticsManager.getInstance().sendAnalytics("CricketActivity","ipl")

            if (iplList.isEmpty()) {
                if (isInternetAvailable()) {
                    ViewModelVideos(this, this).getIplList()
                }
            }
            val intent = Intent(this, ShowAssertsActivity::class.java)
            intent.putExtra("listType", "ipl")
            startActivity(intent)
        }
        binding.lvBbl.setOnClickListener {

            com.apploop.face.changer.app.manager.AnalyticsManager.getInstance().sendAnalytics("CricketActivity","bbl")

            if (bblList.isEmpty()) {
                if (isInternetAvailable()) {
                    ViewModelVideos(this, this).getBblVideos()
                }
            }
            val intent = Intent(this, ShowAssertsActivity::class.java)
            intent.putExtra("listType", "bbl")
            startActivity(intent)
        }
        binding.lvBpl.setOnClickListener {

            com.apploop.face.changer.app.manager.AnalyticsManager.getInstance().sendAnalytics("CricketActivity","bpl")

            if (bplList.isEmpty()) {
                if (isInternetAvailable()) {
                    ViewModelVideos(this, this).getBplVideos()
                }
            }
            val intent = Intent(this, ShowAssertsActivity::class.java)
            intent.putExtra("listType", "bpl")
            startActivity(intent)
        }

        binding.lvPsl.setOnClickListener {

            com.apploop.face.changer.app.manager.AnalyticsManager.getInstance().sendAnalytics("CricketActivity","psl")

            if (pslList.isEmpty()) {
                if (isInternetAvailable()) {
                    ViewModelVideos(this, this).getPslVideos()
                }
            }
            val intent = Intent(this, ShowAssertsActivity::class.java)
            intent.putExtra("listType", "psl")
            startActivity(intent)
        }
        binding.lvWt.setOnClickListener {
            com.apploop.face.changer.app.manager.AnalyticsManager.getInstance().sendAnalytics("CricketActivity","world_cricket")


            if (wtList.isEmpty()) {
                if (isInternetAvailable()) {
                    ViewModelVideos(this, this).getWtVideos()
                }
            }
            val intent = Intent(this, ShowAssertsActivity::class.java)
            intent.putExtra("listType", "wt")
            startActivity(intent)
        }


    }

    override fun onSuccessResponse(result: JSONObject) {
    }

    override fun onFailResponse(result: String) {
    }
}