package com.makakaton.dm.ac.tec.makakaton;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class Menu extends AppCompatActivity {

    MediaPlayer introMusic;
    Typeface font;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        font = Typeface.createFromAsset(getAssets(), "fonts/Toshiko.ttf");

        Button btnRush = (Button) findViewById(R.id.btnRush);
        Button btnSingle = (Button) findViewById(R.id.btnSingle);
        Button btnMulti = (Button) findViewById(R.id.btnMulti);
        Button btnAbout = (Button) findViewById(R.id.btnAbout);

        btnRush.setTypeface(font);
        btnSingle.setTypeface(font);
        btnMulti.setTypeface(font);
        btnAbout.setTypeface(font);

        //introMusic = MediaPlayer.create(this, R.raw.intro_song);
        /*introMusic.seekTo(getIntent().getIntExtra("seconds", 0));
        introMusic.setLooping(true);*/
        ////introMusic.start();
    }

    public void gameMode(View view) {
        Intent intent = new Intent(this, Level.class);
        switch (view.getId()){
            case R.id.btnRush:
                intent.putExtra("gameMode",R.string.rush);
                break;
            case R.id.btnSingle:
                intent.putExtra("gameMode",R.string.single);
                break;
            case R.id.btnMulti:
                intent.putExtra("gameMode",R.string.multi);
                break;


        }
        //intent.putExtra("seconds", introMusic.getCurrentPosition());
        //introMusic.stop();
        startActivity(intent);
    }

    public void about(View view){
        Intent intent = new Intent(this, About.class);
        startActivity(intent);
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
        Intent intent = new Intent(Menu.this, Music.class);
        startService(intent);
//        introMusic.start();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //introMusic = MediaPlayer.create(this, R.raw.intro_song);
        //introMusic.setLooping(true);
        //introMusic.start();
    }


}
