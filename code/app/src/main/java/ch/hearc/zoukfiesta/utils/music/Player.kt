package ch.hearc.zoukfiesta.utils.music

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.net.Uri

object MusicPlayer {

    init {
        }

    private var mediaPlayer: MediaPlayer? = null
    var onCompleted: ((mp : MediaPlayer) -> Unit)? = null

    fun play(context: Context, resid: Int) {

        //Stop the current music
        mediaPlayer?.stop()

        //Change current music
        mediaPlayer = MediaPlayer.create(context, resid)

        //Play the music
        mediaPlayer?.start()
    }

    fun play(context: Context, uri: Uri) {
        if (mediaPlayer == null)
        {
            mediaPlayer = MediaPlayer()
            mediaPlayer?.setOnCompletionListener(OnCompletionListener {
                    mp -> onCompleted?.let { it(mp) }
            })
        }


        //Stop the current music
        if (mediaPlayer!!.isPlaying) mediaPlayer?.stop()

        //Change current music
        mediaPlayer?.reset()
        mediaPlayer?.setDataSource(context,uri)
        mediaPlayer?.prepare()

        //Play the music
        mediaPlayer?.start()
    }

    fun moveTo(newTime: Float) {
        mediaPlayer?.seekTo((newTime).toInt())
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
        if (mediaPlayer!!.isPlaying) mediaPlayer?.stop()
    }

    fun resume() {
        mediaPlayer?.start()
    }

    fun isPlaying(): Boolean?
    {
        return mediaPlayer?.isPlaying
    }

    fun release()
    {
        if (mediaPlayer!!.isPlaying) mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}