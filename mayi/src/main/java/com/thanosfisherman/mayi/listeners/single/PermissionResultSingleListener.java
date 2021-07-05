package com.thanosfisherman.mayi.listeners.single;

import com.thanosfisherman.mayi.PermissionBean;

@FunctionalInterface
public interface PermissionResultSingleListener {
    void permissionResult(PermissionBean permission);
}