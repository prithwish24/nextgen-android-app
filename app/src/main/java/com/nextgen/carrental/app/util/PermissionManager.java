package com.nextgen.carrental.app.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * This class used to get requested permission from user
 * @author  Prithwish
 */

public final class PermissionManager {

    public interface CallbackHandler {
        void onResponse(final int requestCode, final Activity activity);
    }

    public boolean hasPermission (Context context, String permission) {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission (
                context, permission);
    }

    public void requestPermission(final String[] permissions,
                                  final Activity activity,
                                  final CallbackHandler callbackHandler) {
        final int requestCode = activity.getTaskId();
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        boolean shouldShowRequestPermissionRationale = false;
        for (String permission : permissions) {
            permissionCheck = permissionCheck + ContextCompat.checkSelfPermission (
                    activity.getApplicationContext(), permission);
            shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale
                    || ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
        }

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            /*if (shouldShowRequestPermissionRationale) {
                Snackbar.make(activity.findViewById(android.R.id.content), stringId,
                        Snackbar.LENGTH_INDEFINITE).setAction("GRANT",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(BaseActivity.this, requestedPermissions, requestCode);
                            }
                        }).show();
            } else {
                ActivityCompat.requestPermissions(this, requestedPermissions, requestCode);
            }*/

            ActivityCompat.requestPermissions(activity, permissions, requestCode);

        } else {
            callbackHandler.onResponse(requestCode, activity);
        }
    }

}
