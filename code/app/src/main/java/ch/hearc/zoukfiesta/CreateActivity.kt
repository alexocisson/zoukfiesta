package ch.hearc.zoukfiesta

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ch.hearc.zoukfiesta.fragments.ConnectionFragment

class CreateActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.create_activity)
    }
}