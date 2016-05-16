package com.makakaton.dm.ac.tec.makakaton;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.IntentSender;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Music extends Service implements MediaPlayer.OnPreparedListener {
    private static final String ACTION_PLAY = "com.makakaton.dm.ac.tec.menu.action.PLAY";
    public MediaPlayer mMediaPlayer=null ; // initialize it here

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mMediaPlayer.start();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mMediaPlayer = MediaPlayer.create(this, R.raw.intro_song);
        mMediaPlayer.setOnPreparedListener(this);
       // mMediaPlayer.prepareAsync(); // prepare async to not block main thread
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
      /*  System.out.println("->>>>>>>>>>>>>>>>>>>>>"+intent.getAction());
        if (intent.getAction().equals(ACTION_PLAY)) {
            mMediaPlayer = MediaPlayer.create(this, R.raw.game_song); // initialize it here
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.prepareAsync(); // prepare async to not block main thread
        }*/

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer!=null){
            mMediaPlayer.release();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
}
