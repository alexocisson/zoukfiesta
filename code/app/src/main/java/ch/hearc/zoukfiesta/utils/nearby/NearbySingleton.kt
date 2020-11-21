package ch.hearc.zoukfiesta.utils.nearby

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ch.hearc.zoukfiesta.utils.nearby.NearbyClient
import ch.hearc.zoukfiesta.utils.nearby.NearbyServer
import com.google.android.gms.nearby.connection.Strategy


object NearbySingleton {

    init {
    }

    var nearbyClient: NearbyClient? = null
    var nearbyServer: NearbyServer? = null
    val STRATEGY: Strategy = Strategy.P2P_STAR
    var PACKAGE_NAME: String = ""

//    fun play(context: Context, resid: Int) {
//
//        //Stop the current musi
//        mediaPlayer?.stop()
//
//        //Change current music
//        mediaPlayer = MediaPlayer.create(context, resid)
//
//        //Play the music
//        mediaPlayer?.start()
//
//    }
}