package ch.hearc.zoukfiesta.fragments
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import ch.hearc.zoukfiesta.R
import ch.hearc.zoukfiesta.activity.ZoukHostActivity

class CreateFragment : Fragment() {

    private var startButton: Button? = null
    private var fiestaNameTextView: TextView? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.create_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        retrieveViews(view!!)
        setUpViews(activity!!)
    }

    /**
     * Retrieve all views inside res/layout/garbage_list_fragment.xml.
     */
    private fun retrieveViews(view: View) {
        startButton = view.findViewById<View>(R.id.startZouking) as Button
        fiestaNameTextView = view.findViewById<View>(R.id.fiestaNameLabel) as TextView
    }

    /**
     * Construct our logic. What we wants is the following:
     *
     * - being able to filter the garbage list;
     * - being able to see a garbage details by clicking an item in the list;
     * - being able to start the creation of a new garbage by clicking the "Add" button.
     */
    private fun setUpViews(activity: Activity) {
        startButton!!.setOnClickListener {

            val intent = Intent(activity, ZoukHostActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        // Important! Refresh our list when we return to this activity (from another one)

        super.onResume()
    }
}
