package com.shalom.itai.theservantexperience.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.shalom.itai.theservantexperience.R;
import com.shalom.itai.theservantexperience.services.BuggerService;
import com.shalom.itai.theservantexperience.utils.Constants;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.GET_ACCOUNTS;
import static android.Manifest.permission.INSTALL_SHORTCUT;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.RECEIVE_BOOT_COMPLETED;
import static android.Manifest.permission.SYSTEM_ALERT_WINDOW;
import static android.Manifest.permission.VIBRATE;
import static android.Manifest.permission.WAKE_LOCK;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_SETTINGS;

public class PermissionActivity extends AppCompatActivity {
    private String[] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA,
            Manifest.permission.READ_CONTACTS, Manifest.permission.SEND_SMS, GET_ACCOUNTS,
            Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR, RECEIVE_BOOT_COMPLETED,
            VIBRATE, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE, ACCESS_COARSE_LOCATION, READ_PHONE_STATE,
            ACCESS_WIFI_STATE, INTERNET, WRITE_SETTINGS, WAKE_LOCK, SYSTEM_ALERT_WINDOW, INSTALL_SHORTCUT};

    private static String failedPermission = null;

    private static final int REQUESTS = 100;
    public final static int REQUEST_OVERLAY = 1231;
    public final static int REQUEST_DENIED_PERMISSION = 1232;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        ActivityCompat.requestPermissions(this, permissions, REQUESTS);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUESTS:
                boolean[] arrOfPermissions = new boolean[grantResults.length];
                for (int i = 0; i < arrOfPermissions.length; i++) {
                    arrOfPermissions[i] = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    String permission = permissions[i];
                    if (!arrOfPermissions[i]) {
                        boolean showRationale = false;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            showRationale = shouldShowRequestPermissionRationale(permission);
                        }
                        if (!showRationale) {
                            startSettings(REQUEST_DENIED_PERMISSION, Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            /*Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivityForResult(intent, REQUEST_DENIED_PERMISSION);*/
                            return;
                            // user also CHECKED "never ask again"
                            // you can either enable some fall back,
                            // disable features of your app
                            // or open another dialog explaining
                            // again the permission and directing to
                            // the app settingi
                        } else {
                            failedPermission = permission;
                            popUpForRequest(null,0);
                            // this.showDialog();
                            return;
                            // user did NOT check "never ask again"
                            // this is a good place to explain the user
                            // why you need the permission and ask if he wants
                            // to accept it (the rationale)
                        }
                    }
                }
                checkDrawOverlayPermission();
                break;
            case REQUESTS + 1:
                //       permissionToSettings = grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }


        //   checkDrawOverlayPermission();
    }


    private void popUpForRequest(final Intent intent, final int code) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(R.string.permission_requested_message)
                .setTitle(R.string.alert_dialog_Permission_Request);
        // Add the buttons
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (intent != null)
                    startActivityForResult(intent, code);
                else
                    ActivityCompat.requestPermissions(PermissionActivity.this, permissions, REQUESTS);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void createShortcut() {
        Intent intentShortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        intentShortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));
        Parcelable appicon = Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.jon_png);
        intentShortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, appicon);
        intentShortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(getApplicationContext(), MainActivity.class));
        intentShortcut.putExtra("duplicate", false);
        sendBroadcast(intentShortcut);
    }

    private void startSettings(final int code, String settings) {
        final Intent intent = new Intent(settings,
                Uri.parse("package:" + getPackageName()));
        /** request permission via start activity for result */
        popUpForRequest(intent,code);

    }

    public void checkDrawOverlayPermission() {
        /** check if we already  have permission to draw over other apps */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                /** if not construct intent to request permission */
                startSettings(REQUEST_OVERLAY, Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            } else {
                createShortcut();
                startService(new Intent(this, BuggerService.class).putExtra(Constants.JonIntents.UPD_BUG_RUN_MAIN, true));
                finish();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /** check if received result code
         is equal our requested code for draw permission  */

        switch (requestCode) {
            case REQUEST_OVERLAY:
       /* if so check once again if we have permission */
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (Settings.canDrawOverlays(this)) {
                        createShortcut();
                        startService(new Intent(this, BuggerService.class).putExtra(Constants.JonIntents.UPD_BUG_RUN_MAIN, true));
                        finish();
                        //   BuggerService.startOverly = true;
                        // continue here - permission was granted
                    } else {
                        startSettings(REQUEST_OVERLAY, Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    }
                }
                break;
            case REQUEST_DENIED_PERMISSION:
                ActivityCompat.requestPermissions(this, permissions, REQUESTS);
                break;
        }
    }
/*
    @Override
    public void doPositive() {
        ActivityCompat.requestPermissions(this, permissions, REQUESTS);
    }

    @Override
    public void doNegative() {
        finish();
    }

    @Override
    public void showDialog() {
        String[] per = failedPermission.split("\\.");
        String headPer = per[per.length - 1] + " is required, can't work without it";
        DialogFragment newFragment = MyAlertDialogFragment
                .newInstance(headPer, "Ok", "Cancel", getClass().getName());
        newFragment.show(getSupportFragmentManager(), "dialog");
    }
*/
}
