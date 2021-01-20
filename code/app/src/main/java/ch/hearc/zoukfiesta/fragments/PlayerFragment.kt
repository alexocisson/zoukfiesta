package ch.hearc.zoukfiesta.fragments
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import ch.hearc.zoukfiesta.*
import ch.hearc.zoukfiesta.activity.CreateActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.slider.Slider

class
PlayerFragment : Fragment() {

    private var pauseButton: FloatingActionButton? = null
    private var skipButton: FloatingActionButton? = null
    private var musicTextView: TextView? = null
    private var timeView: TextView? = null
    var timeSlider: Slider? = null
    private var isPlaying: Boolean = true

    private var time: Float = 1f
    private var maxTime: Float = 1000f
    private val mainHandler = Handler(Looper.getMainLooper())
    private var isHost : Boolean = false;
    private var maxTimeInit: Float = 0f
    private var musicNameInit: String = ""

    public var onValueChange: ((slider: Slider, value: Float, fromUser: Boolean) -> Unit)? = null
    public var onPause: ((it: View) -> Unit)? = null

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

        timeSlider!!.isEnabled = isHost
        pauseButton!!.setVisibility(if (isHost) View.VISIBLE else View.GONE)

        timeSlider!!.setValueTo(maxTimeInit)
        musicTextView!!.text = musicNameInit

        //Set on click event
        timeSlider!!.addOnChangeListener { slider, value, fromUser ->
            onValueChange?.let { it(slider, value, fromUser) }
        }

        //Set on pause event
        pauseButton!!.setOnClickListener {it ->
            isPlaying = !isPlaying
            onPause?.let { it1 -> it1(it) }
        }
    }

    override fun onResume() {
        // Important! Refresh our list when we return to this activity (from another one)

        super.onResume()
    }

    private fun updateSlider(){
        if(isPlaying)
        {
            time+=1f
            if (time<maxTime) {
                timeSlider!!.setValue(time)
                timeView!!.text = "" + (time/60).toInt() + ":" + String.format("%02d",(time%60).toInt())
            }
        }
    }

    fun setNewTimeInfo(newTime: Int, newMaxTime: Int, musicName: String){
        time = newTime.toFloat()/1e3f
        maxTime = newMaxTime.toFloat()/1e3f

        if(timeSlider != null) {
            timeSlider!!.setValue(time)
            timeSlider!!.setValueTo(maxTime)
            musicTextView!!.text = musicName
            timeView!!.text = "" + (time/60).toInt() + ":" + String.format("%02d",(time%60).toInt())
        }
        else
        {
            maxTimeInit = maxTime
            musicNameInit = musicName
        }
    }

    fun setAsHost()
    {
        isHost = true
    }

    fun setCurrentTime(newTime: Float)
    {
        time =  newTime
    }
}
