package com.apploop.face.changer.app.bottomsheets

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.apploop.face.changer.app.R
import com.apploop.face.changer.app.callBacks.AdapterPathInterface
import com.apploop.face.changer.app.callBacks.BackgroundBottomSheetInterface
import com.apploop.face.changer.app.databinding.CusSuitBottomSheetBinding
import com.apploop.face.changer.app.models.ObjSuitImageOption
import com.apploop.face.changer.app.views.adapters.BackGroundBottomSheetAdapter
import java.util.*


class ShowBackgroundsBottomSheet(
    val activity: Activity,
    val list: List<ObjSuitImageOption>,
    val listener: BackgroundBottomSheetInterface
) :
    BottomSheetDialogFragment(), AdapterPathInterface {

    private lateinit var binding: CusSuitBottomSheetBinding
    private lateinit var suitsBottomSheetAdapter: BackGroundBottomSheetAdapter

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvSuits.layoutManager = GridLayoutManager(activity, 2)
        suitsBottomSheetAdapter = BackGroundBottomSheetAdapter(list, activity, listener, this)
        binding.rvSuits.adapter = suitsBottomSheetAdapter
    }

    override fun getTheme(): Int {
        return R.style.SheetDialog
    }

    override fun onClick() {
        dismiss()
    }

}