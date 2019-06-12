package com.zfkj.gamecenter.permissiondemo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.zfkj.gamecenter.permission.PermissionUtils;


/**
 * Created by Administrator on 2019/5/28.
 */

public class MainActivity extends Activity {
    PermissionUtils permissionUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permissionUtils = PermissionUtils.getInstance(this);
        permissionUtils.permission(Manifest.permission.READ_PHONE_STATE)
                .allowRationale(true)
                .rationale(
                        new PermissionUtils.Rationale()
                                .setCancelText("cancel")
                                .setConfirmText("confirm")
                                .setConfirmByStyle(R.drawable.btn_confirm_bg_style)
                )
                .callback(new PermissionUtils.PermissionGrant() {
                    @Override
                    public void onPermissionGranted() {
                        Toast.makeText(MainActivity.this, "授权通过", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onUnGranted() {
                        Toast.makeText(MainActivity.this, "未授权通过", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCompleted() {
                        Toast.makeText(MainActivity.this, "授权完成", Toast.LENGTH_LONG).show();
                    }
                })
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PermissionUtils.PERMISSION_ACTIVITY_REQUEST_CODE) // 权限请求回调
            permissionUtils.onActivityResult();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 权限申请结果检查
        permissionUtils.requestPermissionResult();
    }
}
