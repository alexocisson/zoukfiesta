package ch.hearc.zoukfiesta.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ch.hearc.zoukfiesta.R
import ch.hearc.zoukfiesta.utils.music.Music
import ch.hearc.zoukfiesta.utils.music.MusicStore
import ch.hearc.zoukfiesta.utils.nearby.NearbyClient
import ch.hearc.zoukfiesta.utils.nearby.NearbySingleton
import com.google.android.gms.nearby.Nearby

class ZoukHubActivity() : AppCompatActivity(){

    lateinit var availableMusics : Array<String>;
    lateinit var nearbyClient : NearbyClient;
    lateinit var  playlist : Map<String, Int>;
    var currentTime : Int = 0
    var totalTime : Int = 0
    var endpointId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);

        //Get the endpoint
        endpointId = intent.getStringExtra("endpointId").toString()

//        //Init nearby client
//        nearbyClient = NearbyClient(
//            endpointId,
//            this,
//            {
//                playlist, currentTime, totalTime ->
//                this.playlist = playlist;
//                this.currentTime = currentTime;
//                this.totalTime = totalTime;
//            }, //On playlist call back
//            //On available call back
//            {
//                availableMusics -> this.availableMusics = availableMusics
//            },
//            // On kick call back
//            {
//                println("I JUST GOT KICKED");
//            }
//        )

        NearbySingleton.nearbyClient?.onPlaylist = {
                playlist, currentTime, totalTime ->
            this.playlist = playlist;
            this.currentTime = currentTime;
            this.totalTime = totalTime;

            MusicStore.musics.clear()
            playlist.forEach { musicName, musicVote ->
                MusicStore.musics.add(Music(musicName, musicVote))
            }

            NearbySingleton.musicPointAdapter?.notifyDataSetChanged()
        }
        NearbySingleton.nearbyClient?.onAvailable = {
                availableMusics -> this.availableMusics = availableMusics
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