package ch.hearc.zoukfiesta.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_VERTICAL
import ch.hearc.zoukfiesta.R
import ch.hearc.zoukfiesta.fragments.MusicQueueFragment
import ch.hearc.zoukfiesta.fragments.PlayerFragment
import ch.hearc.zoukfiesta.fragments.SettingsFragment
import ch.hearc.zoukfiesta.utils.music.Music
import ch.hearc.zoukfiesta.utils.music.MusicPlayer
import ch.hearc.zoukfiesta.utils.music.MusicStore
import ch.hearc.zoukfiesta.utils.nearby.NearbySingleton
import java.util.*
import kotlin.concurrent.schedule


class ZoukHostActivity : AppCompatActivity(){

    private lateinit var viewPager: ViewPager2
    private lateinit var playerFragment: PlayerFragment
    private lateinit var musicTimer: TimerTask
    private var isPlaying: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.zouk_host_activity)

        if (savedInstanceState == null) {
            //Get the player fragment
            playerFragment = (supportFragmentManager.findFragmentById(R.id.playerFragment) as PlayerFragment?)!!

            //Set it to host mode
            playerFragment?.setAsHost()

            //Set the change event function callback
            playerFragment.onValueChange = { slider, value, fromUser ->
                if(fromUser)
                {
                    //Set the current time
                    updateScheduleRemainingTime(value)
                }
            }

            //Set the pause event function callback
            playerFragment.onPause = { it ->
                if(isPlaying)
                {
                    //Pause the music
                    MusicPlayer.pause()
                }
                else
                {
                    //Resume the music
                    MusicPlayer.resume()
                }

                //Inverse state
                isPlaying = !isPlaying
            }
        }

        //NearbySingleton.musicPointAdapter = MusicAdapter(this)

        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = findViewById(R.id.pager)
        viewPager.orientation = ORIENTATION_VERTICAL

        // The pager adapter, which provides the pages to the view pager widget.
        val pagerAdapter = ScreenSlidePagerAdapter(this)
        viewPager.adapter = pagerAdapter

        //Add some musics to test
        MusicStore.musics.add(Music("Dancing With The DJ - The Knocks", 2, R.raw.apareka, resources.openRawResourceFd(R.raw.apareka)))
        MusicStore.musics.add(Music("1979 - The Smashings Pumpkins", 5,R.raw.rachuli, resources.openRawResourceFd(R.raw.rachuli)))
        MusicStore.musics.add(Music("Signatune - DJ Mehdi", 1,R.raw.dililme, resources.openRawResourceFd(R.raw.dililme)))

        // Start by playing the first music in the store
        nextMusic()
    }

    override fun onBackPressed() {
        if (viewPager.currentItem == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed()
        } else {
            // Otherwise, select the previous step.
            viewPager.currentItem = viewPager.currentItem - 1
        }
    }

    //Tell the music player to play the first music in music store
    fun nextMusic()
    {

        //Get the next music to play
        var music = MusicStore.musics[0]

        //Get its duration
        var duration = music.duration

        //Play it
        music.resourceId?.let { MusicPlayer.play(this, it) }

        runOnUiThread {
            //Set the timer
            playerFragment.setNewTimeInfo(0, duration, music.name)

            //Notify the change
            NearbySingleton.musicPointAdapter?.notifyDataSetChanged()
        }

        //Pass to the next music at the end of thze current one
        musicTimer = Timer("waitingForMusicToFinish", false).schedule(music.duration.toLong()) {
            passToNextMusic()
        }
    }

    //Tell the music player to play the first music in music store
    fun updateScheduleRemainingTime(newTime: Float)
    {
        runOnUiThread{
            playerFragment.setCurrentTime(newTime)
        }

        //Get the next music to play
        var music = MusicStore.musics[0]

        //Get its duration
        var duration = music.duration

        musicTimer.cancel()

        //Move current music player
        MusicPlayer.moveTo(newTime)

        //Send to all clients
        sendToAllClient(newTime)

        //Pass to the next music at the end of thze current one
        musicTimer = Timer("waitingForMusicToFinish", false).schedule((duration - newTime*1e3).toLong()) {
            passToNextMusic()
        }
    }

    private fun passToNextMusic()
    {
        //Send to all clients
        sendToAllClient(0f)

        MusicStore.musics.removeAt(0)

        //Play the next one
        nextMusic()
    }

    //Send to all clients
    private fun sendToAllClient(currentMusicTime: Float)
    {
        var mapMusic : MutableMap<String, Int> = emptyMap<String, Int>().toMutableMap()
        MusicStore.musics.forEach {
            mapMusic[it.name] = it.voteSkip
        }

        //Send to all clients
        NearbySingleton.nearbyServer?.clientsById?.forEach { endpointId, name ->

            MusicPlayer.getDuration()?.let { duration ->
                NearbySingleton.nearbyServer?.sendPlaylist(endpointId,mapMusic,
                    (currentMusicTime*1e3).toInt(), duration
                )
            }
        }
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 2

        var settingFragment : Fragment = SettingsFragment()
        var musicQueueFragment : Fragment = MusicQueueFragment()

        override fun createFragment(position: Int): Fragment
        {
            if(position == 1) {
                return settingFragment
            }
            else {
                return musicQueueFragment
            }
        }
    }
}
