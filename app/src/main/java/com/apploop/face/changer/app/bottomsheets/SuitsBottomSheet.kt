package com.apploop.face.changer.app.bottomsheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.apploop.face.changer.app.R
import com.apploop.face.changer.app.callBacks.AdapterPathInterface
import com.apploop.face.changer.app.callBacks.SuitBottomSheetViewModelInterface
import com.apploop.face.changer.app.databinding.CusSuitBottomSheetBinding
import com.apploop.face.changer.app.models.ObjSuitImageOption
import com.apploop.face.changer.app.viewModels.SuitBottomSheetViewModel
import com.apploop.face.changer.app.views.adapters.SuitsBottomSheetAdapter
import com.apploop.face.changer.app.views.editor.SelectSuitActivity

class SuitsBottomSheet(
    val activity: SelectSuitActivity,
    val list: List<ObjSuitImageOption>,
    val listener: SuitBottomSheetViewModelInterface
) :
    BottomSheetDialogFragment(), AdapterPathInterface {

    private lateinit var binding: CusSuitBottomSheetBinding
    private lateinit var suitBottomSheetViewModel: SuitBottomSheetViewModel
    private lateinit var suitsBottomSheetAdapter: SuitsBottomSheetAdapter

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



        suitBottomSheetViewModel = SuitBottomSheetViewModel(listener)
        binding.suitBottomSheetViewModel = suitBottomSheetViewModel



        binding.rvSuits.layoutManager = GridLayoutManager(activity, 2)
        suitsBottomSheetAdapter = SuitsBottomSheetAdapter(list, activity, listener, this)
        binding.rvSuits.adapter = suitsBottomSheetAdapter
    }

    override fun getTheme(): Int {
        return R.style.SheetDialog
    }

    override fun onClick() {
        dismiss()
    }

}