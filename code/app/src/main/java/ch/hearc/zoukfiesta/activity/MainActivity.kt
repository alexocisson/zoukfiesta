package ch.hearc.zoukfiesta.activity
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
import ch.hearc.zoukfiesta.R
import ch.hearc.zoukfiesta.utils.music.MusicAdapter
import ch.hearc.zoukfiesta.utils.nearby.*
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject
import java.io.*
import java.lang.Exception


class MainActivity : AppCompatActivity() {

    // Our handle to Nearby Connections
//    private var connectionsClient: ConnectionsClient? = null

    // Our randomly generated name
//    private val username: String = "Yo " + Random(32131).nextInt().toString()
//    private val username: String = "Utilisateur " + (0..100).random().toString()

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



    private var usernameTextField: TextView? = null
    private var setUsernameButton: Button? = null
    private var addHostButton: FloatingActionButton? = null
    private var endpointListView: ListView? = null
    private var endpointSearchView: SearchView? = null
    private var nearbyEndPointAdapter: NearbyEndPointAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!hasPermissions(this, *REQUIRED_PERMISSIONS)) {
            requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_REQUIRED_PERMISSIONS);
        }
        else
        {
            init()
        }
    }

    private fun init() {
        //Pour garder le username dans un fichier
        var userConfigJSON: JSONObject
        try {
            userConfigJSON = readJSONFile("user.config")
            if (!userConfigJSON.has("username")) {
                userConfigJSON.put("username", "User " + (0..100).random().toString())
                writeJSONFile(userConfigJSON, "user.config")
            }
        } catch (e: Exception) {
            println(e.toString())
            userConfigJSON = JSONObject();
            userConfigJSON.put("username", "User " + (0..100).random().toString())
            writeJSONFile(userConfigJSON, "user.config")
        }


        NearbySingleton.USERNAME = userConfigJSON.get("username").toString()
        NearbySingleton.PACKAGE_NAME = packageName
        NearbySingleton.ENDPOINTDISCOVERYCALLBACK = endpointDiscoveryCallback
        NearbySingleton.musicPointAdapter = MusicAdapter(this)

        retrieveViews()
        setUpViews()


        NearbySingleton.nearbyClient = NearbyClient(this, NearbySingleton.USERNAME);

        //        startAdvertising();
        NearbySingleton.nearbyClient!!.startDiscovery(
            NearbySingleton.PACKAGE_NAME, endpointDiscoveryCallback,
            NearbySingleton.STRATEGY
        );

        NearbyEndPointStore.ENDPOINTS.add(NearbyEndpoint("endpointID", "Username", null))
    }

    private fun retrieveViews() {
        usernameTextField = findViewById<View>(R.id.usernameEditField) as TextInputEditText
        setUsernameButton = findViewById<View>(R.id.setUsername) as Button
        addHostButton = findViewById<View>(R.id.addButton) as FloatingActionButton
        endpointListView = findViewById<View>(R.id.listView) as ListView
        endpointSearchView = findViewById<View>(R.id.searchView) as SearchView
    }

    private fun setUpViews() {
        nearbyEndPointAdapter = NearbyEndPointAdapter(this)

        // Tell by which adapter we will handle our list
        endpointListView!!.adapter = nearbyEndPointAdapter

        // Miscellaneous configuration for our search view
        endpointSearchView!!.isSubmitButtonEnabled = true
        endpointSearchView!!.queryHint = "Endpoint name..."

        endpointListView!!.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val endpoint = endpointListView!!.getItemAtPosition(position) as NearbyEndpoint

            val intent = Intent(this, ConnectionActivity::class.java)

            intent.putExtra("endpointName", endpoint.name)
            intent.putExtra("endpointId", endpoint.id)

            startActivity(intent)

        }

        // The core for the search view: what to do when the text change!
        endpointSearchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                // Do nothing when clicking the submit button (displayed ">") -> return false

                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {

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

        // Start the activity to add an host when clicking the "Add" button
        addHostButton!!.setOnClickListener {
            val intent = Intent(this, CreateActivity::class.java)
            startActivity(intent)
        }

        usernameTextField?.text = NearbySingleton.USERNAME
        setUsernameButton!!.setOnClickListener {
            var name :String = usernameTextField?.text.toString()
            var toastText = "Hello toast!"
            if (!TextUtils.isEmpty(name))
            {
                NearbySingleton.USERNAME = name
                toastText = "Username changed to " + NearbySingleton.USERNAME

                val userConfigJSON = readJSONFile("user.config")
                userConfigJSON.put("username", name)
                writeJSONFile(userConfigJSON,"user.config")
            }
            else
            {
                usernameTextField?.text = NearbySingleton.USERNAME
                toastText = "ERROR : Username cannot be empty"
            }

            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(this, toastText, duration)
            toast.show()
        }
    }

    private fun readJSONFile(fileName:String):JSONObject
    {
        val jsonFile = File(this.filesDir, fileName)
        if (jsonFile.exists())
        {
            val fileReader = FileReader(jsonFile)
            val bufferedReader = BufferedReader(fileReader)
            val stringBuilder = StringBuilder()
            var line: String? = bufferedReader.readLine()
            while (line != null) {
                stringBuilder.append(line).append("\n")
                line = bufferedReader.readLine()
            }
            bufferedReader.close()
            // This responce will have Json Format String
            val responce = stringBuilder.toString()
            return JSONObject(responce)
        }
        throw Exception("File "+fileName+" doesn't exist")
    }

    private fun writeJSONFile(jsonObject: JSONObject,fileName:String)
    {
        val jsonFile = File(this.filesDir, fileName)
        val fileWriter = FileWriter(jsonFile)
        val bufferedWriter = BufferedWriter(fileWriter)
        bufferedWriter.write(jsonObject.toString())
        bufferedWriter.close()
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
//        recreate()
        init()
    }

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

    override fun onPause() {
        println("MainActivity on Pause")
//        NearbySingleton.nearbyClient?.stopDiscovery()
//        NearbyEndPointStore.ENDPOINTS.clear()
//        nearbyEndPointAdapter?.notifyDataSetChanged()
        super.onPause()

    }

    override fun onResume() {
        println("MainActivity on Resume")
//        NearbySingleton.nearbyClient!!.startDiscovery(NearbySingleton.PACKAGE_NAME,NearbySingleton.ENDPOINTDISCOVERYCALLBACK,
//            NearbySingleton.STRATEGY)
        super.onResume()
    }
}