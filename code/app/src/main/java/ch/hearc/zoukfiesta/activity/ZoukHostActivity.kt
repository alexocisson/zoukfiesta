package ch.hearc.zoukfiesta.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ch.hearc.zoukfiesta.R
import ch.hearc.zoukfiesta.utils.music.Player

class ZoukHostActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Player.play(this, R.raw.apareka)

        Thread.sleep(5000)

        Player.play(this, R.raw.dililme)

        setContentView(R.layout.zouk_host_activity)
    }
}