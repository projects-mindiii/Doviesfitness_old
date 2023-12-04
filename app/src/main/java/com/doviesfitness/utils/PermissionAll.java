package com.doviesfitness.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionAll {
    public boolean RequestMultiplePermission(Activity context) {
        int FirstPermissionResult = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        int FourthPermissionResult = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (FirstPermissionResult != PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult != PackageManager.PERMISSION_GRANTED
                 && FourthPermissionResult != PackageManager.PERMISSION_GRANTED
                && ThirdPermissionResult != PackageManager.PERMISSION_GRANTED)
        {
            // Creating String Array with Permissions.
            ActivityCompat.requestPermissions((Activity) context, new String[]
                    {
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.RECORD_AUDIO
                    }, Constant.Companion.getREQUEST_ID_MULTIPLE_PERMISSIONS());
            return false;
        } else {
            return true;
        }
    }
}
