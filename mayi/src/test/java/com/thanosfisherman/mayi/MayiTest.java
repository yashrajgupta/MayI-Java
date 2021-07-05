package com.thanosfisherman.mayi;

import com.thanosfisherman.mayi.listeners.MayiErrorListener;
import com.thanosfisherman.mayi.listeners.multi.PermissionResultMultiListener;
import com.thanosfisherman.mayi.listeners.multi.RationaleMultiListener;
import com.thanosfisherman.mayi.listeners.single.PermissionResultSingleListener;
import com.thanosfisherman.mayi.listeners.single.RationaleSingleListener;
import ohos.aafwk.ability.AbilitySlice;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class MayiTest {

    @InjectMocks
    Mayi mayi;
    @Mock
    PermissionResultSingleListener response;
    @Mock
    PermissionResultMultiListener response1;
    @Mock
    RationaleSingleListener rationale;
    @Mock
    RationaleMultiListener rationale1;
    @Mock
    MayiErrorListener errorListener;
    @Mock
    AbilitySlice slice;
    @Mock
    MayiErrorListener mErrorListener;

    @Before
    public void setUp() throws Exception {
        mayi = (Mayi)Mayi.withActivity(slice);
    }

    @Test
    public void testWithPermission() {
        Mayi mayi1 = (Mayi)mayi.withPermission("SystemPermission.Location");
        Assert.assertNotNull(mayi1);
    }

    @Test
    public void testWithPermissions() {
        String permssions[] = {"SystemPermission.Location", "SystemPermission.Microphone"};
        Mayi mayi1 = (Mayi)mayi.withPermissions(permssions);
        Assert.assertNotNull(mayi1);
    }

    @Test
    public void testOnResult() {
        Mayi mayi1 = (Mayi)mayi.onResult(response);
        Assert.assertNotNull(mayi1);
    }

    @Test
    public void testOnResultMulti() {
        Mayi mayi1 = (Mayi)mayi.onResult(response1);
        Assert.assertNotNull(mayi1);
    }

    @Test
    public void testOnRationale() {
        Mayi mayi1 = (Mayi)mayi.onRationale(rationale);
        Assert.assertNotNull(mayi1);
    }


    @Test
    public void testOnRationaleMulti() {
        Mayi mayi1 = (Mayi)mayi.onRationale(rationale1);
        Assert.assertNotNull(mayi1);
    }


    @Test
    public void testOnErrorListener() {
        Mayi mayi1 = (Mayi)mayi.onErrorListener(errorListener);
        Assert.assertNotNull(mayi1);
    }

    @Test
    public void testCheck() throws NullPointerException {
        try {
            mayi.onErrorListener(mErrorListener);
            Mayi mayi1 = (Mayi)mayi.withPermissions(null);
            mayi1.check();
        } catch(Exception e) {
            verify(mErrorListener).onError(e);
        }
    }
}