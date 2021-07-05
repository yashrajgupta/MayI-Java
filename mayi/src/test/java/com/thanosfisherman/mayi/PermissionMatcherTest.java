package com.thanosfisherman.mayi;

import ohos.aafwk.ability.AbilitySlice;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.ref.WeakReference;


@RunWith(MockitoJUnitRunner.class)
public class PermissionMatcherTest  {
    String[] permissions = {"SystemPermission.LOCATION", "SystemPermission.MICROPHONE"};
    PermissionMatcher pMatcher;
    @Mock
    WeakReference<AbilitySlice> slice;

    @Test(expected = IllegalArgumentException.class)
    public void testMatcher() {
        pMatcher = new PermissionMatcher(null, slice);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatcherWithNullSlice() {
        pMatcher = new PermissionMatcher(permissions, null);
    }
}