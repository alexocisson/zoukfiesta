package ch.hearc.zoukfiesta.utils.music

import android.content.Context
import android.media.MediaPlayer

object MusicPlayer {

    init {
        }

    private var mediaPlayer: MediaPlayer? = null

    fun play(context: Context, resid: Int) {

        //Stop the current music
        mediaPlayer?.stop()

        //Change current music
        mediaPlayer = MediaPlayer.create(context, resid)

        //Play the music
        mediaPlayer?.start()

    }

    fun moveTo(newTime: Float) {
        mediaPlayer?.seekTo((newTime*1e3).toInt())
    }

    fun getTimestamp(): Int? {
        return mediaPlayer?.currentPosition
    }

    fun getDuration(): Int? {
        return mediaPlayer?.duration
    }

    fun pause() {
        mediaPlayer?.pause()
    }

    fun stop() {
        mediaPlayer?.stop();
    }

    fun resume() {
        mediaPlayer?.start()
    }
}