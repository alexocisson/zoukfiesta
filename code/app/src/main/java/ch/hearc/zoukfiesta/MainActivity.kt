package ch.hearc.zoukfiesta
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import ch.hearc.zoukfiesta.fragments.ConnectionFragment
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.*
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    // Our handle to Nearby Connections
    private var connectionsClient: ConnectionsClient? = null

    // Our randomly generated name
    private val codeName: String = "Yo " + Random(32131).nextInt().toString()

    private val TAG : String = "Zoukfiesta"

    private val REQUIRED_PERMISSIONS = arrayOf<String>(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.ACCESS_WIFI_STATE,
        Manifest.permission.CHANGE_WIFI_STATE,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    private val REQUEST_CODE_REQUIRED_PERMISSIONS = 1

    private val STRATEGY: Strategy = Strategy.P2P_STAR

    val arrayList: ArrayList<HashMap<String, String?>> = ArrayList()
    var simpleAdapter : SimpleAdapter? = null

    private var addGarbageButton: Button? = null
    private var endpointListView: ListView? = null
    private var endpointSearchView: SearchView? = null
    private var nearbyEndPointAdapter: NearbyEndPointAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        retrieveViews()
        setUpViews()

        connectionsClient = Nearby.getConnectionsClient(this);

        startAdvertising();
        startDiscovery();

        NearbyEndPointStore.ENDPOINTS.add(NearbyEndpoint("SAlut", "ça va ?",null))
    }

    /**
     * Retrieve all views inside res/layout/garbage_list_activity.xml.
     */
    private fun retrieveViews() {
        addGarbageButton = findViewById<View>(R.id.addGarbageButton) as Button
        endpointListView = findViewById<View>(R.id.garbageListView) as ListView
        endpointSearchView = findViewById<View>(R.id.garbageSearchView) as SearchView
    }

    /**
     * Construct our logic. What we wants is the following:
     *
     * - being able to filter the garbage list;
     * - being able to see a garbage details by clicking an item in the list;
     * - being able to start the creation of a new garbage by clicking the "Add" button.
     */
    private fun setUpViews() {
        nearbyEndPointAdapter = NearbyEndPointAdapter(this)

        // Tell by which adapter we will handle our list
        endpointListView!!.adapter = nearbyEndPointAdapter

        // See a garbage details when clicking on it
//        garbageListView!!.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
//            val garbage = garbageListView!!.getItemAtPosition(position) as NearbyEndpoint
//
//            val intent = Intent(this@GarbageListActivity, GarbageDetailsActivity::class.java)
//
//            intent.putExtra("garbageName", garbage.name)
//
//            startActivity(intent)
//        }

        // Miscellaneous configuration for our search view
        endpointSearchView!!.isSubmitButtonEnabled = true
        endpointSearchView!!.queryHint = "Endpoint name..."

        endpointListView!!.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val endpoint = endpointListView!!.getItemAtPosition(position) as NearbyEndpoint

            val intent = Intent(this, ConnectionActivity::class.java)

            intent.putExtra("endpointName", endpoint.name)

            startActivity(intent)

        }

        // The core for the search view: what to do when the text change!
        endpointSearchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                // Do nothing when clicking the submit button (displayed ">") -> return false

                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // When the text change, filter our list of garbages

                val filter = nearbyEndPointAdapter!!.filter!!

                if (TextUtils.isEmpty(newText)) {
                    // Empty search field = no filtering
                    filter.filter(null)
                } else {
                    filter.filter(newText)
                }

                // Something was done -> return true instead of false
                return true
            }
        })

        // Start the activity to add a garbage when clicking the "Add" button
//        addGarbageButton!!.setOnClickListener {
//            val intent = Intent(this@GarbageListActivity, AddGarbageActivity::class.java)
//
//            startActivity(intent)
//        }
    }

    override fun onStart() {
        super.onStart()

        if (!hasPermissions(this, *REQUIRED_PERMISSIONS)) {
            requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_REQUIRED_PERMISSIONS);
        }
    }

    /** Returns true if the app was granted all the permissions. Otherwise, returns false.  */
    private fun hasPermissions(context: Context, vararg permissions: String): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(context, permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode !== REQUEST_CODE_REQUIRED_PERMISSIONS) {
            return
        }

        for (grantResult in grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "PERMISSION DENIED", Toast.LENGTH_LONG).show()
                finish()
                return
            }
        }
        recreate()
    }

    /** Starts looking for other players using Nearby Connections.  */
    private fun startDiscovery() {
        // Note: Discovery may fail. To keep this demo simple, we don't handle failures.
        connectionsClient?.startDiscovery(
            packageName, endpointDiscoveryCallback,
            DiscoveryOptions.Builder().setStrategy(STRATEGY).build()
        )
    }

    /** Broadcasts our presence using Nearby Connections so other players can find us.  */
    private fun startAdvertising() {
        // Note: Advertising may fail. To keep this demo simple, we don't handle failures.
        connectionsClient?.startAdvertising(
            codeName, packageName, connectionLifecycleCallback,
            AdvertisingOptions.Builder().setStrategy(STRATEGY).build()
        )?.addOnSuccessListener { unused: Void? -> }
            ?.addOnFailureListener { e: Exception? -> }
    }

//    // Callbacks for receiving payloads
//    private val payloadCallback: PayloadCallback = object : PayloadCallback() {
//        override fun onPayloadReceived(endpointId: String, payload: Payload) {
//            opponentChoice = GameChoice.valueOf(String(payload.asBytes()!!, UTF_8))
//        }
//
//        override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {
//            if (update.status == Status.SUCCESS && myChoice != null && opponentChoice != null) {
//                finishRound()
//            }
//        }
//    }

    // Callbacks for finding other devices (CLIENT SIDE)
    private val endpointDiscoveryCallback: EndpointDiscoveryCallback =
        object : EndpointDiscoveryCallback() {
            override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {

                Log.i(TAG, "onEndpointFound: endpoint found")
                NearbyEndPointStore.ENDPOINTS.add(NearbyEndpoint(endpointId, info.endpointName ,info))
                nearbyEndPointAdapter?.notifyDataSetChanged()
//                var con = NearbyEndpoint(endpointId, info.endpointName ,info)
//                con.connect(connectionsClient!!,codeName,connectionLifecycleCallback)
            }

            override fun onEndpointLost(endpointId: String) {
                Log.i(TAG, "onEndpointFound: endpoint lost :(")
                var ep = NearbyEndPointStore.findEndPointByName(endpointId)
                NearbyEndPointStore.ENDPOINTS.remove(ep)
                nearbyEndPointAdapter?.notifyDataSetChanged()
            }
        }

    // Callbacks for connections to other devices (SERVER SIDE)
    private val connectionLifecycleCallback: ConnectionLifecycleCallback =
        object : ConnectionLifecycleCallback() {
            override fun onConnectionInitiated(endpointId: String, connectionInfo: ConnectionInfo) {
//                Log.i(TAG, "onConnectionInitiated: accepting connection")
//                connectionsClient!!.acceptConnection(endpointId, payloadCallback)
//                opponentName = connectionInfo.endpointName
            }

            override fun onConnectionResult(endpointId: String, result: ConnectionResolution) {
//                if (result.status.isSuccess) {
//                    Log.i(TAG, "onConnectionResult: connection successful")
//                    connectionsClient!!.stopDiscovery()
//                    connectionsClient.stopAdvertising()
//                    opponentEndpointId = endpointId
//                    setOpponentName(opponentName)
//                    setStatusText(getString(R.string.status_connected))
//                    setButtonState(true)
//                } else {
//                    Log.i(TAG, "onConnectionResult: connection failed")
//                }
            }

            override fun onDisconnected(endpointId: String) {
//                Log.i(TAG, "onDisconnected: disconnected from the opponent")
//                resetGame()
            }
        }
}