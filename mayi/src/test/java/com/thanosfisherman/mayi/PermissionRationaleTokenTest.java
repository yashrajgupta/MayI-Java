package com.thanosfisherman.mayi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PermissionRationaleTokenTest {

    @InjectMocks
    PermissionRationaleToken pToken;
    @Mock
    PermissionManager permissionManager;
    @Before
    public void setUp() {
        pToken = new PermissionRationaleToken(permissionManager);
    }

    @Test
    public void testContinuePermissionRequest() {
        pToken.continuePermissionRequest();
        verify(permissionManager).onContinuePermissionRequest();
    }

    @Test
    public void testSkipPermissionRequest() {
        pToken.skipPermissionRequest();
        verify(permissionManager).onSkipPermissionRequest();
    }
}