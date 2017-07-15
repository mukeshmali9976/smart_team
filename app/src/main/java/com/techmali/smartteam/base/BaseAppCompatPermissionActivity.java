package com.techmali.smartteam.base;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;


import com.techmali.smartteam.R;
import com.techmali.smartteam.utils.DialogHelper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class BaseAppCompatPermissionActivity extends BaseAppCompatActivity {

    private final int KEY_PERMISSION = 200;
    private PermissionListener permissionListener;
    private String permissionsAsk[];

    /**
     * @param context    current Context
     * @param permission String permission to ask
     * @return boolean true/false
     */
    public boolean isPermissionGranted(Context context, String permission) {
        boolean flag = (((Build.VERSION.SDK_INT < Build.VERSION_CODES.M) || (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED)));
        Log.e(TAG, permission + " : " + flag);
        return flag;
    }

    /**
     * @param context     current Context
     * @param permissions String[] permission to ask
     * @return boolean true/false
     */
    public boolean isPermissionsGranted(Context context, String permissions[]) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;

        boolean granted = true;

        for (String permission : permissions) {
            if (!(ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED))
                granted = false;
        }
        return granted;
    }

    private void internalRequestPermission(String[] permissionAsk) {
        String arrayPermissionNotGranted[];
        ArrayList<String> permissionsNotGranted = new ArrayList<>();

        for (int i = 0; i < permissionAsk.length; i++) {
            if (!isPermissionGranted(BaseAppCompatPermissionActivity.this, permissionAsk[i])) {
                permissionsNotGranted.add(permissionAsk[i]);
            } else {
                if (permissionListener != null) {
                    permissionListener.permissionGranted(permissionAsk[i]);
                }
            }
        }

        if (!permissionsNotGranted.isEmpty()) {
            arrayPermissionNotGranted = new String[permissionsNotGranted.size()];
            arrayPermissionNotGranted = permissionsNotGranted.toArray(arrayPermissionNotGranted);
            ActivityCompat.requestPermissions(BaseAppCompatPermissionActivity.this, arrayPermissionNotGranted, KEY_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode != KEY_PERMISSION) {
            return;
        }

        List<String> permissionDenied = new LinkedList<>();

        for (int i = 0; i < grantResults.length; i++) {

            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                permissionDenied.add(permissions[i]);
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {
                    permissionListener.permissionForeverDenied(permissions[i]);
                    return;
                } else {
                    permissionListener.permissionDenied(permissions[i]);
                }
            } else {
                permissionListener.permissionGranted(permissions[i]);
            }
        }
    }

    /**
     * @param permission         String permission which needs to be ask
     * @param permissionListener callback PermissionResult
     */
    public void askForPermission(String permission, PermissionListener permissionListener) {
        permissionsAsk = new String[]{permission};
        this.permissionListener = permissionListener;
        internalRequestPermission(permissionsAsk);
    }

    /**
     * @param permissions        String[] permissions which needs to be ask
     * @param permissionListener callback PermissionResult
     */
    public void askForPermissions(String permissions[], PermissionListener permissionListener) {
        permissionsAsk = permissions;
        this.permissionListener = permissionListener;
        internalRequestPermission(permissionsAsk);

    }

    /**
     * @param context     current context
     * @param denyMessage message display while user deny permission
     */
    public void openSettingsApp(final Context context, String denyMessage) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {

            DialogHelper.showDialog(context, null, denyMessage, false, getString(R.string.lbl_settings), getString(R.string.lbl_close), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {

                    try {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + context.getPackageName()));
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                        Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                        startActivity(intent);
                    }
                }
            }, null);
        }
    }
}
