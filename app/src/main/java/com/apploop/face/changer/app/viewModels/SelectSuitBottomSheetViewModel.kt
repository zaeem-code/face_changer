package com.apploop.face.changer.app.viewModels

import androidx.lifecycle.ViewModel
import com.apploop.face.changer.app.callBacks.SelectSuitBottomSheetViewModelInterface
import com.apploop.face.changer.app.helpers.EnumClass

class SelectSuitBottomSheetViewModel(selectSuitCallBack: SelectSuitBottomSheetViewModelInterface) : ViewModel() {

    private var selectSuitViewModelInterface: SelectSuitBottomSheetViewModelInterface = selectSuitCallBack

    fun onBlazerClick() {
        selectSuitViewModelInterface.onSelectSuitBottomSheetButtonClicks(EnumClass.BLAZER)
    }

    fun onSuitClick() {
        selectSuitViewModelInterface.onSelectSuitBottomSheetButtonClicks(EnumClass.SUIT)
    }

    fun onFormalClick() {
        selectSuitViewModelInterface.onSelectSuitBottomSheetButtonClicks(EnumClass.FORMAL)
    }

    fun onJacketClick() {
        selectSuitViewModelInterface.onSelectSuitBottomSheetButtonClicks(EnumClass.JACKET)
    }

    fun onPoliceClick() {
        selectSuitViewModelInterface.onSelectSuitBottomSheetButtonClicks(EnumClass.Police)
    }

    fun onKurtaClick() {
        selectSuitViewModelInterface.onSelectSuitBottomSheetButtonClicks(EnumClass.KURTA)
    }

    fun onBodyBuilderClick() {
        selectSuitViewModelInterface.onSelectSuitBottomSheetButtonClicks(EnumClass.BODY_BUILDER)
    }

    fun onTShirtClick() {
        selectSuitViewModelInterface.onSelectSuitBottomSheetButtonClicks(EnumClass.T_SHIRT)
    }

    fun onChristmasClick() {
        selectSuitViewModelInterface.onSelectSuitBottomSheetButtonClicks(EnumClass.CHRISTMAS)
    }
}