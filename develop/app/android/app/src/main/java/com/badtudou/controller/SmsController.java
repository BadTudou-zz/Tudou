package com.badtudou.controller;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Telephony;
import android.support.v4.app.ActivityCompat;

import com.badtudou.util.Util;

/**
 * Created by badtudou on 11/05/2017.
 */

public class SmsController{
        private Activity activity;

        public SmsController(Activity activity) {
            this.activity = activity;
            Util.PermissionRequire(activity, Manifest.permission.SEND_SMS);
        }

        public void sendSms(String phone, String text) {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phone));
            intent.putExtra(Telephony.Sms.BODY, text);
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.SEND_SMS}, 1);
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
            }
            activity.getApplication().startActivity(intent);
        }
}
