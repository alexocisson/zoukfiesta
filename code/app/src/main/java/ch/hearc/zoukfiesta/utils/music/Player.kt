package ch.hearc.zoukfiesta.utils.music

import android.content.Context
import android.media.MediaPlayer

object MusicPlayer {

    init {
        }

    var mediaPlayer: MediaPlayer? = null

    fun play(context: Context, resid: Int) {

        //Stop the current musi
        mediaPlayer?.stop()

        //Change current music
        mediaPlayer = MediaPlayer.create(context, resid)

        //Play the music
        mediaPlayer?.start()

    }
}