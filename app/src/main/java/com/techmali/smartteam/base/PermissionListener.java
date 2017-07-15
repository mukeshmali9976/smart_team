package com.techmali.smartteam.base;



public interface PermissionListener {

    void permissionGranted(String permission);

    void permissionDenied(String permission);

    void permissionForeverDenied(String permission);
}
