package ch.hearc.zoukfiesta.activity

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_VERTICAL
import ch.hearc.zoukfiesta.R
import ch.hearc.zoukfiesta.fragments.AvailableMusicsFragment
import ch.hearc.zoukfiesta.fragments.MusicQueueFragment
import ch.hearc.zoukfiesta.fragments.PlayerFragment
import ch.hearc.zoukfiesta.fragments.SettingsFragment
import ch.hearc.zoukfiesta.utils.music.Music
import ch.hearc.zoukfiesta.utils.music.MusicPlayer
import ch.hearc.zoukfiesta.utils.music.MusicStore
import ch.hearc.zoukfiesta.utils.nearby.NearbySingleton
import kotlinx.android.synthetic.main.create_activity.*
import org.json.JSONObject
import java.util.*
import kotlin.concurrent.schedule


class ZoukHostActivity : AppCompatActivity(){

    private lateinit var viewPager: ViewPager2
    private lateinit var playerFragment: PlayerFragment
    private var remainingTimePause: Float = 0.0f
    private lateinit var musicTimer: TimerTask

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
                    if(playerFragment.isPlaying/*isPlaying*/)
                    {
                        //Set the current time
                        updateScheduleRemainingTime(value)
                    }
                    else
                    {
                        remainingTimePause = value
                        runOnUiThread{
                            playerFragment.setCurrentTime(value)
                        }
                    }

                }
            }

            //Set the pause event function callback
            playerFragment.onPause = { it ->
                if (!MusicStore.musicQueue.isEmpty())
                {
                    if(playerFragment.isPlaying)
                    {
                        //Pause the music
                        MusicPlayer.pause()
                        musicTimer.cancel()
                        remainingTimePause = MusicPlayer.getTimestamp()!!.toFloat()
                    }
                    else
                    {
                        //Resume the music
                        MusicPlayer.resume()
                        updateScheduleRemainingTime(remainingTimePause)
                    }

                    //Inverse state
                    playerFragment.isPlaying = !playerFragment.isPlaying
                }
                else
                {
                    playerFragment.isPlaying = false
                    playerFragment.pause()
                }
                sendPlayPauseToAllClient(playerFragment.isPlaying)
            }

            playerFragment.pause()
        }

        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = findViewById(R.id.pager)
        viewPager.orientation = ORIENTATION_VERTICAL

        // The pager adapter, which provides the pages to the view pager widget.
        val availableMusicsFragment = AvailableMusicsFragment()
        availableMusicsFragment.onItemClick = {parent, view, position, id ->
            val music = parent!!.getItemAtPosition(position) as Music

            if (MusicStore.musicQueue.isEmpty())
            {
                MusicStore.musicQueue.add(music)
                nextMusic()
            }
            else
            {
                MusicStore.musicQueue.add(music)
            }
            NearbySingleton.musicQueueAdapter?.notifyDataSetChanged()
        }

        val pagerAdapter = ScreenSlidePagerAdapter(this,availableMusicsFragment)
        viewPager.adapter = pagerAdapter

        // Start by playing the first music in the store
        nextMusic()
    }

    override fun onBackPressed() {
        if (viewPager.currentItem == 0) {

            // Build an AlertDialog
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)

            // Set a title for alert dialog
            builder.setTitle("Stop Fiesta")

            // Ask the final question
            builder.setMessage("Are you sure to end the fiesta?")

            // Set the alert dialog yes button click listener
            builder.setPositiveButton("Yes",
                DialogInterface.OnClickListener { dialog, which -> // Do something when user clicked the Yes button
                    // If the user is currently looking at the first step, allow the system to handle the
                    // Back button. This calls finish() on this activity and pops the back stack.
                    MusicPlayer.stop()
                    NearbySingleton.nearbyServer?.stopAdvertising()
                    NearbySingleton.nearbyClient?.startDiscovery(NearbySingleton.PACKAGE_NAME,NearbySingleton.ENDPOINTDISCOVERYCALLBACK,NearbySingleton.STRATEGY)
                    this.finish()
                    super.onBackPressed()
                })

            // Set the alert dialog no button click listener
            builder.setNegativeButton("No",
                DialogInterface.OnClickListener { dialog, which -> // Do something when No button clicked
                    Toast.makeText(
                        applicationContext,
                        "Fiesta is not over !", Toast.LENGTH_SHORT
                    ).show()
                })

            val dialog: AlertDialog = builder.create()
            // Display the alert dialog on interface
            dialog.show()
        } else {
            // Otherwise, select the previous step.
            viewPager.currentItem = viewPager.currentItem - 1
        }
    }

    //Tell the music player to play the first music in music store
    fun nextMusic()
    {

        //Get the next music to play
        var music = MusicStore.musicQueue.firstOrNull()
        if (music == null)
        {
            runOnUiThread {
                //Update Player UI
                playerFragment.pause()
                //Set the timer
                playerFragment.setNewTimeInfo(0, Float.MAX_VALUE.toInt(), "", "")

                //Notify the change
                NearbySingleton.musicQueueAdapter?.notifyDataSetChanged()
            }
            sendAvailableMusicsToAllClient()
            sendMusicQueueToAllClient(0f)
            sendPlayPauseToAllClient(false)
            return
        }

        //Update Player UI
        playerFragment.play()

        //Get its duration
        var duration = music.duration

        //Play it
        music.resourceUri?.let {
            MusicPlayer.play(this, it)
        }

        runOnUiThread {
            //Set the timer
            playerFragment.setNewTimeInfo(0, duration, music.name, music.artist)

            //Notify the change
            NearbySingleton.musicQueueAdapter?.notifyDataSetChanged()
        }

        //Pass to the next music at the end of the current one
        musicTimer = Timer("waitingForMusicToFinish", false).schedule(music.duration.toLong()) {
            passToNextMusic()
        }

        //Send to all clients
        sendAvailableMusicsToAllClient()
        sendMusicQueueToAllClient(0f)
        sendPlayPauseToAllClient(true)
    }

    //Tell the music player to play the first music in music store
    fun updateScheduleRemainingTime(newTime: Float)
    {
        runOnUiThread{
            playerFragment.setCurrentTime(newTime)
        }

        //Get the next music to play
        var music = MusicStore.musicQueue.first()

        //Get its duration
        var duration = music.duration

        musicTimer.cancel()

        //Move current music player
        MusicPlayer.moveTo(newTime)

        //Send to all clients
        sendAvailableMusicsToAllClient()
        sendMusicQueueToAllClient(newTime)

        //Pass to the next music at the end of the current one
        musicTimer = Timer("waitingForMusicToFinish", false).schedule((duration - newTime).toLong()) {
            passToNextMusic()
        }
    }

    private fun passToNextMusic()
    {
        MusicStore.musicQueue.removeAt(0)

        //Play the next one
        nextMusic()
    }

    //Send to all clients
    private fun sendMusicQueueToAllClient(currentMusicTime: Float)
    {
        var mapMusic : MutableMap<String, String> = emptyMap<String, String>().toMutableMap()
        MusicStore.musicQueue.forEach { music ->
            val musicJSON = JSONObject();
            musicJSON.put("name", music.name)
            musicJSON.put("artist", music.artist)
            musicJSON.put("vote", music.voteSkip)
            mapMusic[music.name] = musicJSON.toString()
        }

        //Send to all clients
        NearbySingleton.nearbyServer?.clientsById?.forEach { endpointId, name ->

            MusicPlayer.getDuration()?.let { duration ->
                NearbySingleton.nearbyServer?.sendPlaylist(endpointId,mapMusic,
                    (currentMusicTime).toInt(), duration
                )
            }
        }
    }

    private fun sendAvailableMusicsToAllClient()
    {
        var mapMusic : MutableMap<String, String> = emptyMap<String, String>().toMutableMap()
        MusicStore.availableMusics.forEach { music ->
            val musicJSON = JSONObject();
            musicJSON.put("name", music.name)
            musicJSON.put("artist", music.artist)
            mapMusic[music.name] = musicJSON.toString()
        }

        //Send to all clients
        NearbySingleton.nearbyServer?.clientsById?.forEach { endpointId, name ->
            NearbySingleton.nearbyServer!!.sendAvailable(endpointId,mapMusic)
        }
    }

    private fun sendPlayPauseToAllClient(isPlaying:Boolean)
    {
        NearbySingleton.nearbyServer?.clientsById?.forEach { endpointId, name ->

            NearbySingleton.nearbyServer!!.sendPause(endpointId,isPlaying)
        }
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
