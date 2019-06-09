package com.rithikjain.loopbox;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

class RequestPermission {

    private Context mContext;
    private int MIC_PERMISSION_CODE = 1;
    private Activity mainActivity;

    RequestPermission(Context mContext, Activity mainActivity){
        this.mContext = mContext;
        this.mainActivity = mainActivity;
    }

    private void requestMicPermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(mainActivity, Manifest.permission.RECORD_AUDIO)){
            new AlertDialog.Builder(mContext)
                    .setTitle("Permission needed!")
                    .setMessage("Microphone Access is needed to record audio! And Storage Access to access the audio!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(mainActivity, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MIC_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create()
                    .show();
        } else {
            ActivityCompat.requestPermissions(mainActivity, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MIC_PERMISSION_CODE);
        }
    }

    // Checking Or asking for permission
    void checkAndRequestPermission(){
        if (ContextCompat.checkSelfPermission(mainActivity,
                Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            Log.d("Loop", "Permission is granted");
        } else {
            requestMicPermission();
        }
    }

}
