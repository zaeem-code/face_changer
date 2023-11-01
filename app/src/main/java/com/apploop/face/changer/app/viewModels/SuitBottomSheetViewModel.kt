package com.apploop.face.changer.app.viewModels

import androidx.lifecycle.ViewModel
import com.apploop.face.changer.app.callBacks.SuitBottomSheetViewModelInterface

class SuitBottomSheetViewModel(suitCallBack: SuitBottomSheetViewModelInterface) : ViewModel() {

    private var suitViewModelInterface: SuitBottomSheetViewModelInterface = suitCallBack

}