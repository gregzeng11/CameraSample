/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.library.cameraview;

import android.view.View;

import java.util.Set;

/**
 * 不同方式实现CameraView的抽象基类
 */
abstract class CameraViewImpl {

    public static final int FOCUS_AREA_SIZE_DEFAULT = 300;
    public static final int FOCUS_METERING_AREA_WEIGHT_DEFAULT = 1000;
    public static final int DELAY_MILLIS_BEFORE_RESETTING_FOCUS = 3000;

    protected final Callback mCallback;
    protected final PreviewImpl mPreview;

    CameraViewImpl(Callback callback, PreviewImpl preview) {
        mCallback = callback;
        mPreview = preview;
    }

    View getView() {
        return mPreview.getView();
    }

    int getFocusAreaSize() {
        return FOCUS_AREA_SIZE_DEFAULT;
    }

    int getFocusMeteringAreaWeight() {
        return FOCUS_METERING_AREA_WEIGHT_DEFAULT;
    }

    void detachFocusTapListener() {
        if (mPreview != null && mPreview.getView() != null) {
            mPreview.getView().setOnTouchListener(null);
        }
    }

    abstract boolean start();

    abstract void stop();

    abstract boolean isCameraOpened();

    abstract void setFacing(int facing);

    abstract int getFacing();

    abstract Set<AspectRatio> getSupportedAspectRatios();

    abstract boolean setAspectRatio(AspectRatio ratio);

    abstract AspectRatio getAspectRatio();

    abstract void setAutoFocus(boolean autoFocus);

    abstract boolean getAutoFocus();

    abstract void setFlash(int flash);

    abstract int getFlash();

    abstract void takePicture();

    abstract void setDisplayOrientation(int displayOrientation);


    /**
     * 摄像头相关回调 (camera session callbacks)
     */
    interface Callback {

        void onCameraOpened();

        void onCameraClosed();

        void onPictureTaken(byte[] data);
    }

}

