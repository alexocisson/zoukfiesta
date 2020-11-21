package ch.hearc.zoukfiesta.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ch.hearc.zoukfiesta.R
import ch.hearc.zoukfiesta.utils.nearby.NearbyClient
import ch.hearc.zoukfiesta.utils.nearby.NearbySingleton

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
        }
        NearbySingleton.nearbyClient?.onAvailable = {
                availableMusics -> this.availableMusics = availableMusics
        }
        NearbySingleton.nearbyClient?.onKick = {
            println("I JUST GOT KICKED");
        }

        setContentView(R.layout.zouk_hub_activity)
    }
}