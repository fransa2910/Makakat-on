package com.makakaton.dm.ac.tec.makakaton;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class MainActivity extends Activity{

    MediaPlayer introMusic;
    Music mService;
    boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(MainActivity.this, Music.class);
        startService(intent);

//        introMusic = MediaPlayer.create(this, R.raw.intro_song);
//        introMusic.setLooping(true);
//        introMusic.start();
        TextView start = (TextView) findViewById(R.id.textView);
        Typeface fontToshiko = Typeface.createFromAsset(getAssets(), "fonts/Toshiko.ttf");
        start.setTypeface(fontToshiko);
        alphaAnimation(start);

    }
    @Override
    protected void onPause() {
        if (this.isFinishing()){ //basically BACK was pressed from this activity
            stopService(new Intent(this, Music.class));
        }
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
        //stopService(new Intent(this, Music.class));
        //introMusic.stop();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //stopService(new Intent(this, Music.class));
       // introMusic.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(MainActivity.this, Music.class);
        startService(intent);
//        introMusic.start();
    }

/*    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        stopService(new Intent(MainActivity.this, Music.class));
    }*/

    @Override
    protected void onRestart() {
        super.onRestart();

        /*introMusic = MediaPlayer.create(this, R.raw.intro_song);
        introMusic.setLooping(true);
        introMusic.start();*/
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, Music.class));
    }

    private void alphaAnimation(TextView textView) {
        Animation an = AnimationUtils.loadAnimation(this, R.anim.alpha);
        an.setRepeatCount(Animation.INFINITE);
        textView.startAnimation(an);
    }

    public void start(View view){
        Intent intent  = new Intent(this, Menu.class);
//        intent.putExtra("seconds", introMusic.getCurrentPosition());
 //       introMusic.stop();
        startActivity(intent);
    }
}
