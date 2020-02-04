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

package com.example.android.library.cameraview.util;


import com.example.android.library.cameraview.AspectRatio;

public interface TConstants {

    AspectRatio DEFAULT_ASPECT_RATIO = AspectRatio.of(16, 9);//如果是16:9的话显示图片的时候可以填充整个屏幕
    AspectRatio SECOND_ASPECT_RATIO = AspectRatio.of(4, 3);//如果是4:3的话显示图片的时候会上下留黑很多空间

    public static final int FACING_BACK = 0;
    public static final int FACING_FRONT = 1;

    public static final int FLASH_OFF = 0;
    public static final int FLASH_ON = 1;
    public static final int FLASH_TORCH = 2;
    public static final int FLASH_AUTO = 3;
    public static final int FLASH_RED_EYE = 4;

    public static final int LANDSCAPE_90 = 90;
    public static final int LANDSCAPE_270 = 270;
    
    public static final String ORIGINAL_PIC_PATH="original_pic.jpg";
    public static final String CROP_PIC_PATH="crop_pic.jpg";
    
    /**
     * request Code 裁剪照片
     **/
    public final static int RC_CROP = 1001;
    /**
     * request Code 从相机获取照片并裁剪
     **/
    public final static int RC_PICK_PICTURE_FROM_CAPTURE_CROP = 1002;
    /**
     * request Code 从相机获取照片不裁剪
     **/
    public final static int RC_PICK_PICTURE_FROM_CAPTURE = 1003;
    /**
     * request Code 从文件中选择照片
     **/
    public final static int RC_PICK_PICTURE_FROM_DOCUMENTS_ORIGINAL = 1004;
    /**
     * request Code 从文件中选择照片并裁剪
     **/
    public final static int RC_PICK_PICTURE_FROM_DOCUMENTS_CROP = 1005;
    /**
     * request Code 从相册选择照
     **/
    public final static int RC_PICK_PICTURE_FROM_GALLERY_ORIGINAL = 1006;
    /**
     * request Code 从相册选择照片并裁剪
     **/
    public final static int RC_PICK_PICTURE_FROM_GALLERY_CROP = 1007;
    /**
     * request Code 选择多张照片
     **/
    public final static int RC_PICK_MULTIPLE = 1008;


    /**
     * requestCode 请求权限
     **/
    public final static int PERMISSION_REQUEST_TAKE_PHOTO = 2000;

   /* public final static String getFileProviderName(Context context) {
        return context.getPackageName() + ".fileprovider";
    }*/
    
}
