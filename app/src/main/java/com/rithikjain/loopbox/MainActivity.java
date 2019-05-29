package com.rithikjain.loopbox;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    // Defining the variables that will help in performing different functions with the same button
    private int[] counter = new int[6];

    private boolean[] isRecorded = new boolean[6];

    // Defining variable to take care of multiple recording not occurring at once
    private boolean isRecording = false;

    // Defining MediaRecorder & MediaPlayer for each different loop
    MediaRecorder mMediaRecorder;
    MediaPlayer[] mMediaPlayer = new MediaPlayer[6];

    // Defining name of audio files for each different loops
    String[] mFileName = new String[6];

    // Defining all the imageViews
    ImageView[] mLoop = new ImageView[6];

    // Permission related variables
    private int MIC_PERMISSION_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialising all counters to 0
        for(int i = 0; i<6; i++) counter[i] = 0;

        // Initialising all isRecorded booleans to false
        for(int j = 0; j<6; j++) isRecorded[j] = false;

        // Creating a folder where temp loops can be stored
        File file = new File(Environment.getExternalStorageDirectory() + "/Loop Box/Loops");
        if(!file.exists()) file.mkdirs();

        // Requesting Mic Permission
        checkAndRequestPermission();

        // Making the Window fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Assigning all the ImageViews to the respective ImageViews in the layout
        // Making the ImageViews member variables
        mLoop[0] = findViewById(R.id.loop1);
        mLoop[1] = findViewById(R.id.loop2);
        mLoop[2] = findViewById(R.id.loop3);
        mLoop[3] = findViewById(R.id.loop4);
        mLoop[4] = findViewById(R.id.loop5);
        mLoop[5] = findViewById(R.id.loop6);

        // OnClick Functions on the loop1
        /*  The single button will perform different functions such as Recording, Playing and Pausing.
            The logic is that once the button has been pressed it will check if recording has been done or not,
            and if it hasn't been done then it will record, otherwise it will ignore the Record function
            and just move onto the Play and Pause function accordingly.
         */
        mLoop[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loopLogic(0);
            }
        });

        mLoop[0].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                deleteLoop(0);

                return true;
            }
        });

        mLoop[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loopLogic(1);
            }
        });

        mLoop[1].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                deleteLoop(1);

                return true;
            }
        });

        mLoop[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loopLogic(2);
            }
        });

        mLoop[2].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                deleteLoop(2);

                return true;
            }
        });

        mLoop[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loopLogic(3);
            }
        });

        mLoop[3].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                deleteLoop(3);

                return true;
            }
        });

        mLoop[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loopLogic(4);
            }
        });

        mLoop[4].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                deleteLoop(4);

                return true;
            }
        });

        mLoop[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loopLogic(5);
            }
        });

        mLoop[5].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                deleteLoop(5);

                return true;
            }
        });
    }

    // Checking Or asking for permission
    private void checkAndRequestPermission(){
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
        ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            return;
        } else {
            requestMicPermission();
        }
    }

    // Asking for permission
    private void requestMicPermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)){
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed!")
                    .setMessage("Microphone Access is needed to record audio! And Storage Access to access the audio!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MIC_PERMISSION_CODE);
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
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MIC_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MIC_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission not granted!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loopLogic(int n){
        if (counter[n] % 2 == 0 && !isRecorded[n] && !isRecording) {
            // Checking if permission has been granted before looping
            checkAndRequestPermission();

            recordAudio(n);
            isRecording = true;
            counter[n]++;
        }
        else if (counter[n] % 2 == 1 && !isRecorded[n]){
            stopRecording(n);
            playAudio(n);
            isRecorded[n] = true;
            isRecording = false;
            counter[n]++;

            Toast.makeText(getApplicationContext(), "Stopped Recording & Playing Audio", Toast.LENGTH_SHORT).show();
        }
        else if (counter[n] % 2 == 0 && isRecorded[n]){
            pauseAudio(n);
            counter[n]++;
        }
        else if (counter[n] % 2 == 1 && isRecorded[n]){
            playAudio(n);
            counter[n]++;
        }
    }


    private void deleteLoop(int x){
        mMediaPlayer[x].stop();
        mMediaPlayer[x].release();
        mMediaPlayer[x] = null;
        isRecorded[x] = false;
        counter[x] = 0;

        // Changing loop image to deactivated image
        mLoop[x].setImageResource(R.drawable.loop_deactive);

        Toast.makeText(getApplicationContext(), "Loop deleted!", Toast.LENGTH_SHORT).show();
    }


    private void recordAudio(int n){
        // Checking if permission has been granted before looping
        checkAndRequestPermission();

        // Generating a new filename
        mFileName[n] = Environment.getExternalStorageDirectory() + "/Loop Box/Loops/" + UUID.randomUUID().toString() + "_loop.3gp";

        // Setting up the MediaRecorder for each loop respectively
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mMediaRecorder.setAudioEncodingBitRate(128000);
        mMediaRecorder.setAudioSamplingRate(44100);
        mMediaRecorder.setOutputFile(mFileName[n]);

        // Start to Record Audio
        try {
            mMediaRecorder.prepare();
            mMediaRecorder.start();
            Toast.makeText(getApplicationContext(), "Recording Started", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }

        // Changing the loop image to recording image
        mLoop[n].setImageResource(R.drawable.loop_recording);
    }

    private void stopRecording(int n){
        mMediaRecorder.stop();
        mMediaRecorder.release();
        mMediaRecorder = null;

        // Making the MediaPlayer available for playback of audio
        mMediaPlayer[n] = new MediaPlayer();
        try {
            mMediaPlayer[n].setDataSource(mFileName[n]);
            mMediaPlayer[n].prepare();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void playAudio(int n){
        try {
            mMediaPlayer[n].setLooping(true);
            mMediaPlayer[n].start();
            Toast.makeText(getApplicationContext(), "Playing Audio", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Playback Error", Toast.LENGTH_SHORT).show();
        }

        // Changing loop image to playing image
        mLoop[n].setImageResource(R.drawable.loop_playing);
    }

    private void pauseAudio(int n){
        Toast.makeText(getApplicationContext(), "Pausing Audio", Toast.LENGTH_SHORT).show();
        mMediaPlayer[n].pause();
        mMediaPlayer[n].seekTo(0);

        // Changing loop image to deactivated image
        mLoop[n].setImageResource(R.drawable.loop_deactive);
    }

}
