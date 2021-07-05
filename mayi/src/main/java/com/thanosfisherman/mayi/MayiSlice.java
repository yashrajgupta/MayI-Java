package com.thanosfisherman.mayi;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class MayiSlice extends AbilitySlice {
    private static final HiLogLabel HILOGLABEL = new HiLogLabel(0, 0, "Mayi");

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        setUIContent(ResourceTable.Layout_permission_slice);
        HiLog.debug(HILOGLABEL, "Yash MayiSlice enter");
        PermissionManager permissionManager = PermissionManager.getInstance();
        permissionManager.setmAbility(this);
        permissionManager.checkPermissionsUtil();
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
