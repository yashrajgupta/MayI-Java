package com.thanosfisherman.mayi.listeners.multi;

import com.thanosfisherman.mayi.PermissionBean;

@FunctionalInterface
public interface PermissionResultMultiListener {
    void permissionResults(PermissionBean[] permissions);
}

