package com.techmali.smartteam.base;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;

import com.techmali.smartteam.R;
import com.techmali.smartteam.utils.DialogHelper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class BasePermissionFragment extends BaseFragment {

    private final int KEY_PERMISSION = 200;
    private PermissionListener permissionListener;
    private String permissionsAsk[];
    private String mTag;

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setRetainInstance(false);
    }

    /**
     * @param context    current Context
     * @param permission String permission to ask
     * @return boolean true/false
     */
    public boolean isPermissionGranted(Context context, String permission) {
        return ((Build.VERSION.SDK_INT < Build.VERSION_CODES.M) || (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED));
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
            if (!(ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED))
                granted = false;
        }

        return granted;
    }

    private void internalRequestPermission(String[] permissionAsk) {
        String arrayPermissionNotGranted[];
        ArrayList<String> permissionsNotGranted = new ArrayList<>();

        for (int i = 0; i < permissionAsk.length; i++) {
            if (!isPermissionGranted(getActivity(), permissionAsk[i])) {
                permissionsNotGranted.add(permissionAsk[i]);
            } else {
                if (permissionListener != null) {
                    permissionListener.permissionGranted(permissionAsk[i],mTag);
                }
            }
        }

        if (!permissionsNotGranted.isEmpty()) {
            arrayPermissionNotGranted = new String[permissionsNotGranted.size()];
            arrayPermissionNotGranted = permissionsNotGranted.toArray(arrayPermissionNotGranted);
            requestPermissions(arrayPermissionNotGranted, KEY_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode != KEY_PERMISSION) {
            return;
        }

        List<String> permissionDenied = new LinkedList<>();

        for (int i = 0; i < grantResults.length; i++) {

            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                permissionDenied.add(permissions[i]);
                if (!shouldShowRequestPermissionRationale(permissions[i])) {
                    permissionListener.permissionForeverDenied(permissions[i]);
                    return;
                } else {
                    permissionListener.permissionDenied(permissions[i]);
                }
            } else {
                permissionListener.permissionGranted(permissions[i], mTag);
            }
        }
    }

    public void askForPermissions(String permissions[], PermissionListener permissionListener,String  tag) {
        permissionsAsk = permissions;
        this.mTag = tag;
        this.permissionListener = permissionListener;
        internalRequestPermission(permissionsAsk);
    }

    /**
     * @param context current context
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
