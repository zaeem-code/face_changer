package com.apploop.face.changer.app.viewModels

import androidx.lifecycle.ViewModel
import com.apploop.face.changer.app.callBacks.AddStickerViewModelInterface
import com.apploop.face.changer.app.helpers.EnumClass

class AddStickerBottomSheetViewModel(addStickerCallBack: AddStickerViewModelInterface) :
    ViewModel() {

    private var addStickerViewModelInterface: AddStickerViewModelInterface = addStickerCallBack

    fun onBeardClick() {
        addStickerViewModelInterface.onAddStickerButtonClicks(EnumClass.BEARD)
    }

    fun onGogglesClick() {
        addStickerViewModelInterface.onAddStickerButtonClicks(EnumClass.GOGGLES)
    }

    fun onHairClick() {
        addStickerViewModelInterface.onAddStickerButtonClicks(EnumClass.HAIR)
    }

    fun onHatClick() {
        addStickerViewModelInterface.onAddStickerButtonClicks(EnumClass.HAT)
    }

    fun onMustacheClick() {
        addStickerViewModelInterface.onAddStickerButtonClicks(EnumClass.MUSTACHE)
    }

    fun onTieClick() {
        addStickerViewModelInterface.onAddStickerButtonClicks(EnumClass.TIE)
    }
}