package ch.hearc.zoukfiesta.fragments
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
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
    private var timeView: TextView? = null
    private var timeSlider: Slider? = null

    private var time: Float = 1f
    private var maxTime: Float = 1000f
    private val mainHandler = Handler(Looper.getMainLooper())


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mainHandler.post(object : Runnable {
            override fun run() {
                updateSlider()
                mainHandler.postDelayed(this, 1000)
            }
        })

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.player_fragment, container, false)
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
        timeView = view.findViewById<View>(R.id.TimeView) as TextView
        timeSlider = view.findViewById<View>(R.id.TimeSlider) as Slider
    }

    private fun setUpViews(activity: Activity) {
        //time = MusicPlayer.mediaPlayer.currentPosition;
        musicTextView!!.text = "Yo la musique"
        timeSlider!!.setValue(time)
        timeSlider!!.setValueTo(maxTime)
        timeSlider!!.isEnabled = false
        pauseButton!!.setVisibility(View.GONE)
    }

    override fun onResume() {
        // Important! Refresh our list when we return to this activity (from another one)

        super.onResume()
    }

    private fun updateSlider(){
            time+=1f
            if (time<maxTime) {
                timeSlider!!.setValue(time)
                timeView!!.text = "" + (time/60).toInt() + ":" + (time%60).toInt()
            }
    }

    public fun setNewTimeInfo(newTime: Int, newMaxTime: Int, musicName: String){
        time = newTime.toFloat()/1e3f
        maxTime = newMaxTime.toFloat()/1e3f
        timeSlider!!.setValueTo(maxTime)
        musicTextView!!.text = musicName
    }

    public fun setAsHost()
    {
        timeSlider!!.isEnabled = true
        pauseButton!!.setVisibility(View.VISIBLE)
    }
}
