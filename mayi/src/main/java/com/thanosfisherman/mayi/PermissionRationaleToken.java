package com.thanosfisherman.mayi;

import java.lang.ref.WeakReference;

final class PermissionRationaleToken implements PermissionToken {
    private final WeakReference<PermissionManager> permissionManager;
    private boolean isTokenResolved = false;

    PermissionRationaleToken(PermissionManager permissionManager) {
        this.permissionManager = new WeakReference<>(permissionManager);
    }

    @Override
    public void continuePermissionRequest() {
        if (!isTokenResolved) {
            permissionManager.get().onContinuePermissionRequest();
            isTokenResolved = true;
        }
    }

    @Override
    public void skipPermissionRequest() {
        if (!isTokenResolved) {
            permissionManager.get().onSkipPermissionRequest();
            isTokenResolved = true;
        }
    }
}
