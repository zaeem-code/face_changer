package com.apploop.face.changer.app.bottomsheets

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.apploop.face.changer.app.R
import com.apploop.face.changer.app.callBacks.AdapterPathInterface
import com.apploop.face.changer.app.callBacks.AddStickerBottomSheetViewModelInterface
import com.apploop.face.changer.app.databinding.CusSuitBottomSheetBinding
import com.apploop.face.changer.app.models.ObjStickerImageDetail
import com.apploop.face.changer.app.views.adapters.ShowStickersBottomSheetAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.apploop.face.changer.app.manager.AdsManager

class ShowStickersBottomSheet(
    val activity: Activity,
    val list: List<ObjStickerImageDetail>,
    val listener: AddStickerBottomSheetViewModelInterface
) :
    BottomSheetDialogFragment(), AdapterPathInterface {

    private lateinit var binding: CusSuitBottomSheetBinding
    private lateinit var suitsBottomSheetAdapter: ShowStickersBottomSheetAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.cus_suit_bottom_sheet,
            container,
            false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AdsManager.instance?.showNativeAd(
            binding.frameLayout,
            binding.frameLayout,
            layoutInflater,
            R.layout.ad_with_media
        ) {
            if (it) {
                binding.shimmerFrameLayout.visibility = View.INVISIBLE
            } else {
                binding.shimmerFrameLayout.visibility = View.INVISIBLE
                binding.frameLayout.visibility = View.INVISIBLE
            }
        }

        binding.rvSuits.layoutManager = GridLayoutManager(activity, 3)
        suitsBottomSheetAdapter = ShowStickersBottomSheetAdapter(list, activity, listener, this)
        binding.rvSuits.adapter = suitsBottomSheetAdapter
    }

    override fun getTheme(): Int {
        return R.style.SheetDialog
    }

    override fun onClick() {
        dismiss()
    }

}