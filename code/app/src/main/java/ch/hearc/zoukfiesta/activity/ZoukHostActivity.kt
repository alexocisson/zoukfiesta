package ch.hearc.zoukfiesta.activity

import android.media.MediaMetadataRetriever
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ch.hearc.zoukfiesta.R
import ch.hearc.zoukfiesta.utils.music.Music
import ch.hearc.zoukfiesta.utils.music.MusicStore
import ch.hearc.zoukfiesta.utils.music.Player
import kotlinx.android.synthetic.main.music_queue_fragment.*
import kotlinx.android.synthetic.main.zouk_host_activity.*
import java.time.Duration
import java.util.*
import kotlin.concurrent.schedule


class ZoukHostActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.zouk_host_activity)

        //Add some musics to test
        MusicStore.musics.add(Music("apereka", 11, R.raw.apareka, resources.openRawResourceFd(R.raw.apareka)))
        MusicStore.musics.add(Music("rachuli", 22,R.raw.rachuli, resources.openRawResourceFd(R.raw.rachuli)))
        MusicStore.musics.add(Music("dililme", 33,R.raw.dililme, resources.openRawResourceFd(R.raw.dililme)))

        // Start by playing the first music in the store
        updateMusicPlayer()
    }

    //Tell the music player to play the first music in music store
    fun updateMusicPlayer()
    {
        //Get the next music to play
        var music = MusicStore.musics[0]

        //Get its duration
        var duration = music.duration

        //Play it
        Player.play(this, music.resourceId)

        //Pass to the next music at the end of thze current one
        Timer("waitingForMusicToFinish", false).schedule(duration.toLong()) {
            //Remove the current music from the list
            MusicStore.musics.removeAt(0)

            //Update the list view
            //TODO
            //MusicQueueFragment.on

            //Play the next one
            updateMusicPlayer()
        }
    }
}