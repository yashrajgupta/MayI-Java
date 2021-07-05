package com.thanosfisherman.mayi;

/**
 * Interface to continue or stop the permission request.
 */
public interface PermissionToken {

    /**
     * Continues with the permission request process.
     */
    void continuePermissionRequest();

    /**
     * Cancels the permission request process.
     */
    void skipPermissionRequest();
}
