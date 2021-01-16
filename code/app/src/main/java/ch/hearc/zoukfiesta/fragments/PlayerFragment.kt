package ch.hearc.zoukfiesta.fragments
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import ch.hearc.zoukfiesta.*
import ch.hearc.zoukfiesta.utils.music.MusicPlayer
import ch.hearc.zoukfiesta.utils.music.MusicStore
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.slider.Slider
import java.util.*
import kotlin.concurrent.schedule

class
PlayerFragment : Fragment() {

    private var pauseButton: FloatingActionButton? = null
    private var skipButton: FloatingActionButton? = null
    private var musicTextView: TextView? = null
    private var timeSlider: Slider? = null

    private var time: Float = 10f
    private var maxTime: Float = 132f


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.player_fragment, container, false)

        Timer("increase time on slider", false).schedule(1) {
            //Increse the timer value
            time=time+1f;
            timeSlider!!.setValue(time);
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        retrieveViews(view!!)
        setUpViews(activity!!)
    }

    private fun retrieveViews(view: View) {
        pauseButton = view.findViewById<View>(R.id.PauseButton) as FloatingActionButton
        skipButton = view.findViewById<View>(R.id.SkipButton) as FloatingActionButton
        musicTextView = view.findViewById<View>(R.id.MusicTextView) as TextView
        timeSlider = view.findViewById<View>(R.id.TimeSlider) as Slider
    }

    private fun setUpViews(activity: Activity) {
        //time = MusicPlayer.mediaPlayer.currentPosition;
        musicTextView!!.text = "Yo la musique";
        timeSlider!!.setValue(time);


        
    }

    override fun onResume() {
        // Important! Refresh our list when we return to this activity (from another one)

        super.onResume()
    }
}
