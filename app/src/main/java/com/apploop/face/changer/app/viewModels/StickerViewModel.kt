package com.apploop.face.changer.app.viewModels

import com.apploop.face.changer.app.callBacks.StickerViewModelInterface
import com.apploop.face.changer.app.helpers.EnumClass

class StickerViewModel(stickerCallBack: StickerViewModelInterface) {

    private var stickerViewModelInterface: StickerViewModelInterface = stickerCallBack

    fun onBackClick() {
        stickerViewModelInterface.onStickerButtonClicks(EnumClass.BACK)
    }

    fun onStickersClick() {
        stickerViewModelInterface.onStickerButtonClicks(EnumClass.STICKERS)
    }

    fun onBackGroundClick() {
        stickerViewModelInterface.onStickerButtonClicks(EnumClass.BACKGROUND)
    }

    fun onOldManClick() {
        stickerViewModelInterface.onStickerButtonClicks(EnumClass.OLD_MAN)
    }
    fun onKidClick() {
        stickerViewModelInterface.onStickerButtonClicks(EnumClass.KID)
    }

    fun onStickerColorClick() {
        stickerViewModelInterface.onStickerButtonClicks(EnumClass.STICKER_COLOR)
    }

    fun onSkinToneClick() {
        stickerViewModelInterface.onStickerButtonClicks(EnumClass.SKIN_TONE)
    }

    fun onOpacityClick() {
        stickerViewModelInterface.onStickerButtonClicks(EnumClass.OPACITY)
    }

    fun onZoomClick() {
        stickerViewModelInterface.onStickerButtonClicks(EnumClass.ZOOM)
    }

    fun onBackgroundColorClick() {
        stickerViewModelInterface.onStickerButtonClicks(EnumClass.BACKGROUND_COLOR)
    }

    fun onAddStickersClick() {
        stickerViewModelInterface.onStickerButtonClicks(EnumClass.ADD_STICKERS)
    }

    fun onBlackClick() {
        stickerViewModelInterface.onStickerButtonClicks(EnumClass.BLACK)
    }

    fun onRedClick() {
        stickerViewModelInterface.onStickerButtonClicks(EnumClass.RED)
    }

    fun onBlueClick() {
        stickerViewModelInterface.onStickerButtonClicks(EnumClass.BLUE)
    }

    fun onGreenClick() {
        stickerViewModelInterface.onStickerButtonClicks(EnumClass.GREEN)
    }

    fun onYellowClick() {
        stickerViewModelInterface.onStickerButtonClicks(EnumClass.YELLOW)
    }

    fun onPurpleClick() {
        stickerViewModelInterface.onStickerButtonClicks(EnumClass.PURPLE)
    }

    fun onGrayClick() {
        stickerViewModelInterface.onStickerButtonClicks(EnumClass.GRAY)
    }

    fun onBlackClick1() {
        stickerViewModelInterface.onStickerButtonClicks(EnumClass.BLACK1)
    }

    fun onRedClick1() {
        stickerViewModelInterface.onStickerButtonClicks(EnumClass.RED1)
    }

    fun onBlueClick1() {
        stickerViewModelInterface.onStickerButtonClicks(EnumClass.BLUE1)
    }

    fun onGreenClick1() {
        stickerViewModelInterface.onStickerButtonClicks(EnumClass.GREEN1)
    }

    fun onYellowClick1() {
        stickerViewModelInterface.onStickerButtonClicks(EnumClass.YELLOW1)
    }

    fun onPurpleClick1() {
        stickerViewModelInterface.onStickerButtonClicks(EnumClass.PURPLE1)
    }

    fun onGrayClick1() {
        stickerViewModelInterface.onStickerButtonClicks(EnumClass.GRAY1)
    }

    fun onBackGroundImagesClick() {
        stickerViewModelInterface.onStickerButtonClicks(EnumClass.BACKGROUND_IMAGES)
    }

    fun onNoneClick() {
        stickerViewModelInterface.onStickerButtonClicks(EnumClass.NONE)
    }

    fun onDoneClick() {
        stickerViewModelInterface.onStickerButtonClicks(EnumClass.DONE)
    }

}