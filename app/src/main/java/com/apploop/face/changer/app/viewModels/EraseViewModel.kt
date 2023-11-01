package com.apploop.face.changer.app.viewModels

import com.apploop.face.changer.app.callBacks.EraseViewModelInterface
import com.apploop.face.changer.app.helpers.EnumClass

class EraseViewModel(eraseCallBack: EraseViewModelInterface) {

    private var eraseViewModelInterface: EraseViewModelInterface = eraseCallBack

    fun onBackClick() {
        eraseViewModelInterface.onEraseButtonClicks(EnumClass.BACK)
    }

    fun onEraseClick() {
        eraseViewModelInterface.onEraseButtonClicks(EnumClass.ERASE)
    }

    fun onRestoreClick() {
        eraseViewModelInterface.onEraseButtonClicks(EnumClass.RESTORE)
    }

    fun onAutoEraseClick() {
        eraseViewModelInterface.onEraseButtonClicks(EnumClass.AUTO_ERASE)
    }

    fun onZoomClick() {
        eraseViewModelInterface.onEraseButtonClicks(EnumClass.ZOOM)
    }

    fun onDoneClick() {
        eraseViewModelInterface.onEraseButtonClicks(EnumClass.DONE)
    }

    fun onUndoClick() {
        eraseViewModelInterface.onEraseButtonClicks(EnumClass.UNDO)
    }

    fun onRedoClick() {
        eraseViewModelInterface.onEraseButtonClicks(EnumClass.REDO)
    }



}