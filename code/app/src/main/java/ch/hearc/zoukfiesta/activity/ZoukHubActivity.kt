package ch.hearc.zoukfiesta.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ch.hearc.zoukfiesta.R
import ch.hearc.zoukfiesta.fragments.PlayerFragment
import ch.hearc.zoukfiesta.utils.music.Music
import ch.hearc.zoukfiesta.utils.music.MusicStore
import ch.hearc.zoukfiesta.utils.nearby.NearbyClient
import ch.hearc.zoukfiesta.utils.nearby.NearbySingleton
import com.google.android.gms.nearby.Nearby


class ZoukHubActivity() : AppCompatActivity(){

    private lateinit var availableMusics : Array<String>;
    private lateinit var nearbyClient : NearbyClient;
    private lateinit var  playlist : Map<String, Int>;
    private var endpointId: String = ""
    private lateinit var playerFragment: PlayerFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);

        //Get the endpoint
        endpointId = intent.getStringExtra("endpointId").toString()

        //Set playerfragment
        if (savedInstanceState == null) {
            playerFragment = (supportFragmentManager.findFragmentById(R.id.playerFragment) as PlayerFragment?)!!
        }

        NearbySingleton.nearbyClient?.onPlaylist = { playlist, currentTime, totalTime ->
            this.playlist = playlist;

            //Update current time and max time
            playerFragment.setNewTimeInfo(currentTime, totalTime, playlist.keys.first())

            MusicStore.musics.clear()
            playlist.forEach { musicName, musicVote ->
                MusicStore.musics.add(Music(musicName, musicVote))
            }

            NearbySingleton.musicPointAdapter?.notifyDataSetChanged()
        }

        NearbySingleton.nearbyClient?.onAvailable = { availableMusics -> this.availableMusics = availableMusics
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

        NearbySingleton.nearbyClient?.start()

        setContentView(R.layout.zouk_hub_activity)
    }
}