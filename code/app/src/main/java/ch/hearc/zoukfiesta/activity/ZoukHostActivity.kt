package ch.hearc.zoukfiesta.activity

import android.accounts.NetworkErrorException
import android.graphics.drawable.GradientDrawable
import android.media.MediaMetadataRetriever
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_VERTICAL
import ch.hearc.zoukfiesta.R
import ch.hearc.zoukfiesta.fragments.MusicQueueFragment
import ch.hearc.zoukfiesta.fragments.SettingsFragment
import ch.hearc.zoukfiesta.utils.music.Music
import ch.hearc.zoukfiesta.utils.music.MusicStore
import ch.hearc.zoukfiesta.utils.music.Player
import ch.hearc.zoukfiesta.utils.nearby.NearbySingleton
import kotlinx.android.synthetic.main.music_queue_fragment.*
import kotlinx.android.synthetic.main.zouk_host_activity.*
import java.time.Duration
import java.util.*
import kotlin.concurrent.schedule


class ZoukHostActivity : AppCompatActivity(){

    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.zouk_host_activity)

        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = findViewById(R.id.pager)
        viewPager.orientation = ORIENTATION_VERTICAL

        // The pager adapter, which provides the pages to the view pager widget.
        val pagerAdapter = ScreenSlidePagerAdapter(this)
        viewPager.adapter = pagerAdapter

        //Add some musics to test
        MusicStore.musics.add(Music("apereka", 11, R.raw.apareka, resources.openRawResourceFd(R.raw.apareka)))
        MusicStore.musics.add(Music("rachuli", 22,R.raw.rachuli, resources.openRawResourceFd(R.raw.rachuli)))
        MusicStore.musics.add(Music("dililme", 33,R.raw.dililme, resources.openRawResourceFd(R.raw.dililme)))

        // Start by playing the first music in the store
        updateMusicPlayer()
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

            //Play the next one
            updateMusicPlayer()
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
