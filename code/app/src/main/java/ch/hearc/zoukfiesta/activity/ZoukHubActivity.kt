package ch.hearc.zoukfiesta.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import ch.hearc.zoukfiesta.R
import ch.hearc.zoukfiesta.fragments.AvailableMusicsFragment
import ch.hearc.zoukfiesta.fragments.MusicQueueFragment
import ch.hearc.zoukfiesta.fragments.PlayerFragment
import ch.hearc.zoukfiesta.fragments.SettingsFragment
import ch.hearc.zoukfiesta.utils.music.Music
import ch.hearc.zoukfiesta.utils.music.MusicStore
import ch.hearc.zoukfiesta.utils.nearby.NearbySingleton
import com.google.android.gms.nearby.Nearby
import org.json.JSONObject


class ZoukHubActivity() : AppCompatActivity(){

    private lateinit var viewPager: ViewPager2
    private var endpointId: String = ""
    private lateinit var playerFragment: PlayerFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.zouk_hub_activity)
        //Get the endpoint
        endpointId = intent.getStringExtra("endpointId").toString()

        //Set playerfragment
        if (savedInstanceState == null) {
            playerFragment = (supportFragmentManager.findFragmentById(R.id.playerFragment) as PlayerFragment?)!!
        }

        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = findViewById(R.id.pager)
        viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL

        // The pager adapter, which provides the pages to the view pager widget.
        val availableMusicsFragment = AvailableMusicsFragment()
        availableMusicsFragment.onItemClick = {parent, view, position, id ->
            //NearbySingleton.nearbyClient?.sendWhat()
        }

        val pagerAdapter = ScreenSlidePagerAdapter(this,availableMusicsFragment)
        viewPager.adapter = pagerAdapter

        NearbySingleton.nearbyClient?.onPlaylist = { playlist, currentTime, totalTime ->

            MusicStore.musicQueue.clear()
            playlist.forEach { musicName, musicJSON ->
                val music = JSONObject(musicJSON)
                MusicStore.musicQueue.add(Music(music.getString("name"), music.getString("artist"),music.getInt("vote")))
            }

            //Update current time and max time
            if (MusicStore.musicQueue.isEmpty())
            {
                playerFragment.setNewTimeInfo(0, Float.MAX_VALUE.toInt(), "","")
            }
            else
            {
                val musicToPlay : Music = MusicStore.musicQueue.first()
                playerFragment.setNewTimeInfo(currentTime, totalTime, musicToPlay.name,musicToPlay.artist)
            }

            NearbySingleton.musicQueueAdapter?.notifyDataSetChanged()
        }

        NearbySingleton.nearbyClient?.onAvailable = { availableMusics ->

            MusicStore.availableMusics.clear()
            availableMusics.forEach { musicName, musicJSON ->
                val music = JSONObject(musicJSON)
                MusicStore.availableMusics.add(Music(music.getString("name"), music.getString("artist")))
            }

            NearbySingleton.availableMusicsAdapter?.notifyDataSetChanged()
        }

        NearbySingleton.nearbyClient?.onKick = {
            Nearby.getConnectionsClient(baseContext).disconnectFromEndpoint(endpointId)
            println("I JUST GOT KICKED OMG");
            val toastText = "You have been kicked"
            val duration = Toast.LENGTH_LONG
            val toast = Toast.makeText(baseContext, toastText, duration)
            toast.show()
            this.finish()
        }

        NearbySingleton.nearbyClient?.onPause = {
            //Stop the slider
            playerFragment.isPlaying = !playerFragment.isPlaying
        }

        NearbySingleton.nearbyClient?.start()
    }

    private inner class ScreenSlidePagerAdapter(
        fa: FragmentActivity,
        availableMusicsFragment: AvailableMusicsFragment
    ) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 3

        var settingFragment : Fragment = SettingsFragment()
        var musicQueueFragment : Fragment = MusicQueueFragment()
        var availableMusicsFragment : Fragment = availableMusicsFragment


        override fun createFragment(position: Int): Fragment
        {
            if(position == 1) {
                return availableMusicsFragment
            }
            else if(position == 2) {
                return settingFragment
            }
            else {
                return musicQueueFragment
            }
        }
    }
}