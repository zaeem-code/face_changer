package com.apploop.face.changer.app.viewModels

import com.apploop.face.changer.app.callBacks.CategoryModelInterface
import com.apploop.face.changer.app.helpers.EnumClass

class CategoryViewModel(categoryCallBack: CategoryModelInterface) {

    private var categoryViewModelInterface: CategoryModelInterface = categoryCallBack

    fun onBackClick() {
        categoryViewModelInterface.onCategoryButtonClicks(EnumClass.BACK)
    }

    fun onBlazerClick() {
        categoryViewModelInterface.onCategoryButtonClicks(EnumClass.BLAZER)
    }

    fun onSuitClick() {
        categoryViewModelInterface.onCategoryButtonClicks(EnumClass.SUIT)
    }

    fun onFormalClick() {
        categoryViewModelInterface.onCategoryButtonClicks(EnumClass.FORMAL)
    }

    fun onJacketClick() {
        categoryViewModelInterface.onCategoryButtonClicks(EnumClass.JACKET)
    }

    fun onPoliceClick() {
        categoryViewModelInterface.onCategoryButtonClicks(EnumClass.Police)
    }

    fun onKurtaClick() {
        categoryViewModelInterface.onCategoryButtonClicks(EnumClass.KURTA)
    }

    fun onBodyBuilderClick() {
        categoryViewModelInterface.onCategoryButtonClicks(EnumClass.BODY_BUILDER)
    }

    fun onTShirtClick() {
        categoryViewModelInterface.onCategoryButtonClicks(EnumClass.T_SHIRT)
    }

    fun onCardClick() {
        categoryViewModelInterface.onCategoryButtonClicks(EnumClass.CARD)
    }

    fun onIplClick() {
        categoryViewModelInterface.onCategoryButtonClicks(EnumClass.IPL)
    }

    fun onAfricanClick() {
        categoryViewModelInterface.onCategoryButtonClicks(EnumClass.AFRICA)
    }

    fun onPslClick() {
        categoryViewModelInterface.onCategoryButtonClicks(EnumClass.PSL)
    }

    fun onBplClick() {
        categoryViewModelInterface.onCategoryButtonClicks(EnumClass.BPL)
    }

    fun onBblClick() {
        categoryViewModelInterface.onCategoryButtonClicks(EnumClass.BBL)
    }

    fun onWTClick() {
        categoryViewModelInterface.onCategoryButtonClicks(EnumClass.WT)
    }

    fun onFifaClick() {
        categoryViewModelInterface.onCategoryButtonClicks(EnumClass.FIFA)
    }

    fun onSuperClick() {
        categoryViewModelInterface.onCategoryButtonClicks(EnumClass.SUPER)
    }
}