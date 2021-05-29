package com.vijay.runtimepermissionutil;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements PermissionUtils.PermissionListener {

    private static final String TAG = "MainActivity";
    PermissionUtils permissionUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(TAG, "onCreate: ");
        permissionUtils = new PermissionUtils(this);

        singlePermission(MainActivity.this, PermissionUtils.STORAGE);
        multiplePermission(MainActivity.this, PermissionUtils.CAMERA,
                PermissionUtils.STORAGE
        );
    }

    private void openSettingPage() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    private void singlePermission(Activity activity, String permission) {
        permissionUtils.askPermission(activity, permission, isGranted -> {
            if (isGranted) {
                Log.e(TAG, "singlePermission: granted");
            } else {
                Log.e(TAG, "singlePermission: not granted");
            }
        });
    }

    private void multiplePermission(Activity activity, String per1, String per2) {
        permissionUtils.askPermissions(activity,
                per1,
                per2,

                isGranted -> {
                    if (isGranted) {
                        Log.e(TAG, "onPermissionResult: granted");
                    } else {
                        Log.e(TAG, "onPermissionResult: permission denied");
                        Toast.makeText(this, getResources().getString(R.string.please_allow_location), Toast.LENGTH_LONG).show();

                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e(TAG, "onRequestPermissionsResult: " + Arrays.asList(permissions));
        Log.e(TAG, "onRequestPermissionsResult: " + Arrays.asList(grantResults));
        Log.e(TAG, "onRequestPermissionsResult: " + Arrays.asList(requestCode));

    }

    @Override
    public void onPermissionResult(boolean isGranted) {

    }


}