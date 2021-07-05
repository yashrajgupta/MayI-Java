package com.thanosfisherman.mayi;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.*;

public class PermissionBeanTest {

    @InjectMocks
    PermissionBean pBean;
    @Mock
    Object o;
    @Before
    public void setUp() {
        pBean = new PermissionBean("Yash.Permissions.LOCATION");
    }

    @Test
    public void testEquals() {
        boolean result = pBean.equals(o);
        assertFalse(result);
    }

    @Test
    public void testEqualsTrue() {
        PermissionBean pBean1 = new PermissionBean("Yash.Permissions.LOCATION");
        boolean result = pBean.equals(pBean1);
        assertTrue(result);
    }

    @Test
    public void testHashCode(){
        String name = "Yash.Permissions.LOCATION";
        int result = name.hashCode();
        result = 31 * result;
        assertEquals(result, pBean.hashCode());
    }

    @Test
    public void testToString() {
        String result = "Permission{" + "name='" + pBean.getSimpleName() + "'" + ", isGranted=" + pBean.isGranted()
                + ", isPermanentlyDenied=" + pBean.isPermanentlyDenied() + "}";
        assertEquals(pBean.toString(), result);
    }

    @Test
    public void testGetName() {
        String name = "Yash.Permissions.LOCATION";
        assertEquals(pBean.getName(), name);
    }

    @Test
    public void testGetSimpleNameTrue() {
        String name = "Yash.Permissions.LOCATION";
        String result = name.split("\\.")[2];
        assertEquals(pBean.getSimpleName(), result);
    }

    @Test
    public void testIsGranted() {
        assertFalse(pBean.isGranted());
    }

    @Test
    public void testSetGranted() {
        pBean.setGranted(true);
        assertTrue(pBean.isGranted());
    }

    @Test
    public void testIsPermanentlyDenied() {
        assertFalse(pBean.isPermanentlyDenied());
    }

    @Test
    public void testSetPermanentlyDenied() {
        pBean.setPermanentlyDenied(true);
        assertTrue(pBean.isPermanentlyDenied());
    }
}