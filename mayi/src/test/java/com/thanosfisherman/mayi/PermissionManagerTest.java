package com.thanosfisherman.mayi;

import com.thanosfisherman.mayi.listeners.multi.PermissionResultMultiListener;
import com.thanosfisherman.mayi.listeners.multi.RationaleMultiListener;
import com.thanosfisherman.mayi.listeners.single.RationaleSingleListener;
import ohos.aafwk.ability.AbilitySlice;
import ohos.bundle.IBundleManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PermissionManagerTest{

    @InjectMocks
    PermissionManager permissionManager;
    @Mock
    AbilitySlice slice;
    @Mock
    PermissionResultMultiListener permissionResultMultiListener;
    @Mock
    RationaleSingleListener rationaleSingleListener;
    @Mock
    RationaleMultiListener rationaleMultiListener;

    @Before
    public void setUp() {

        permissionManager = PermissionManager.getInstance();
        permissionManager.setmAbility(slice);
    }

    @Test
    public void testRequestPermissionResult() {
        int requestCode = 1001;
        String[] permissions = {"SystemPermission.LOCATION"};
        int[] grantResults = {IBundleManager.PERMISSION_DENIED};
        String[] allPermissions = {"SystemPermission.LOCATION", "SystemPermission.MICROPHONE"};
        List<String> deniedPermissions = new ArrayList<String>();
        deniedPermissions.add("SystemPermission.LOCATION");
        List<String> grantedPermissions = new ArrayList<String>();
        grantedPermissions.add("SystemPermission.MICROPHONE");
        permissionManager.checkPermissions(allPermissions, deniedPermissions, grantedPermissions);
        permissionManager.setListeners(null, permissionResultMultiListener,
                rationaleSingleListener, rationaleMultiListener);
        permissionManager.requestPermissionsResult(requestCode, permissions, grantResults);
        PermissionBean pBean;
        pBean = new PermissionBean("SystemPermission.MICROPHONE");
        pBean.setGranted(true);
        pBean.setPermanentlyDenied(false);
        List<PermissionBean> permissionBeans = new LinkedList<>();
        permissionBeans.add(0, pBean);
        pBean = new PermissionBean("SystemPermission.LOCATION");
        pBean.setGranted(false);
        pBean.setPermanentlyDenied(true);
        permissionBeans.add(1, pBean);
        verify(permissionResultMultiListener).permissionResults(permissionBeans.toArray(new
                PermissionBean[permissionBeans.size()]));
    }

    @Test
    public void testOnContinuePermissionRequest() {
        String[] allPermissions = {"SystemPermission.LOCATION", "SystemPermission.MICROPHONE"};
        List<String> deniedPermissions = new ArrayList<String>();
        deniedPermissions.add("SystemPermission.LOCATION");
        List<String> grantedPermissions = new ArrayList<String>();
        grantedPermissions.add("SystemPermission.MICROPHONE");
        permissionManager.checkPermissions(allPermissions, deniedPermissions, grantedPermissions);
        permissionManager.onContinuePermissionRequest();
        verify(slice).requestPermissionsFromUser(deniedPermissions.toArray(new
                String[deniedPermissions.size()]), 1001);
    }
}