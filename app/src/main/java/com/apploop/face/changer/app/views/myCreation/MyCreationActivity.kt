package com.apploop.face.changer.app.views.myCreation

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.apploop.face.changer.app.R
import com.apploop.face.changer.app.callBacks.MyCreationInterface
import com.apploop.face.changer.app.databinding.ActivityMyCreationBinding
import com.apploop.face.changer.app.manager.AdsManager
import com.apploop.face.changer.app.manager.OnAdLoaded
import com.apploop.face.changer.app.utils.Extension.fetchImage
import com.apploop.face.changer.app.utils.Extension.imageGallery
import com.apploop.face.changer.app.utils.Extension.statusBarColor
import com.apploop.face.changer.app.views.adapters.MyCreationAdapter

class MyCreationActivity : AppCompatActivity(), MyCreationInterface {

    private lateinit var binding: ActivityMyCreationBinding
    private lateinit var myCreationAdapter: MyCreationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_creation)
        statusBarColor(R.color.background)
    }

    private fun init() {
        binding.shimmerFrameLayout.startShimmer()
        AdsManager.getInstance().loadNativeAdCallback(
            this,
            binding.frameLayout,
            AdsManager.NativeAdType.MEDIUM_TYPE
        )
        {
            if (it) {
                binding.shimmerFrameLayout.visibility = View.INVISIBLE
            } else {
                binding.shimmerFrameLayout.visibility = View.GONE
                binding.frameLayout.visibility = View.GONE
            }
        }
        fetchImage()
        binding.rvMyCreations.layoutManager = GridLayoutManager(this, 2)
        myCreationAdapter = MyCreationAdapter(imageGallery, this, this)
        binding.rvMyCreations.adapter = myCreationAdapter

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onItemClick(path: String) {
        val intent = Intent(this, ViewSavedImageActivity::class.java)
        intent.putExtra("fullImagePath", path)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        init()
    }


    override fun onBackPressed() {
        finish()
    }

}