package br.com.helpdev.supportlib.media;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by Guilherme Biff Zarelli on 16/03/16.
 */
public class AudioPlayer implements MediaPlayer.OnCompletionListener {

    private volatile static AudioPlayer instance;

    private MediaPlayer mediaPlayer;

    private AudioPlayer() {

    }

    public static AudioPlayer getInstance() {
        if (instance == null) {
            instance = new AudioPlayer();
        }
        return instance;
    }

    public void play(Context context, int audioResId) {
        if (mediaPlayer==null || !mediaPlayer.isPlaying()) {
            mediaPlayer = MediaPlayer.create(context, audioResId);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.start();
        }
    }

    public void release() {
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        try {
            release();
            mediaPlayer = null;
        } catch (Exception e) {

        }
    }
}
