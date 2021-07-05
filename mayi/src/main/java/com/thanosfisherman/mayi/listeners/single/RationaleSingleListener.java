package com.thanosfisherman.mayi.listeners.single;

import com.thanosfisherman.mayi.PermissionBean;
import com.thanosfisherman.mayi.PermissionToken;

@FunctionalInterface
public interface RationaleSingleListener {
    void onRationale(PermissionBean permission, PermissionToken token);
}