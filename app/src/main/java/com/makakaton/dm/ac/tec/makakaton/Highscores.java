package com.makakaton.dm.ac.tec.makakaton;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.makakaton.dm.ac.tec.makakaton.R;

import java.util.List;

public class Highscores extends AppCompatActivity {
    int gameDifficulty;
    int gameMode;
    String[] savedScores;
    Typeface font;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscores);
        gameDifficulty = getIntent().getIntExtra("gameDifficulty",R.string.level1);
        gameMode = getIntent().getIntExtra("gameMode",R.string.single);

        font = Typeface.createFromAsset(getAssets(), "fonts/Toshiko.ttf");

        TextView txtTitle = (TextView) findViewById(R.id.txtHighscoresTitle);
        txtTitle.setTypeface(font);

        TextView txtIntro = (TextView) findViewById(R.id.txtTitleM);
        txtIntro.setTypeface(font);

        //get text view
        TextView scoreView = (TextView)findViewById(R.id.txtHighscores);
        scoreView.setTypeface(font);
        //get shared prefs
        SharedPreferences scorePrefs = getSharedPreferences(MainActivityVR.GAME_PREFS, 0);
        //get scores
        String mode = getMode();
        savedScores = scorePrefs.getString(mode, "").split("\\|");

        //build string
        StringBuilder scoreBuild = new StringBuilder("");
        int i = 1;
        for (String score : savedScores) {
            scoreBuild.append(i + "- " + score + "\n");
            i++;
        }
        if (i < 5) {
            for(int j = i; j <= 5; j++) {
                scoreBuild.append(j + "- \n");
            }
        }
        //display scores
        scoreView.setText(scoreBuild.toString());
    }

    public String getMode(){
        TextView title = (TextView)findViewById(R.id.txtHighscoresTitle);
        String mode = "";
        switch (gameMode) {
            case R.string.single:
                if(gameDifficulty == R.string.level1) {
                    mode = "highScoresSingleEasy";
                    title.setText(R.string.txtSingleEasyTitle);
                } else if(gameDifficulty == R.string.level2) {
                    mode = "highScoresSingleMedium";
                    title.setText(R.string.txtSingleMediumTitle);
                } else if(gameDifficulty == R.string.level3) {
                    mode = "highScoresSingleAdvanced";
                    title.setText(R.string.txtSingleAdvancedTitle);
                }
                break;
            case R.string.rush:
                if(gameDifficulty == R.string.level1) {
                    mode = "highScoresRushEasy";
                    title.setText(R.string.txtRushEasyTitle);
                } else if(gameDifficulty == R.string.level2) {
                    mode = "highScoresRushMedium";
                    title.setText(R.string.txtRushMediumTitle);
                } else if(gameDifficulty == R.string.level3) {
                    mode = "highScoresRushAdvanced";
                    title.setText(R.string.txtRushAdvancedTitle);
                }
                break;
        }
        return mode;
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
    }
    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(Highscores.this, Music.class);
        startService(intent);
//        introMusic.start();
    }
}
