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
import ch.hearc.zoukfiesta.R
import ch.hearc.zoukfiesta.fragments.AvailableMusicsFragment
import ch.hearc.zoukfiesta.fragments.MusicQueueFragment
import ch.hearc.zoukfiesta.fragments.PlayerFragment
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

        retrieveViews(savedInstanceState)
        setUpViews(savedInstanceState)
        setUpListeners(savedInstanceState)

        NearbySingleton.nearbyClient?.start()
    }

    private fun retrieveViews(savedInstanceState: Bundle?)
    {
        //Set playerfragment
        if (savedInstanceState == null) {
            playerFragment = (supportFragmentManager.findFragmentById(R.id.playerFragment) as PlayerFragment?)!!
        }

        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = findViewById(R.id.pager)
        viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL
    }
    private fun setUpViews(savedInstanceState: Bundle?)
    {
        // The pager adapter, which provides the pages to the view pager widget.
        val musicQueueFragment = MusicQueueFragment()
        musicQueueFragment.onItemClick = {parent, view, position, id ->
            NearbySingleton.nearbyClient?.sendSkip(position)
        }

        val availableMusicsFragment = AvailableMusicsFragment()
        availableMusicsFragment.onItemClick = {parent, view, position, id ->
            val music = parent!!.getItemAtPosition(position) as Music
            NearbySingleton.nearbyClient?.sendAdd(music.name)
        }

        val pagerAdapter = ScreenSlidePagerAdapter(this,availableMusicsFragment,musicQueueFragment)
        viewPager.adapter = pagerAdapter
    }
    private fun setUpListeners(savedInstanceState: Bundle?)
    {
        NearbySingleton.nearbyClient?.onPlaylist = { playlist, currentTime, totalTime ->

            MusicStore.musicQueue.clear()
            playlist.forEach { musicName, musicJSON ->
                val music = JSONObject(musicJSON)
                val musicObj = Music(music.getString("name"), music.getString("artist"),music.getInt("vote"))
                musicObj.voteNeeded = music.getInt("voteNeeded")
                MusicStore.musicQueue.add(musicObj)
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

        NearbySingleton.nearbyClient?.onPause = {isPlaying:Boolean ->
            //Stop the slider
            playerFragment.isPlaying = isPlaying
        }
    }

    override fun onBackPressed() {
        if (viewPager.currentItem == 0) {

            // Build an AlertDialog
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)

            // Set a title for alert dialog
            builder.setTitle("Leave Fiesta")

            // Ask the final question
            builder.setMessage("Are you sure to leave the fiesta?")

            // Set the alert dialog yes button click listener
            builder.setPositiveButton("Yes",
                DialogInterface.OnClickListener { dialog, which -> // Do something when user clicked the Yes button
                    // If the user is currently looking at the first step, allow the system to handle the
                    // Back button. This calls finish() on this activity and pops the back stack.
                    NearbySingleton.nearbyClient?.stop()
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

    private inner class ScreenSlidePagerAdapter(
        fa: FragmentActivity,
        availableMusicsFragment: AvailableMusicsFragment,
        musicQueueFragment: MusicQueueFragment
    ) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 2

        var musicQueueFragment : Fragment = musicQueueFragment
        var availableMusicsFragment : Fragment = availableMusicsFragment


        override fun createFragment(position: Int): Fragment
        {
            if(position == 1) {
                return availableMusicsFragment
            }
            else {
                return musicQueueFragment
            }
        }
    }
}