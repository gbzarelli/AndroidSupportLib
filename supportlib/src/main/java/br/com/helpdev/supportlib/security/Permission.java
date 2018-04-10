package br.com.grupocriar.swapandroid.security;

import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import java.util.Arrays;

/**
 * Created by Guilherme Biff Zarelli on 12/11/17.
 */

public class Permission {
    private static final int REQUEST_PERMISSIONS = 1;
    private AppCompatActivity activity;
    private String[] permissions;

    public Permission(AppCompatActivity activity, String[] permissions) {
        this.activity = activity;
        this.permissions = permissions;
    }

    public boolean verifyPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String perm : permissions) {
                if (activity.checkSelfPermission(perm) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean verifyAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!verifyPermissions()) {
                ActivityCompat.requestPermissions(activity, permissions, REQUEST_PERMISSIONS);
                return false;
            }
        }
        return true;
    }

    public boolean onForceRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS) {
            for (int permissionResult : grantResults) {
                if (permissionResult != PackageManager.PERMISSION_GRANTED) {
                    verifyAndRequestPermissions();
                    return false;
                }
            }
        }
        return true;
    }
}
