package ch.hearc.zoukfiesta.fragments
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import ch.hearc.zoukfiesta.R
import ch.hearc.zoukfiesta.activity.ZoukHubActivity
import ch.hearc.zoukfiesta.utils.nearby.NearbySingleton
import com.google.android.gms.nearby.Nearby


class ConnectionFragment : Fragment() {

    private var startButton: Button? = null
    private var fiestaNameTextView: TextView? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.connection_fragment, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        retrieveViews(view!!)
        setUpViews(activity!!)
    }

    private fun retrieveViews(view: View) {
        startButton = view.findViewById<View>(R.id.startZouking) as Button
        fiestaNameTextView = view.findViewById<View>(R.id.fiestaNameLabel) as TextView
    }

    private fun setUpViews(activity: Activity) {
        //Get the endpoint id and the endpont name (the fiesta name !)
        val fiestaName = activity.intent.getStringExtra("endpointName")
        val endpointId = activity.intent.getStringExtra("endpointId")

        fiestaNameTextView!!.text = fiestaName.toString()

        startButton!!.setOnClickListener {

            if (endpointId != null) {
                NearbySingleton.nearbyClient?.onConnection = {
                    val intent = Intent(context, ZoukHubActivity::class.java)
                    startActivity(intent)
                }
                NearbySingleton.nearbyClient?.requestConnection(endpointId)
            };
        }
    }

    override fun onResume() {
        // Important! Refresh our list when we return to this activity (from another one)

        super.onResume()
    }
}
