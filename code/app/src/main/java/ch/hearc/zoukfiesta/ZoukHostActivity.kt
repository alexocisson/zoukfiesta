package ch.hearc.zoukfiesta

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ch.hearc.zoukfiesta.fragments.ConnectionFragment

class ZoukHostActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.zouk_host_activity)
    }
}