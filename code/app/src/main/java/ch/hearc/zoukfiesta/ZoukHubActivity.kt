package ch.hearc.zoukfiesta

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ch.hearc.zoukfiesta.nearby.utils.NearbyClient

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

        //Init nearby client
        nearbyClient = NearbyClient(
            endpointId,
            this,
            {
                playlist, currentTime, totalTime ->
                this.playlist = playlist;
                this.currentTime = currentTime;
                this.totalTime = totalTime;
            }, //On playlist call back
            //On available call back
            {
                availableMusics -> this.availableMusics = availableMusics
            },
            // On kick call back
            {
                println("I JUST GOT KICKED");
            }
        )

        setContentView(R.layout.zouk_hub_activity)
    }
}