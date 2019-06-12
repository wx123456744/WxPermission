package com.zfkj.gamecenter.permission;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by win7 on 2019/2/22.
 */

public class PermissionUtils {
    private static final String TAG = PermissionUtils.class.getSimpleName();
    private Activity mContext;
    private String[] requestPermissions;
    private PermissionGrant grantListener;
    private RationaleCancel cancelListener; // 授权弹窗关闭按钮监听
    private RationaleConfirm confirmListener; // 授权弹窗确认按钮监听
    private static PermissionUtils mPermissionUtils;
    private Rationale rationale;
    private boolean allowRationale = true; // 是否显示权限设置弹窗
    public final static int PERMISSION_ACTIVITY_REQUEST_CODE = 1100;
    private RationaleDialog rationaleDialog;

    private PermissionUtils(Activity mContext) {
        this.mContext = mContext;
        this.rationale = new Rationale();
    }

    public static PermissionUtils getInstance(Activity activity) {
        if (mPermissionUtils == null)
            mPermissionUtils = new PermissionUtils(activity);
        return mPermissionUtils;
    }

    public interface PermissionGrant {
        void onPermissionGranted(); // 授权

        void onUnGranted(); // 未授权

        void onCompleted(); // 授权结束
    }

    public static class Rationale {
        private String content;
        private String cancelText;
        private String confirmText;
        private RationaleCancel cancelListener;
        private RationaleConfirm confirmListener;
        private int cancelBgStyle, confirmBgStyle;

        public Rationale setContent(String content) {
            this.content = content;
            return this;
        }

        public Rationale setCancelText(String cancelText) {
            this.cancelText = cancelText;
            return this;
        }

        public Rationale setCancelBgStyle(int themeId) {
            this.cancelBgStyle = themeId;
            return this;
        }

        public Rationale setConfirmByStyle(int themeId) {
            this.confirmBgStyle = themeId;
            return this;
        }

        public Rationale setConfirmText(String confirmText) {
            this.confirmText = confirmText;
            return this;
        }

        public Rationale setCancelListener(RationaleCancel cancelListener) {
            this.cancelListener = cancelListener;
            return this;
        }

        public Rationale setConfirmListener(RationaleConfirm confirmListener) {
            this.confirmListener = confirmListener;
            return this;
        }
    }

    public PermissionUtils rationale(Rationale rationale) {
        this.rationale = rationale;
        return this;
    }

    public interface RationaleCancel {
        void onCancel();
    }

    public interface RationaleConfirm {
        void onConfirm();
    }

    public PermissionUtils callback(PermissionGrant listener) {
        if (listener != null)
            grantListener = listener;
        return this;
    }

    private boolean checkItemPermission(String permission) {
        if (ActivityCompat.checkSelfPermission(mContext, permission) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else
            return false;
    }

    public PermissionUtils allowRationale(boolean show) {
        this.allowRationale = show;
        return this;
    }

    public void start() {
        if (requestPermissions.length == 0)
            return;
        List<String> no_grant_permissions = new ArrayList<>();
        if (checkItemPermission(requestPermissions[0])) {
            // 授权成功
            for (int i = 1; i < requestPermissions.length; i++) {
                if (!checkItemPermission(requestPermissions[i])) {
                    no_grant_permissions.add(requestPermissions[i]);
                }
            }
            if (no_grant_permissions.size() == 0) {
              /*  if (!allowRationale && android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
                *    grantListener.onUnGranted();
                else*/
                grantListener.onPermissionGranted();
                grantListener.onCompleted();
            } else
                ActivityCompat.requestPermissions(mContext, no_grant_permissions.toArray(new String[no_grant_permissions.size()])
                        , PERMISSION_ACTIVITY_REQUEST_CODE);
        } else {
            // 授权失败 保存未授权的权限
            ActivityCompat.requestPermissions(mContext, requestPermissions, PERMISSION_ACTIVITY_REQUEST_CODE);
        }
    }

    private void openSetting() {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= 9) {
            Log.e("HLQ_Struggle", "APPLICATION_DETAILS_SETTINGS");
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", mContext.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", mContext.getPackageName());
        }
        mContext.startActivityForResult(intent, PERMISSION_ACTIVITY_REQUEST_CODE);
    }

    private void showRationale() {
        String content = "我们需要您授予权限才能进行下一步操作！";
        if (!ConversionUtils.isEmpty(rationale.content))
            content = rationale.content;
        String confirmText = "去设置";
        if (!ConversionUtils.isEmpty(rationale.confirmText))
            confirmText = rationale.confirmText;
        String cancelText = "关闭";
        if (!ConversionUtils.isEmpty(rationale.cancelText))
            cancelText = rationale.cancelText;
        if (rationale.confirmListener != null)
            confirmListener = rationale.confirmListener;
        if (rationale.cancelListener != null)
            cancelListener = rationale.cancelListener;
        RationaleDialog.Builder builder = new RationaleDialog.Builder(mContext, R.layout.dialog_permission_rationale_layout);
        rationaleDialog = builder
                .setMessage(content)
                .setConfirmButton(confirmText, rationale.confirmBgStyle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (confirmListener != null)
                            confirmListener.onConfirm();
                        else
                            openSetting();
                    }
                })
                .setCancelButton(cancelText, rationale.cancelBgStyle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (cancelListener != null)
                            cancelListener.onCancel();
                        else {
                            dialog.cancel();
                            if (grantListener != null) {
                                grantListener.onUnGranted();
                                grantListener.onCompleted();
                            }
                        }
                    }
                })
                .build();
        rationaleDialog.show();
    }

    public void requestPermissionResult() {
        if (grantListener == null) {
            Log.e(TAG, "未传入授权回调");
            return;
        }
        boolean check = true;
        if (requestPermissions != null && requestPermissions.length > 0) {
            for (int i = 0; i < requestPermissions.length; i++) {
                if (!checkItemPermission(requestPermissions[i])) {
                    check = false;
                    break;
                }
            }
            if (check) {
                grantListener.onPermissionGranted();
            } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {  // < 23
                grantListener.onUnGranted();
            } else {
                if (allowRationale) showRationale(); // 允许弹窗
                else grantListener.onUnGranted(); //
            }
        }
        grantListener.onCompleted();
    }

    /**
     * Requests permission.
     */
    public PermissionUtils permission(String... permissions) {

        if (permissions != null && permissions.length == 0) {
            // TODO: 抛出异常
            Log.e(TAG, "未传入待检查的权限");
        }
        requestPermissions = permissions;
        return this;
    }

    /**
     * 传入权限组
     *
     * @param permissionGroup 权限组
     * @return
     */
    public PermissionUtils permission(String[]... permissionGroup) {
        List<String> permission_list = new ArrayList<>();
        for (String[] itemGroup : permissionGroup) {
            for (String item : itemGroup) {
                permission_list.add(item);
            }
        }
        requestPermissions = permission_list.toArray(new String[0]);
        return this;
    }

    public void onActivityResult() {
        rationaleDialog.dismiss();
        this.start();
    }


}
