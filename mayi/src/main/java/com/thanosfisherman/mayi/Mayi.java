package com.thanosfisherman.mayi;

import com.thanosfisherman.mayi.listeners.IPermissionBuilder;
import com.thanosfisherman.mayi.listeners.MayiErrorListener;
import com.thanosfisherman.mayi.listeners.multi.PermissionResultMultiListener;
import com.thanosfisherman.mayi.listeners.multi.RationaleMultiListener;
import com.thanosfisherman.mayi.listeners.single.PermissionResultSingleListener;
import com.thanosfisherman.mayi.listeners.single.RationaleSingleListener;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.bundle.IBundleManager;

import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;

public class Mayi implements IPermissionBuilder,
        IPermissionBuilder.Permission,
        IPermissionBuilder.SinglePermissionBuilder,
        IPermissionBuilder.MultiPermissionBuilder {
    private String[] mPermissions;
    @Nullable
    private PermissionResultSingleListener mPermissionResultListener;
    @Nullable private RationaleSingleListener mRationaleSingleListener;
    @Nullable private PermissionResultMultiListener mPermissionsResultMultiListener;
    @Nullable private RationaleMultiListener mRationaleMultiListener;
    private MayiErrorListener mErrorListener;
    private final WeakReference<AbilitySlice> mActivity;
    private boolean isRationaleCalled = false;
    private boolean isResultCalled = false;

    private Mayi(AbilitySlice activity) {
        this.mActivity = new WeakReference<>(activity);
    }

    public static IPermissionBuilder.Permission withActivity(AbilitySlice activity) {
        return new Mayi(activity);
    }

    @Override
    public SinglePermissionBuilder withPermission(String permission) {
        mPermissions = new String[]{permission};
        return this;
    }

    @Override
    public MultiPermissionBuilder withPermissions(String... permissions) {
        mPermissions = permissions;
        return this;
    }

    @Override
    public SinglePermissionBuilder onResult(PermissionResultSingleListener response) {
        if (!isResultCalled) {
            mPermissionResultListener = response;
            isResultCalled = true;
        }
        return this;
    }

    @Override
    public MultiPermissionBuilder onResult(PermissionResultMultiListener response) {
        if (!isResultCalled) {
            mPermissionsResultMultiListener = response;
            isResultCalled = true;
        }
        return this;
    }

    @Override
    public SinglePermissionBuilder onRationale(RationaleSingleListener rationale) {
        if (!isRationaleCalled) {
            mRationaleSingleListener = rationale;
            isRationaleCalled = true;
        }
        return this;
    }

    @Override
    public MultiPermissionBuilder onRationale(RationaleMultiListener rationale) {
        if (!isRationaleCalled) {
            mRationaleMultiListener = rationale;
            isRationaleCalled = true;
        }
        return this;
    }

    @Override
    public IPermissionBuilder onErrorListener(MayiErrorListener errorListener) {
        mErrorListener = errorListener;
        return this;
    }

    @Override
    public void check() {
        try {
            if (mPermissions == null || mPermissions.length == 0) {
                throw new NullPointerException("You must specify at least one valid permission to check");
            }
            if (Arrays.asList(mPermissions).contains(null)) {
                throw new NullPointerException("Permissions arguments must NOT contain null values");
            }
            final PermissionMatcher matcher = new PermissionMatcher(mPermissions, mActivity);
            if (matcher.areAllPermissionsGranted()) {
                grandEverything();
            } else {
                initializeFragmentAndCheck(mPermissions, matcher.getDeniedPermissions(),
                        matcher.getGrantedPermissions());
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (mErrorListener != null) {
                mErrorListener.onError(e);
            }
        }
    }

    private void grandEverything() {
        final PermissionBean[] beans = new PermissionBean[mPermissions.length];
        for (int i = 0; i < mPermissions.length; i++) {
            if (mActivity.get().verifyCallingOrSelfPermission(mPermissions[i]) == IBundleManager.PERMISSION_GRANTED) {
                beans[i] = new PermissionBean(mPermissions[i]);
                beans[i].setGranted(true);
                beans[i].setPermanentlyDenied(false);
            } else {
                beans[i] = new PermissionBean(mPermissions[i]);
                beans[i].setGranted(false);
                beans[i].setPermanentlyDenied(true);
            }
        }
        if (mPermissionResultListener != null) {
            mPermissionResultListener.permissionResult(beans[0]);
        } else if (mPermissionsResultMultiListener != null) {
            mPermissionsResultMultiListener.permissionResults(beans);
        }
    }

    private void initializeFragmentAndCheck(String[] allPermissions, List<String> deniedPermissions,
                                            List<String> grantedPermissions) {
        PermissionManager permissionManager = PermissionManager.getInstance();
        permissionManager.setListeners(mPermissionResultListener, mPermissionsResultMultiListener,
                mRationaleSingleListener, mRationaleMultiListener);
        permissionManager.checkPermissions(allPermissions, deniedPermissions, grantedPermissions);
        MayiSlice mayislice = new MayiSlice();
        mActivity.get().present(mayislice, new Intent());
    }
}