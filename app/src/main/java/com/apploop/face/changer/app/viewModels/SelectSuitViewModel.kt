package com.apploop.face.changer.app.viewModels

import com.apploop.face.changer.app.callBacks.SelectSuitViewModelInterface
import com.apploop.face.changer.app.helpers.EnumClass

class SelectSuitViewModel(selectSuitCallBack: SelectSuitViewModelInterface) {

    private var selectSuitViewModelInterface: SelectSuitViewModelInterface = selectSuitCallBack

    fun flipSuit() {
        selectSuitViewModelInterface.onSelectSuitButtonClicks(EnumClass.FLIP_SUIT)
    }

    fun flipImage() {
        selectSuitViewModelInterface.onSelectSuitButtonClicks(EnumClass.FLIP_IMAGE)
    }

    fun onBack() {
        selectSuitViewModelInterface.onSelectSuitButtonClicks(EnumClass.BACK)
    }

    fun onSuitClick() {
        selectSuitViewModelInterface.onSelectSuitButtonClicks(EnumClass.SUIT)
    }

    fun onEditClick() {
        selectSuitViewModelInterface.onSelectSuitButtonClicks(EnumClass.EDIT)
    }

    fun onDoneClick() {
        selectSuitViewModelInterface.onSelectSuitButtonClicks(EnumClass.DONE)
    }

    fun onPhotoClick() {
        selectSuitViewModelInterface.onSelectSuitButtonClicks(EnumClass.PHOTO)
    }

    fun onMoveSuitClick() {
        selectSuitViewModelInterface.onSelectSuitButtonClicks(EnumClass.MOVE_SUIT)
    }

}