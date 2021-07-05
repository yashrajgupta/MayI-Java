package com.thanosfisherman.mayi.listeners.multi;


import com.thanosfisherman.mayi.PermissionBean;
import com.thanosfisherman.mayi.PermissionToken;

@FunctionalInterface
public interface RationaleMultiListener {
    void onRationale(PermissionBean[] permissions, PermissionToken token);
}
