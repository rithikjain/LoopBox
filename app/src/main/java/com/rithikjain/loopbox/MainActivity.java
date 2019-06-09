package com.rithikjain.loopbox;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Defining all the imageViews
    private ImageView[] mLoop = new ImageView[6];

    // Defining variable array to assign all progress bars from the layout
    private ProgressBar[] mProgressBar = new ProgressBar[6];

    // Defining other helper classes
    private HandleAudio mHandleAudio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHandleAudio = new HandleAudio(getApplicationContext(), MainActivity.this, mLoop, mProgressBar);
        mHandleAudio.init();

        RequestPermission mRequestPermission = new RequestPermission(getApplicationContext(), MainActivity.this);

        // Requesting Mic Permission
        mRequestPermission.checkAndRequestPermission();

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

        // Assigning all the ProgressBars to the respective ProgressBars in the layout
        mProgressBar[0] = findViewById(R.id.progress1);
        mProgressBar[1] = findViewById(R.id.progress2);
        mProgressBar[2] = findViewById(R.id.progress3);
        mProgressBar[3] = findViewById(R.id.progress4);
        mProgressBar[4] = findViewById(R.id.progress5);
        mProgressBar[5] = findViewById(R.id.progress6);

        // OnClick Functions on the loops
        /*  The single button will perform different functions such as Recording, Playing and Pausing.
            The logic is that once the button has been pressed it will check if recording has been done or not,
            and if it hasn't been done then it will record, otherwise it will ignore the Record function
            and just move onto the Play and Pause function accordingly.
         */
        mLoop[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandleAudio.loopLogic(0);
            }
        });

        mLoop[0].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mHandleAudio.deleteLoop(0);

                return true;
            }
        });

        mLoop[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandleAudio.loopLogic(1);
            }
        });

        mLoop[1].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mHandleAudio.deleteLoop(1);

                return true;
            }
        });

        mLoop[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandleAudio.loopLogic(2);
            }
        });

        mLoop[2].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mHandleAudio.deleteLoop(2);

                return true;
            }
        });

        mLoop[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandleAudio.loopLogic(3);
            }
        });

        mLoop[3].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mHandleAudio.deleteLoop(3);

                return true;
            }
        });

        mLoop[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandleAudio.loopLogic(4);
            }
        });

        mLoop[4].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mHandleAudio.deleteLoop(4);

                return true;
            }
        });

        mLoop[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandleAudio.loopLogic(5);
            }
        });

        mLoop[5].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mHandleAudio.deleteLoop(5);

                return true;
            }
        });

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //Permission related variables
        int MIC_PERMISSION_CODE = 1;
        if (requestCode == MIC_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission not granted!", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
