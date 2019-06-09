package com.rithikjain.loopbox;

import android.app.Activity;
import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.util.UUID;

class HandleAudio {

    private Context mContext;

    // Defining the variables that will help in performing different functions with the same button
    private int[] counter = new int[6];
    private boolean[] isRecorded = new boolean[6];

    // Defining a variable loopDuration for all loops;
    private int[] loopDuration = new int[6];

    // Defining variable to take care of multiple recording not occurring at once
    private boolean isRecording = false;

    // Defining MediaRecorder & MediaPlayer for each different loop
    private MediaRecorder mMediaRecorder;
    private MediaPlayer[] mMediaPlayer = new MediaPlayer[6];

    // Defining name of audio files for each different loops
    private String[] mFileName = new String[6];

    // Defining variable to take care for deleting loop at right time
    private boolean[] loopExists = new boolean[6];

    // Defining all the ImageViews
    private ImageView[] mLoop;

    // Defining a handler to handle the threads
    private Handler mHandler = new Handler();

    // Defining variable array to assign all progress bars from the layout
    private ProgressBar[] mProgressBar;

    // Defining variable for Main Activity
    private Activity mainActivity;

    HandleAudio (Context context, Activity mainActivity, ImageView[] mLoop, ProgressBar[] mProgressBar){
        mContext = context;
        this.mLoop = mLoop;
        this.mProgressBar = mProgressBar;
        this.mainActivity = mainActivity;
    }

    void init(){
        // Initialising all counters to 0 and progressStatus to 0
        for(int i = 0; i<6; i++){
            counter[i] = 0;
        }

        // Initialising all isRecorded and loopExists booleans to false
        for(int j = 0; j<6; j++){
            isRecorded[j] = false;
            loopExists[j] = false;
        }

        // Creating a folder where temp loops can be stored
        File file = new File(Environment.getExternalStorageDirectory() + "/Loop Box/Loops");
        if(!file.exists()){
            boolean mkdir = file.mkdirs();
            if(!mkdir) {
                Toast.makeText(mContext, "Directory creattion failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void loopLogic(int n){
        if (counter[n] % 2 == 0 && !isRecorded[n] && !isRecording) {
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

            Toast.makeText(mContext, "Stopped Recording & Playing Audio", Toast.LENGTH_SHORT).show();
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

    void deleteLoop(int n){
        if(loopExists[n]) {
            mMediaPlayer[n].stop();
            mMediaPlayer[n].release();
            mMediaPlayer[n] = null;
            isRecorded[n] = false;
            counter[n] = 0;

            // Changing loop image to deactivated image
            mLoop[n].setImageResource(R.drawable.loop_deactive);

            // Setting status of loop exists to false
            loopExists[n] = false;

            // Setting the progress of the progress bar to 0
            mProgressBar[n].setProgress(0);

            Toast.makeText(mContext, "Loop deleted!", Toast.LENGTH_SHORT).show();
        }
    }

    private void recordAudio(int n){
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
            Toast.makeText(mContext, "Recording Started", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
        }

        // Set status of loopExists to true
        loopExists[n] = true;

        // Getting the length of the loop
        loopDuration[n] = getLoopLength(n);
    }

    private void playAudio(int n){
        try {
            mMediaPlayer[n].setLooping(true);
            mMediaPlayer[n].start();
            Toast.makeText(mContext, "Playing Audio", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(mContext, "Playback Error", Toast.LENGTH_SHORT).show();
        }

        // Changing loop image to playing image
        mLoop[n].setImageResource(R.drawable.loop_playing);

        // Starting the progress bar
        startProgressBar(n);
    }

    private void pauseAudio(int n){
        Toast.makeText(mContext, "Pausing Audio", Toast.LENGTH_SHORT).show();
        mMediaPlayer[n].pause();
        mMediaPlayer[n].seekTo(0);

        // Changing loop image to deactivated image
        mLoop[n].setImageResource(R.drawable.loop_deactive);

        // Changing the progress bar level to 0
        mProgressBar[n].setProgress(0);
    }

    // Making a function to return the duration of the respective loops
    private int getLoopLength(int n){
        Uri uri = Uri.parse(mFileName[n]);
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(mContext, uri);
        String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        return Integer.parseInt(durationStr);
    }

    // Making a function to sync the progress bar with the loop
    private void startProgressBar(final int n){
        mProgressBar[n].setMax(loopDuration[n]);

        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mMediaPlayer[n] != null){
                    int mCurrentPosition = mMediaPlayer[n].getCurrentPosition();
                    mProgressBar[n].setProgress(mCurrentPosition);
                }
                mHandler.postDelayed(this, 50);
            }
        });
    }

}
