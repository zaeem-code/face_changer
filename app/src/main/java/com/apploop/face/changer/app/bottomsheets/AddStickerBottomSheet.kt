package com.apploop.face.changer.app.bottomsheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.apploop.face.changer.app.R
import com.apploop.face.changer.app.callBacks.AddStickerBottomSheetViewModelInterface
import com.apploop.face.changer.app.callBacks.AddStickerViewModelInterface
import com.apploop.face.changer.app.databinding.CusAddStickerBottomSheetBinding
import com.apploop.face.changer.app.helpers.EnumClass
import com.apploop.face.changer.app.utils.Extension.initLists
import com.apploop.face.changer.app.utils.Extension.objStickerDetailsBeard
import com.apploop.face.changer.app.utils.Extension.objStickerDetailsGoggle
import com.apploop.face.changer.app.utils.Extension.objStickerDetailsHair
import com.apploop.face.changer.app.utils.Extension.objStickerDetailsHat
import com.apploop.face.changer.app.utils.Extension.objStickerDetailsMustache
import com.apploop.face.changer.app.utils.Extension.objStickerDetailsTie
import com.apploop.face.changer.app.viewModels.AddStickerBottomSheetViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.apploop.face.changer.app.views.MenPhotoScreen.MenPhotoActivity

class AddStickerBottomSheet(
    var activity: MenPhotoActivity,
    val listener: AddStickerBottomSheetViewModelInterface
) : BottomSheetDialogFragment(),
    AddStickerViewModelInterface {

    private lateinit var binding: CusAddStickerBottomSheetBinding
    private lateinit var addStickerBottomSheetViewModel: AddStickerBottomSheetViewModel
    private var selectSuitActivity = activity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.cus_add_sticker_bottom_sheet,
            container,
            false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        addStickerBottomSheetViewModel = AddStickerBottomSheetViewModel(this)
        binding.addStickerBottomSheetViewModel = addStickerBottomSheetViewModel

        selectSuitActivity.initLists()
    }


    override fun getTheme(): Int {
        return R.style.SheetDialog
    }

    override fun onAddStickerButtonClicks(type: EnumClass) {
        when (type) {
            EnumClass.BEARD -> {
                dismiss()
                ShowStickersBottomSheet(
                    selectSuitActivity,
                    objStickerDetailsBeard,
                    listener
                ).apply {
                    show(selectSuitActivity.supportFragmentManager, tag)
                }
            }
            EnumClass.GOGGLES -> {
                dismiss()
                ShowStickersBottomSheet(
                    selectSuitActivity,
                    objStickerDetailsGoggle,
                    listener
                ).apply {
                    show(selectSuitActivity.supportFragmentManager, tag)
                }
            }
            EnumClass.HAIR -> {
                dismiss()
                ShowStickersBottomSheet(
                    selectSuitActivity,
                    objStickerDetailsHair,
                    listener
                ).apply {
                    show(selectSuitActivity.supportFragmentManager, tag)
                }
            }
            EnumClass.HAT -> {
                dismiss()
                ShowStickersBottomSheet(
                    selectSuitActivity,
                    objStickerDetailsHat,
                    listener
                ).apply {
                    show(selectSuitActivity.supportFragmentManager, tag)
                }
            }
            EnumClass.MUSTACHE -> {
                dismiss()
                ShowStickersBottomSheet(
                    selectSuitActivity,
                    objStickerDetailsMustache,
                    listener
                ).apply {
                    show(selectSuitActivity.supportFragmentManager, tag)
                }
            }
            EnumClass.TIE -> {
                dismiss()
                ShowStickersBottomSheet(
                    selectSuitActivity,
                    objStickerDetailsTie,
                    listener
                ).apply {
                    show(selectSuitActivity.supportFragmentManager, tag)
                }
            }
            else -> {}
        }
    }
}