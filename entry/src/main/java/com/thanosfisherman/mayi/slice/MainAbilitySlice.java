package com.thanosfisherman.mayi.slice;

import static ohos.agp.utils.LayoutAlignment.CENTER;

import com.thanosfisherman.mayi.Mayi;
import com.thanosfisherman.mayi.PermissionBean;
import com.thanosfisherman.mayi.PermissionToken;
import com.thanosfisherman.mayi.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.Text;
import ohos.agp.window.dialog.ToastDialog;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.security.SystemPermission;

import java.util.Arrays;

public class MainAbilitySlice extends AbilitySlice {
    private static final HiLogLabel HILOGLABEL = new HiLogLabel(0, 0, "AbilitySlice");
    private Component toastComponent;
    private Text toastText;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        Button buttonContacts = (Button) findComponentById(ResourceTable.Id_microphone_permission_button);
        buttonContacts.setClickedListener(v -> Mayi.withActivity(this)
                .withPermission(SystemPermission.MICROPHONE)
                .onResult(this::permissionResultSingle)
                .onRationale(this::permissionRationaleSingle)
                .check());

        Button buttonLocation = (Button) findComponentById(ResourceTable.Id_location_permission_button);
        buttonLocation.setClickedListener(v -> Mayi.withActivity(this)
                .withPermission(SystemPermission.LOCATION)
                .onResult(this::permissionResultSingle)
                .onRationale(this::permissionRationaleSingle)
                .check());

        Button buttonAll = (Button) findComponentById(ResourceTable.Id_all_permissions_button);
        buttonAll.setClickedListener(v -> Mayi.withActivity(this)
                .withPermissions(SystemPermission.MICROPHONE, SystemPermission.LOCATION)
                .onRationale(this::permissionRationaleMulti)
                .onResult(this::permissionResultMulti)
                .onErrorListener(this::inCaseOfError)
                .check());
    }

    private void permissionResultSingle(PermissionBean permission) {
        toastComponent = LayoutScatter.getInstance(this).parse(ResourceTable.Layout_toast_ui, null, false);
        toastText = (Text) toastComponent.findComponentById(ResourceTable.Id_toastUi);
        toastText.setText("PERMISSION RESULT " + permission);
        new ToastDialog(this.getApplicationContext()).setDuration(500).setComponent(toastComponent).show();
    }

    private void permissionRationaleSingle(PermissionBean bean, PermissionToken token) {
        HiLog.debug(HILOGLABEL, "Yash AbilitySlice Rationale " + bean.getSimpleName() + " permission");
        toastComponent = LayoutScatter.getInstance(this).parse(ResourceTable.Layout_toast_ui, null, false);
        toastText = (Text) toastComponent.findComponentById(ResourceTable.Id_toastUi);
        toastText.setText("Should show rationale for " + bean.getSimpleName() + " permission");
        new ToastDialog(this.getApplicationContext()).setDuration(500)
                .setComponent(toastComponent).setAlignment(CENTER).show();
        token.continuePermissionRequest();
    }

    private void permissionResultMulti(PermissionBean[] permissions) {
        toastComponent = LayoutScatter.getInstance(this).parse(ResourceTable.Layout_toast_ui, null, false);
        toastText = (Text) toastComponent.findComponentById(ResourceTable.Id_toastUi);
        toastText.setText("MULTI PERMISSION RESULT " + Arrays.deepToString(permissions));
        new ToastDialog(this.getApplicationContext()).setDuration(500).setComponent(toastComponent).show();
    }

    private void permissionRationaleMulti(PermissionBean[] permissions, PermissionToken token) {
        HiLog.debug(HILOGLABEL, "Yash AbilitySlice Rationale " + Arrays.deepToString(permissions));
        toastComponent = LayoutScatter.getInstance(this).parse(ResourceTable.Layout_toast_ui, null, false);
        toastText = (Text) toastComponent.findComponentById(ResourceTable.Id_toastUi);
        toastText.setText("Rationales for Multiple Permissions " + Arrays.deepToString(permissions));
        new ToastDialog(this.getApplicationContext()).setDuration(500)
                .setComponent(toastComponent).setAlignment(CENTER).show();
        token.continuePermissionRequest();
    }

    private void inCaseOfError(Exception e) {
        new ToastDialog(getContext()).setText("ERROR " + e.toString()).show();
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
