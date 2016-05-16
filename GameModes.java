package com.makakaton.dm.ac.tec.makakaton;

/**
 * Created by franco on 4/21/16.
 */
public class GameModes {

    private int score;


    public GameModes() {
        this.score = 0;


    }


    /*public void startRush(int miliSeconds, final TextView left, final TextView right, final Context context) {
        timer = new CountDownTimer(miliSeconds, 1000) {

            public void onTick(long millisUntilFinished) {
                left.setText(String.valueOf(millisUntilFinished / 1000));
                right.setText(String.valueOf(millisUntilFinished / 1000));
            }

            public void onFinish() {
                Toast.makeText(context, "Time's up!", Toast.LENGTH_SHORT).show();
                this.cancel();
                left.setText(String.valueOf(""));
                right.setText(String.valueOf(""));
                finishRush();
            }
        }.start();

    }*/

    public void finishRush(int score) {

        this.score = 0;

    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;


    }
}
