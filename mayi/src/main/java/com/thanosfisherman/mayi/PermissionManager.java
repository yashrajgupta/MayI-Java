package com.thanosfisherman.mayi;

import com.thanosfisherman.mayi.listeners.multi.PermissionResultMultiListener;
import com.thanosfisherman.mayi.listeners.multi.RationaleMultiListener;
import com.thanosfisherman.mayi.listeners.single.PermissionResultSingleListener;
import com.thanosfisherman.mayi.listeners.single.RationaleSingleListener;
import ohos.aafwk.ability.AbilitySlice;
import ohos.bundle.IBundleManager;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

public class PermissionManager {
    private static PermissionManager singleInstance = null;
    public static final String TAG = PermissionManager.class.getSimpleName();
    public static final int PERMISSION_REQUEST_CODE = 1001;
    @Nullable private PermissionResultSingleListener mPermissionResultListener;
    @Nullable private RationaleSingleListener mRationaleSingleListener;
    @Nullable private PermissionResultMultiListener mPermissionsResultMultiListener;
    @Nullable private RationaleMultiListener mRationaleMultiListener;
    private List<String> mDeniedPermissions;
    private List<String> mGrantedPermissions;
    private final List<String> mRationalePermissions = new LinkedList<>();
    private String[] mPermissions;
    private boolean isShowingNativeDialog;
    private WeakReference<AbilitySlice> mAbility;
    private static final HiLogLabel HILOGLABEL = new HiLogLabel(0, 0, "MainAbility");

    /**
     * The function to return the instance of the singleton class.
     *
     *  @return returns the single instance of the class
     */
    public static PermissionManager getInstance() {
        if (singleInstance == null) {
            singleInstance = new PermissionManager();
        }
        return singleInstance;
    }

    public void setmAbility(AbilitySlice activity) {
        this.mAbility = new WeakReference<>(activity);
    }

    /**
     * This method sets the beansResultList with the permissions.
     *
     * @param beansResultList contains the empty list to add the permission beans
     * @param permissions contains all the permissions whose result is requested
     * @param grantResults contains the result array received after the call back
     *                    method onRequestPermissionsFromUserResult
     */
    public void getBeanResults(List<PermissionBean> beansResultList, String[] permissions, int[] grantResults) {
        for (int i = 0; i < permissions.length; i++) {
            beansResultList.add(i, new PermissionBean(permissions[i]));
            if (grantResults[i] == IBundleManager.PERMISSION_DENIED) {
                beansResultList.get(i).setGranted(false);
                beansResultList.get(i).setPermanentlyDenied(!mAbility.get().canRequestPermission(permissions[i]));
            } else {
                beansResultList.get(i).setGranted(true);
                beansResultList.get(i).setPermanentlyDenied(false);
            }
        }
    }

    /**
     * This method sets the result permission beans to the listeners.
     *
     * @param requestCode contains the request code received from the
     *                    callback function onRequestPermissionsFromUserResult
     * @param permissions contains the permissions requested
     * @param grantResults contains the results of the permission requests
     *                    from the callback function onRequestPermissionsFromUserResult
     */
    public void requestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            isShowingNativeDialog = false;
            if (grantResults.length == 0) {
                return;
            }
            final List<PermissionBean> beansResultList = new LinkedList<>();
            getBeanResults(beansResultList, permissions, grantResults);
            if (mPermissionResultListener != null) {
                mPermissionResultListener.permissionResult(beansResultList.get(0));
            } else if (mPermissionsResultMultiListener != null) {
                final List<PermissionBean> beansTotal = new LinkedList<>();
                for (String perm : mGrantedPermissions) {
                    final PermissionBean bean = new PermissionBean(perm);
                    bean.setGranted(true);
                    bean.setPermanentlyDenied(false);
                    beansTotal.add(bean);
                }
                beansTotal.addAll(beansResultList);
                mPermissionsResultMultiListener.permissionResults(beansTotal.toArray(new
                        PermissionBean[beansTotal.size()]));
            }
        }
    }

    /**
     * The method sets the permission variables.
     *
     * @param allPermissions contqins all the permissions requested by the user
     * @param deniedPermissions contains the denied permissions, which are to requested again
     * @param grantedPermissions contains the already granted permissions
     */
    public void checkPermissions(String[] allPermissions, List<String> deniedPermissions,
                                 List<String> grantedPermissions) {
        mPermissions = allPermissions;
        mDeniedPermissions = deniedPermissions;
        mGrantedPermissions = grantedPermissions;
        mRationalePermissions.clear();
    }

    /**
     * Method to check the denied permission and call the rationale if the permission can be requested.
     */
    public void checkPermissionsUtil() {
        final List<PermissionBean> rationaleBeanList = new LinkedList<>();
        for (String deniedPermission : mDeniedPermissions) {
            if (mAbility.get().canRequestPermission(deniedPermission)) {
                HiLog.debug(HILOGLABEL, "Yash can request : " + deniedPermission);
                final PermissionBean beanRationale = new PermissionBean(deniedPermission);
                beanRationale.setGranted(false);
                beanRationale.setPermanentlyDenied(false);
                rationaleBeanList.add(beanRationale);
                mRationalePermissions.add(deniedPermission);
            }
        }
        if (rationaleBeanList.isEmpty()) {
            if (!isShowingNativeDialog) {
                mAbility.get().requestPermissionsFromUser(mDeniedPermissions.toArray(new
                        String[mDeniedPermissions.size()]), PERMISSION_REQUEST_CODE);
            }
            isShowingNativeDialog = true;
        } else {
            if (mRationaleSingleListener != null) {
                mRationaleSingleListener.onRationale(rationaleBeanList.get(0), new PermissionRationaleToken(this));
            } else if (mRationaleMultiListener != null) {
                mRationaleMultiListener.onRationale(rationaleBeanList.toArray(new
                        PermissionBean[rationaleBeanList.size()]), new PermissionRationaleToken(this));
            }
        }
    }

    void onContinuePermissionRequest() {
        if (!isShowingNativeDialog) {
            mAbility.get().requestPermissionsFromUser(mDeniedPermissions.toArray(new
                    String[mDeniedPermissions.size()]), PERMISSION_REQUEST_CODE);
        }
        isShowingNativeDialog = true;
    }

    void onSkipPermissionRequest() {
        isShowingNativeDialog = false;
        if (mPermissionResultListener != null) {
            final PermissionBean beanRationale = new PermissionBean(mRationalePermissions.get(0));
            beanRationale.setGranted(false);
            beanRationale.setPermanentlyDenied(false);
            mPermissionResultListener.permissionResult(beanRationale);
        } else if (mPermissionsResultMultiListener != null) {
            final List<PermissionBean> totalBeanList = new LinkedList<>();
            for (String perm : mPermissions) {
                final PermissionBean bean = new PermissionBean(perm);
                if (mGrantedPermissions.contains(perm)) {
                    bean.setGranted(true);
                    bean.setPermanentlyDenied(false);
                } else if (mRationalePermissions.contains(perm)) {
                    bean.setGranted(false);
                    bean.setPermanentlyDenied(false);
                } else {
                    bean.setGranted(false);
                    bean.setPermanentlyDenied(true);
                }
                totalBeanList.add(bean);
            }
            mPermissionsResultMultiListener.permissionResults(totalBeanList.toArray(new
                    PermissionBean[totalBeanList.size()]));
        }
    }

    /**
     * Method to set the listeners.
     *
     * @param listenerResult Stores the listener for a single permission request,
     *                       null in case of multipermission request
     * @param listenerResultMulti Stores the listener for multi permission requests,
     *                       null in case of a single permission request
     * @param rationaleSingle stores the listener for the rationale of a single denied permission
     * @param rationaleMulti stores the listener for the rationale of multi denied permissions
     */
    public void setListeners(PermissionResultSingleListener listenerResult, PermissionResultMultiListener
            listenerResultMulti, RationaleSingleListener rationaleSingle, RationaleMultiListener rationaleMulti) {
        mPermissionResultListener = listenerResult;
        mPermissionsResultMultiListener = listenerResultMulti;
        mRationaleSingleListener = rationaleSingle;
        mRationaleMultiListener = rationaleMulti;
    }

    /**
     * Closes the slice once the permission results are recorded.
     */
    public void closeSlice() {
        mAbility.get().terminate();
    }
}
