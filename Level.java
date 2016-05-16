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

public class Level extends AppCompatActivity {

    private Integer gameMode;
    MediaPlayer introMusic;
    Typeface font;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

//        introMusic = MediaPlayer.create(this, R.raw.intro_song);
//        introMusic.seekTo(getIntent().getIntExtra("seconds", 0));
//        introMusic.setLooping(true);
//        introMusic.start();

        font = Typeface.createFromAsset(getAssets(), "fonts/Toshiko.ttf");
        TextView textViewGameName = (TextView) findViewById(R.id.txtGameType);
        gameMode = getIntent().getIntExtra("gameMode", R.string.single);
        textViewGameName.setText(getString(gameMode));
        textViewGameName.setTypeface(font);

        Button btnFacil = (Button) findViewById(R.id.btnFacil);
        Button btnMedio = (Button) findViewById(R.id.btnMedio);
        Button btnAvanzado = (Button) findViewById(R.id.btnAvanzado);

        btnFacil.setTypeface(font);
        btnMedio.setTypeface(font);
        btnAvanzado.setTypeface(font);

        btnFacil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelSelect(R.string.level1);

            }
        });

        btnMedio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelSelect(R.string.level2);
            }
        });

        btnAvanzado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                levelSelect(R.string.level3);
            }
        });
    }

    public void levelSelect(Integer id) {
        Intent intentMultiplayer = new Intent(this, MultiplayerVR.class);
        Intent intentSingleplayer = new Intent(this, Singleplayer.class);
        TextView mode = (TextView) findViewById(R.id.txtGameType);
        switch (getString(getIntent().getIntExtra("gameMode", R.string.rush))){
            case "RUSH":
                mode.setText(getResources().getString(R.string.rush));
                intentSingleplayer.putExtra("gameDifficulty", id);
                intentSingleplayer.putExtra("gameMode", gameMode);
//                intentSingleplayer.putExtra("seconds", introMusic.getCurrentPosition());
//                introMusic.stop();
                startActivity(intentSingleplayer);
                break;
            case "MULTIPLAYER":
                intentMultiplayer.putExtra("level",id);
//                intentMultiplayer.putExtra("seconds", introMusic.getCurrentPosition());
//                introMusic.stop();
                startActivity(intentMultiplayer);
                break;
            case "SINGLEPLAYER":
                mode.setText(getResources().getString(R.string.single));
                intentSingleplayer.putExtra("gameDifficulty",id);
                intentSingleplayer.putExtra("gameMode", gameMode);
//                intentSingleplayer.putExtra("seconds", introMusic.getCurrentPosition());
//                introMusic.stop();
                startActivity(intentSingleplayer);
                break;
        }

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
        Intent intent = new Intent(Level.this, Music.class);
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
