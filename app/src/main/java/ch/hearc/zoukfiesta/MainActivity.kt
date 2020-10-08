package ch.hearc.zoukfiesta

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
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

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById<ListView>(R.id.listSession)

        val listItems = arrayOfNulls<String>(10)

//        for (i in 0 until recipeList.size) {
//            val recipe = recipeList[i]
//            listItems[i] = recipe.title
//        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems)
        listView.adapter = adapter
        adapter.add("yooooo")

        connectionsClient = Nearby.getConnectionsClient(this);

//        startAdvertising();
//        startDiscovery();
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
//                Log.i(TAG, "onEndpointFound: endpoint found, connecting")
//                connectionsClient!!.requestConnection(
//                    codeName,
//                    endpointId,
//                    connectionLifecycleCallback
//                )
            }

            override fun onEndpointLost(endpointId: String) {}
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