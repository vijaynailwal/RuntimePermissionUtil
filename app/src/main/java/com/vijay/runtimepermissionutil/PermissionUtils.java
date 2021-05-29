package com.vijay.runtimepermissionutil;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionUtils {
    private static final String TAG = "PermissionUtils";
    private final int requestCode = 1001;
    private PermissionListener permissionListener;
    private Activity activity;
    private String permission;
    private String permission1;
    private String permission2;

    public static final String STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final String CAMERA = Manifest.permission.CAMERA;


    public PermissionUtils(Activity context) {
        this.activity = context;

    }

    public void askPermission(@NonNull final Activity context,
                              final String selPermission, @NonNull final PermissionListener permissionListener) {
        this.activity = context;
        this.permissionListener = permissionListener;
        permission = selPermission;
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{permission}, requestCode);
        } else {
            permissionListener.onPermissionResult(true);
        }
    }

    public void askPermissions(@NonNull final Activity context,
                               @NonNull final String permission1,
                               @NonNull final String permission2,
                               @NonNull final PermissionListener permissionListener) {
        this.activity = context;
        this.permissionListener = permissionListener;
        this.permission1 = permission1;
        this.permission2 = permission2;

        if (ContextCompat.checkSelfPermission(context, permission1) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, permission2) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{permission1, permission2}, requestCode);
        } else {
            permissionListener.onPermissionResult(true);
        }
    }

    public void onRequestPermissionsResult(final int requestCode, String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            permissionListener.onPermissionResult(true);
        } else if (grantResults.length > 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                permissionListener.onPermissionResult(true);
            } else {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED)
                    showAgainPermissionDialog(permission1);
                else
                    showAgainPermissionDialog(permission2);
            }
        } else {
            showAgainPermissionDialog(permission);
        }
    }

    private void showAgainPermissionDialog(@NonNull final String permission) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);
            alertBuilder.setCancelable(true);
            alertBuilder.setTitle(activity.getResources().getString(R.string.required_permission));
            switch (permission) {
                case STORAGE:
                    alertBuilder.setMessage(activity.getResources().getString(R.string.storagePermInfoMsg));
                    break;
                case CAMERA:
                    alertBuilder.setMessage(activity.getResources().getString(R.string.cameraPermInfoMsg));
                    break;
            }

            alertBuilder.setPositiveButton(activity.getResources().getString(R.string.grant), (dialog, which) -> ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode));
            alertBuilder.setNegativeButton(activity.getResources().getString(R.string.Deny), (dialog, which) -> {
                dialog.dismiss();
                permissionListener.onPermissionResult(false);
            });
            AlertDialog alert = alertBuilder.create();
            alert.show();
        } else {
            switch (permission) {
                case STORAGE:
                    Toast.makeText(activity, activity.getResources().getString(R.string.storagePermErrorMsg), Toast.LENGTH_LONG).show();
                    break;
                case CAMERA:
                    Toast.makeText(activity, activity.getResources().getString(R.string.cameraPermErrorMsg), Toast.LENGTH_LONG).show();
                    break;
            }

            permissionListener.onPermissionResult(false);
        }
    }

    public interface PermissionListener {
        void onPermissionResult(boolean isGranted);
    }

}