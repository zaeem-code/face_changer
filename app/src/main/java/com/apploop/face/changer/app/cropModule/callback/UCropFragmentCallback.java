package com.apploop.face.changer.app.cropModule.callback;

import com.apploop.face.changer.app.cropModule.UCropFragment;
import com.apploop.face.changer.app.cropModule.UCropFragment;

public interface UCropFragmentCallback {

    /**
     * Return loader status
     * @param showLoader
     */
    void loadingProgress(boolean showLoader);

    /**
     * Return cropping result or error
     * @param result
     */
    void onCropFinish(UCropFragment.UCropResult result);

}
