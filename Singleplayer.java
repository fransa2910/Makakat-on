package com.makakaton.dm.ac.tec.makakaton;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class Singleplayer extends AppCompatActivity {

    private Integer gameMode;
    private Integer gameDifficulty;
    MediaPlayer introMusic;
    Typeface font;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singleplayer);

//        introMusic = MediaPlayer.create(this, R.raw.intro_song);
//        introMusic.seekTo(getIntent().getIntExtra("seconds", 0));
//        introMusic.setLooping(true);
//        introMusic.start();

        font = Typeface.createFromAsset(getAssets(), "fonts/Toshiko.ttf");

        TextView textViewDifficulty = (TextView) findViewById(R.id.txtDifficulty);
        TextView txtMode = (TextView) findViewById(R.id.txtMode);
        gameDifficulty = getIntent().getIntExtra("gameDifficulty",R.string.level1);
        gameMode = getIntent().getIntExtra("gameMode",R.string.single);


        textViewDifficulty.setText(getString(gameDifficulty));
        txtMode.setText(getString(gameMode));

        Button btnHighScores = (Button) findViewById(R.id.btnSingleHighScores);
        Button btnStart = (Button) findViewById(R.id.btnStart);


        textViewDifficulty.setTypeface(font);
        txtMode.setTypeface(font);
        btnHighScores.setTypeface(font);
        btnStart.setTypeface(font);

        btnHighScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHighscores();
            }
        });

    }

    public void showHighscores() {
        Intent intent = new Intent(this, Highscores.class);
        intent.putExtra("gameMode", gameMode);
        intent.putExtra("gameDifficulty", gameDifficulty);
        startActivity(intent);

    }
    public void startGame(View v){
        Intent intent = new Intent(this, MainActivityVR.class);
        intent.putExtra("gameMode", gameMode);
        intent.putExtra("gameDifficulty", gameDifficulty);
        //introMusic.stop();
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
        Intent intent = new Intent(Singleplayer.this, Music.class);
        startService(intent);
//        introMusic.start();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        introMusic = MediaPlayer.create(this, R.raw.intro_song);
//        introMusic.setLooping(true);
//        introMusic.start();
    }

}
