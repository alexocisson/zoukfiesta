package ch.hearc.zoukfiesta.activity

import android.app.AlertDialog
import android.content.DialogInterface
import android.media.MediaPlayer
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
import org.json.JSONObject
import java.util.*


class ZoukHostActivity : AppCompatActivity(){

    private lateinit var viewPager: ViewPager2
    private lateinit var playerFragment: PlayerFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.zouk_host_activity)

        retrieveViews(savedInstanceState)
        setUpViews(savedInstanceState)
        setUpListeners(savedInstanceState)

        NearbySingleton.nearbyServer?.start()
        playerFragment.pause()
    }

    private fun retrieveViews(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            //Get the player fragment
            playerFragment =
                (supportFragmentManager.findFragmentById(R.id.playerFragment) as PlayerFragment?)!!
        }

        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = findViewById(R.id.pager)
        viewPager.orientation = ORIENTATION_VERTICAL
    }

    private fun setUpViews(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            //Get the player fragment
            playerFragment = (supportFragmentManager.findFragmentById(R.id.playerFragment) as PlayerFragment?)!!

            //Set it to host mode
            playerFragment?.setAsHost()
        }

        // The pager adapter, which provides the pages to the view pager widget.
        val availableMusicsFragment = AvailableMusicsFragment()
        availableMusicsFragment.onItemClick = {parent, view, position, id ->
            val music = (parent!!.getItemAtPosition(position) as Music).clone()

            if (MusicStore.musicQueue.isEmpty())
            {
                MusicStore.musicQueue.add(music)
                nextMusic()
            }
            else
            {
                MusicStore.musicQueue.add(music)
                sendAvailableMusicsToAllClient()
                MusicPlayer.getTimestamp()?.toFloat()?.let { sendMusicQueueToAllClient(it) }
            }
            NearbySingleton.musicQueueAdapter?.notifyDataSetChanged()
        }

        val musicQueueFragment = MusicQueueFragment()
        musicQueueFragment.onItemClick = {parent, view, position, id ->
            NearbySingleton.nearbyServer?.onSkip?.let { it("SERVERID",position) }
        }

        val pagerAdapter = ScreenSlidePagerAdapter(this,availableMusicsFragment,musicQueueFragment)
        viewPager.adapter = pagerAdapter
    }

    private fun setUpListeners(savedInstanceState: Bundle?) {

        MusicPlayer.onCompleted = {mp : MediaPlayer ->
            passToNextMusic()
        }

        NearbySingleton.nearbyServer?.onAdd = { musicName ->
            var musicToAdd: Music? =  MusicStore.availableMusics.firstOrNull{it.name == musicName}
            if (musicToAdd != null)
            {
                musicToAdd = musicToAdd.clone()
                if (MusicStore.musicQueue.isEmpty())
                {
                    MusicStore.musicQueue.add(musicToAdd)
                    nextMusic()
                }
                else
                {
                    MusicStore.musicQueue.add(musicToAdd)
                    sendAvailableMusicsToAllClient()
                    MusicPlayer.getTimestamp()?.toFloat()?.let { sendMusicQueueToAllClient(it) }
                }
                NearbySingleton.musicQueueAdapter?.notifyDataSetChanged()
            }
        }

        NearbySingleton.nearbyServer?.onSkip = { endpointId, musicIndex ->
            val music = MusicStore.musicQueue.get(musicIndex)
            if (!music.voters.contains(endpointId))
            {
                music.voters.add(endpointId)
                music.voteSkip += 1
                if (music.voteNeeded-music.voteSkip <=0)
                {
                    if (musicIndex == 0)
                    {
                        passToNextMusic()
                    }
                    else{
                        MusicStore.musicQueue.removeAt(musicIndex)
                        NearbySingleton.musicQueueAdapter?.notifyDataSetChanged()
                        MusicPlayer.getTimestamp()?.toFloat()?.let { sendMusicQueueToAllClient(it) }
                    }

                }
                else
                {
                    NearbySingleton.musicQueueAdapter?.notifyDataSetChanged()
                    MusicPlayer.getTimestamp()?.toFloat()?.let { sendMusicQueueToAllClient(it) }
                }
            }

        }

        if (savedInstanceState == null) {

            //Set the change event function callback
            playerFragment.onValueChange = { slider, value, fromUser ->
                if(fromUser)
                {
                    seekTo(value)
                }
            }

            //Set the pause event function callback
            playerFragment.onPause = { it ->
                if (!MusicStore.musicQueue.isEmpty())
                {
                    if(playerFragment.isPlaying)
                    {
                        //Pause the music
                        pause()
                    }
                    else
                    {
                        //Resume the music
                        resume()
                    }
                }
                else
                {
                    pause()
                }
            }

            //Set the skip event function callback
            playerFragment.onSkip = {it ->
                if (MusicStore.musicQueue.isNotEmpty()) NearbySingleton.nearbyServer?.onSkip?.let { it("SERVERID",0) }
            }
        }
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
                    sendKickToAllClient()
                    MusicPlayer.release()
                    NearbySingleton.nearbyServer?.stopAdvertising()
                    NearbySingleton.nearbyServer?.stop()
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
            stop()
            playerFragment.disable()
        }
        else {
            playerFragment.enable()
            play(music)
        }
    }


    private fun passToNextMusic()
    {
        MusicStore.musicQueue.removeAt(0)
        //Play the next one
        nextMusic()
    }

    private fun play(music: Music)
    {
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

        //Update Player UI
        playerFragment.play()
        //Send to all clients
        sendAvailableMusicsToAllClient()
        sendMusicQueueToAllClient(0f)
        sendPlayPauseToAllClient(true)
    }

    private fun stop()
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
    }

    private fun pause()
    {
        MusicPlayer.pause()
        playerFragment.pause()
        sendAvailableMusicsToAllClient()
        MusicPlayer.getTimestamp()?.toFloat()?.let { sendMusicQueueToAllClient(it) }
        sendPlayPauseToAllClient(false)
    }

    private fun resume()
    {
        MusicPlayer.resume()
        playerFragment.play()
        sendAvailableMusicsToAllClient()
        MusicPlayer.getTimestamp()?.toFloat()?.let { sendMusicQueueToAllClient(it) }
        sendPlayPauseToAllClient(true)
    }

    private fun seekTo(newTime: Float)
    {
        runOnUiThread{
            playerFragment.setCurrentTime(newTime)
        }

        //Move current music player
        MusicPlayer.moveTo(newTime)

        //Send to all clients
        sendAvailableMusicsToAllClient()
        sendMusicQueueToAllClient(newTime)
    }

    //Send to all clients
    private fun sendMusicQueueToAllClient(currentMusicTime: Float)
    {
        val numberOfZoukers = NearbySingleton.nearbyServer?.clientsById?.size
        val voteNeeded = if (numberOfZoukers == null) 1 else (numberOfZoukers + 1)// / 2
        var mapMusic : MutableMap<String, String> = emptyMap<String, String>().toMutableMap()
        MusicStore.musicQueue.forEachIndexed { index, music ->
            music.voteNeeded = voteNeeded
            val musicJSON = JSONObject();
            musicJSON.put("name", music.name)
            musicJSON.put("artist", music.artist)
            musicJSON.put("vote", music.voteSkip)
            musicJSON.put("voteNeeded", music.voteNeeded)
            mapMusic[music.name + index.toString()] = musicJSON.toString()
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

    private fun sendKickToAllClient()
    {
        NearbySingleton.nearbyServer?.clientsById?.forEach { endpointId, name ->

            NearbySingleton.nearbyServer!!.sendKick(endpointId)
        }
    }

    private inner class ScreenSlidePagerAdapter(
        fa: FragmentActivity,
        availableMusicsFragment: AvailableMusicsFragment,
        musicQueueFragment: MusicQueueFragment
    ) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 3

        var settingFragment : Fragment = SettingsFragment()
        var musicQueueFragment : Fragment = musicQueueFragment
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
