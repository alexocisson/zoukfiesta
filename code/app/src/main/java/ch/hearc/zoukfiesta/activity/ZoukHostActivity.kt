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
import ch.hearc.zoukfiesta.fragments.SettingsFragment
import ch.hearc.zoukfiesta.utils.music.Music
import ch.hearc.zoukfiesta.utils.music.MusicStore
import ch.hearc.zoukfiesta.utils.music.MusicPlayer
import ch.hearc.zoukfiesta.utils.nearby.NearbySingleton
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
        MusicStore.musics.add(Music("Dancing With The DJ - The Knocks", 2, R.raw.apareka, resources.openRawResourceFd(R.raw.apareka)))
        MusicStore.musics.add(Music("1979 - The Smashings Pumpkins", 5,R.raw.rachuli, resources.openRawResourceFd(R.raw.rachuli)))
        MusicStore.musics.add(Music("Signatune - DJ Mehdi", 1,R.raw.dililme, resources.openRawResourceFd(R.raw.dililme)))

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
        MusicPlayer.play(this, music.resourceId)

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
