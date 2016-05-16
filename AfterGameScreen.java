package com.makakaton.dm.ac.tec.makakaton;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AfterGameScreen extends AppCompatActivity {

    String msg;
    int score;
    int gameMode;
    Intent intent;
    int gameDifficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_game_screen);

        score = getIntent().getIntExtra("score", 0);
        gameMode = getIntent().getIntExtra("gameMode", R.string.rush);
        gameDifficulty = getIntent().getIntExtra("gameDificulty", R.string.level1);

        if(gameMode == R.string.rush) {
            msg = "Se terminó el tiempo!";
        } else if(gameMode == R.string.single){
            msg = "Te has quedado sin vidas!";
        }

        msg += "\nPuntuación: " + String.valueOf(score) + "\nDeseas volver a jugar?";


        new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText("Ha terminado el juego!!")
                .setContentText(msg)
                .setCustomImage(R.drawable.makakaton_icon)
                .setCancelText("No")
                .setConfirmText("Si")
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                        playAgain();
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                        goToStartScreen();
                    }
                })

                .show();
    }

    private void goToStartScreen() {
        intent = new Intent(this, MainActivity.class);
        finish();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToStartScreen();
        finish();

    }

    private void playAgain() {
        intent = new Intent(this, MainActivityVR.class);
        intent.putExtra("gameMode", gameMode);
        intent.putExtra("gameDifficulty", gameDifficulty);
        finish();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
        Intent intent = new Intent(AfterGameScreen.this, Music.class);
        startService(intent);
//        introMusic.start();
    }
}