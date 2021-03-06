package ch.hearc.zoukfiesta.fragments
import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import ch.hearc.zoukfiesta.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.slider.Slider
import kotlinx.android.synthetic.main.player_fragment.*

class
PlayerFragment : Fragment() {

    private var pauseButton: FloatingActionButton? = null
    private var skipButton: FloatingActionButton? = null
    private var musicTextView: TextView? = null
    private var artistTextView: TextView? = null
    private var timeView: TextView? = null

    var timeSlider: Slider? = null
    var isPlaying: Boolean = false

    private var time: Float = 0f
    private var maxTime: Float = Float.MAX_VALUE
    private val mainHandler = Handler(Looper.getMainLooper())
    private var isHost : Boolean = false;
    private var musicNameInit: String = ""
    private var artistNameInit: String = ""
    private var isEnableInit: Boolean = false

    var onValueChange: ((slider: Slider, value: Float, fromUser: Boolean) -> Unit)? = null
    var onPause: ((it: View) -> Unit)? = null
    var onSkip: ((it: View) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

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
        artistTextView = view.findViewById<View>(R.id.artistPlayerTextView) as TextView
        timeView = view.findViewById<View>(R.id.TimeView) as TextView
        timeSlider = view.findViewById<View>(R.id.TimeSlider) as Slider
    }

    private fun setUpViews(activity: Activity) {
        //time = MusicPlayer.mediaPlayer.currentPosition;
        timeSlider!!.setValueTo(maxTime)
        timeSlider!!.setValue(time)

        timeSlider!!.isEnabled = isHost
        pauseButton!!.setVisibility(if (isHost) View.VISIBLE else View.GONE)

        musicTextView!!.text = musicNameInit
        artistTextView!!.text = artistNameInit
        timeSlider!!.isEnabled = isEnableInit

        timeSlider!!.setLabelFormatter{ secs: Float ->
            return@setLabelFormatter "" + (secs/60).toInt() + ":" + String.format(
                "%02d",
                (secs % 60).toInt()
            )
        }

        //Set on click event
        timeSlider!!.addOnChangeListener { slider, value, fromUser ->
            onValueChange?.let { it(slider, value, fromUser) }
        }

        //Set on pause event
        pauseButton!!.setOnClickListener { it ->
            onPause?.let { it1 -> it1(it) }
        }

        //Set on skip event
        skipButton!!.setOnClickListener { it ->
            onSkip?.let { it1 -> it1(it) }
        }

        //Set the time slider texte format
        timeSlider!!.setLabelFormatter { value: Float ->
            val minutes = (value / (60 * 1000)).toInt()
            val seconds = (value / 1000 % 60).toInt()
            String.format("%d:%02d", minutes, seconds)
        }

    }

    fun play()
    {
        isPlaying = true
        PauseButton.setImageResource(R.drawable.ic_baseline_pause_24)
    }

    fun pause()
    {
        isPlaying = false
        PauseButton.setImageResource(R.drawable.ic_baseline_play_arrow_24)
    }

    override fun onResume() {
        // Important! Refresh our list when we return to this activity (from another one)
        super.onResume()
    }

    private fun updateSlider(){
        if(isPlaying)
        {
            time+=1000f
        }
        if (time<maxTime) {
            timeSlider!!.setValue(time)
            timeView!!.text = "" + ((time/1000)/60).toInt() + ":" + String.format(
                "%02d",
                ((time / 1000) % 60).toInt()
            )
        }
    }

    fun setNewTimeInfo(newTime: Int, newMaxTime: Int, musicName: String, artistName: String){
        time = newTime.toFloat()
        maxTime = newMaxTime.toFloat()

        if(timeSlider != null) {
            timeSlider!!.setValueTo(maxTime)
            timeSlider!!.setValue(time)
            musicTextView!!.text = musicName
            artistTextView!!.text = artistName
            timeView!!.text = "" + ((time/1000)/60).toInt() + ":" + String.format(
                "%02d",
                ((time / 1000) % 60).toInt()
            )
        }
        else
        {
            musicNameInit = musicName
            artistNameInit = artistName
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

    fun disable()
    {
        timeSlider!!.isEnabled = false
    }

    fun enable()
    {
        timeSlider!!.isEnabled = true
    }
}
