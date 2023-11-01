package com.apploop.face.changer.app.viewModels

import androidx.lifecycle.ViewModel
import com.apploop.face.changer.app.callBacks.MainViewModelInterface
import com.apploop.face.changer.app.helpers.EnumClass

class MainActivityViewModel(mainCallBack: MainViewModelInterface) : ViewModel() {

    private var mainViewModelInterface: MainViewModelInterface = mainCallBack

    fun openMyCreation() {
        mainViewModelInterface.onMainActivityButtonClicks(EnumClass.MY_CREATION)
    }

    fun onPhotoRemoveClick() {
        mainViewModelInterface.onMainActivityButtonClicks(EnumClass.PHOTO_REMOVE_BG)
    }
    fun onPhotoMenClick() {
        mainViewModelInterface.onMainActivityButtonClicks(EnumClass.PHOTO_MEN)
    }
    fun onPhotoFaceClick() {
        mainViewModelInterface.onMainActivityButtonClicks(EnumClass.PHOTO_FACE)
    }


}