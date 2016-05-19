package com.makakaton.dm.ac.tec.makakaton;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

public class Multiplayer extends AppCompatActivity {

    Typeface font;
    MediaPlayer introMusic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);

        font = Typeface.createFromAsset(getAssets(), "fonts/Toshiko.ttf");

//        introMusic = MediaPlayer.create(this, R.raw.intro_song);
//        introMusic.seekTo(getIntent().getIntExtra("seconds", 0));
//        introMusic.setLooping(true);
//        introMusic.start();


        TextView textViewDifficulty = (TextView) findViewById(R.id.txtDifficulty);
        textViewDifficulty.setTypeface(font);
        TextView txtMulti = (TextView) findViewById(R.id.txtM);
        txtMulti.setTypeface(font);
        TextView txtParty = (TextView) findViewById(R.id.txtParty);
        txtParty.setTypeface(font);

        TextView txtP1 = (TextView) findViewById(R.id.txtP1);
        txtP1.setTypeface(font);
        TextView txtP2 = (TextView) findViewById(R.id.txtP2);
        txtP2.setTypeface(font);
        TextView txtP3 = (TextView) findViewById(R.id.txtP3);
        txtP3.setTypeface(font);
        TextView txtP4 = (TextView) findViewById(R.id.txtP4);
        txtP4.setTypeface(font);


        Button btnServer = (Button) findViewById(R.id.btnServer);
        btnServer.setTypeface(font);
        Button btnJoin = (Button) findViewById(R.id.btnJoin);
        btnJoin.setTypeface(font);

        Integer gameTypeData = getIntent().getIntExtra("level",R.string.level1);
        textViewDifficulty.setText(getString(gameTypeData));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(Multiplayer.this, Music.class);
        startService(intent);
//        introMusic.start();
    }
}
