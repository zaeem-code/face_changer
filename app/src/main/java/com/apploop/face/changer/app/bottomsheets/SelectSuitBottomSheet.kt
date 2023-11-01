package com.apploop.face.changer.app.bottomsheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.apploop.face.changer.app.R
import com.apploop.face.changer.app.callBacks.SelectSuitBottomSheetViewModelInterface
import com.apploop.face.changer.app.callBacks.SuitBottomSheetViewModelInterface
import com.apploop.face.changer.app.databinding.CusSelectSuitBottomSheetBinding
import com.apploop.face.changer.app.helpers.EnumClass
import com.apploop.face.changer.app.utils.Extension.bodyBuildersList
import com.apploop.face.changer.app.utils.Extension.christmasList
import com.apploop.face.changer.app.utils.Extension.initSuitsLists
import com.apploop.face.changer.app.utils.Extension.kurtaList
import com.apploop.face.changer.app.utils.Extension.objSuitOptions1
import com.apploop.face.changer.app.utils.Extension.objSuitOptions2
import com.apploop.face.changer.app.utils.Extension.objSuitOptions3
import com.apploop.face.changer.app.utils.Extension.objSuitOptions4
import com.apploop.face.changer.app.utils.Extension.objSuitOptions5
import com.apploop.face.changer.app.utils.Extension.tShirtList
import com.apploop.face.changer.app.viewModels.SelectSuitBottomSheetViewModel
import com.apploop.face.changer.app.views.editor.SelectSuitActivity

class SelectSuitBottomSheet(
    var activity: SelectSuitActivity,
    val listener: SuitBottomSheetViewModelInterface
) : BottomSheetDialogFragment(),
    SelectSuitBottomSheetViewModelInterface {

    private lateinit var binding: CusSelectSuitBottomSheetBinding
    private lateinit var selectSuitBottomSheetViewModel: SelectSuitBottomSheetViewModel
    private var selectSuitActivity = activity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.cus_select_suit_bottom_sheet,
            container,
            false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        selectSuitBottomSheetViewModel = SelectSuitBottomSheetViewModel(this)
        binding.selectSuitBottomSheetViewModel = selectSuitBottomSheetViewModel
        activity.initSuitsLists()

    }

    override fun getTheme(): Int {
        return R.style.SheetDialog
    }

    override fun onSelectSuitBottomSheetButtonClicks(type: EnumClass) {
        try {
            when (type) {
                EnumClass.BLAZER -> {
                    dismiss()
                    SuitsBottomSheet(selectSuitActivity, objSuitOptions1, listener).apply {
                        show(selectSuitActivity.supportFragmentManager, tag)
                    }
                }
                EnumClass.Police -> {
                    dismiss()
                    SuitsBottomSheet(selectSuitActivity, objSuitOptions5, listener).apply {
                        show(selectSuitActivity.supportFragmentManager, tag)
                    }
                }
                EnumClass.SUIT -> {
                    dismiss()
                    SuitsBottomSheet(selectSuitActivity, objSuitOptions4, listener).apply {
                        show(selectSuitActivity.supportFragmentManager, tag)
                    }
                }
                EnumClass.FORMAL -> {
                    dismiss()
                    SuitsBottomSheet(selectSuitActivity, objSuitOptions2, listener).apply {
                        show(selectSuitActivity.supportFragmentManager, tag)
                    }
                }
                EnumClass.JACKET -> {
                    dismiss()
                    SuitsBottomSheet(selectSuitActivity, objSuitOptions3, listener).apply {
                        show(selectSuitActivity.supportFragmentManager, tag)
                    }
                }
                EnumClass.KURTA -> {
                    dismiss()
                    SuitsBottomSheet(selectSuitActivity, kurtaList, listener).apply {
                        show(selectSuitActivity.supportFragmentManager, tag)
                    }
                }
                EnumClass.BODY_BUILDER -> {
                    dismiss()
                    SuitsBottomSheet(selectSuitActivity, bodyBuildersList, listener).apply {
                        show(selectSuitActivity.supportFragmentManager, tag)
                    }
                }
                EnumClass.T_SHIRT -> {
                    dismiss()
                    SuitsBottomSheet(selectSuitActivity, tShirtList, listener).apply {
                        show(selectSuitActivity.supportFragmentManager, tag)
                    }
                }
                EnumClass.CHRISTMAS -> {
                    dismiss()
                    SuitsBottomSheet(selectSuitActivity, christmasList, listener).apply {
                        show(selectSuitActivity.supportFragmentManager, tag)
                    }
                }
                else -> {}
            }
        } catch (e: Exception) {
            e.localizedMessage
        }
    }
}