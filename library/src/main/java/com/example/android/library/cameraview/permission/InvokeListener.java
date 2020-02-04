package com.example.android.library.cameraview.permission;


import com.example.android.library.cameraview.model.InvokeParam;

/**
 * 授权管理回调
 */
public interface InvokeListener {
    PermissionManager.TPermissionType invoke(InvokeParam invokeParam);
}
