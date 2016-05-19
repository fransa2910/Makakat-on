package com.makakaton.dm.ac.tec.makakaton;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class About extends AppCompatActivity {

    MediaPlayer introMusic;

    Button btnEE;
    boolean eeStart;
    MediaPlayer eePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        eeStart = false;

        eePlayer = new MediaPlayer();
        eePlayer.create(this, R.raw.ee);
        btnEE = (Button) findViewById(R.id.btnFullHD);
        btnEE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ee();

            }
        });

       /* introMusic = MediaPlayer.create(this, R.raw.intro_song);
        introMusic.seekTo(getIntent().getIntExtra("seconds", 0));
        introMusic.setLooping(true);
        introMusic.start();*/

    }


    @Override
    protected void onPause() {
        Context context = getApplicationContext();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        if (!taskInfo.isEmpty()) {
            ComponentName topActivity = taskInfo.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                stopService(new Intent(this, Music.class));
            }
        }
        super.onPause();
        //introMusic.stop();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //introMusic.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(About.this, Music.class);
        startService(intent);
//        introMusic.start();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        /*introMusic = MediaPlayer.create(this, R.raw.intro_song);
        introMusic.setLooping(true);
        introMusic.start();*/
    }

    public void ee() {

        if(eeStart) {
            eePlayer.setLooping(true);
            eePlayer.start();

        } else {
            eePlayer.stop();
        }
        eeStart = ! eeStart;
    }

}
