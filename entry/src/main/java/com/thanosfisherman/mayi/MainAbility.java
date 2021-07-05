package com.thanosfisherman.mayi;

import com.thanosfisherman.mayi.slice.MainAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class MainAbility extends Ability {
    private static final HiLogLabel HILOGLABEL = new HiLogLabel(0, 0, "MainAbility");

    @Override
    public void onStart(Intent intent) {
        HiLog.debug(HILOGLABEL, "Yash onStart enter");
        super.onStart(intent);
        super.setMainRoute(MainAbilitySlice.class.getName());

    }

    @Override
    public void onRequestPermissionsFromUserResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsFromUserResult(requestCode, permissions, grantResults);
        HiLog.debug(HILOGLABEL, "Yash onRequestPermissionsFromUserResult enter : " + requestCode);
        PermissionManager permissionManager = PermissionManager.getInstance();
        permissionManager.requestPermissionsResult(requestCode, permissions, grantResults);
        permissionManager.closeSlice();
    }
}
