package ch.hearc.zoukfiesta.utils.nearby

import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.PayloadCallback
import com.google.android.gms.nearby.connection.PayloadTransferUpdate


abstract class NearbyListener {
    protected var isListening : Boolean = false

    public fun start()
    {
        isListening = true
    }

    public fun stop()
    {
        isListening = false
    }

    protected abstract fun myCallback(bytes: ByteArray)

    // Callbacks for receiving payloads
    private val payloadCallback: PayloadCallback = object : PayloadCallback() {
        override fun onPayloadReceived(endpointId: String, payload: Payload) {
            val receivedBytes = payload.asBytes()

            //IF they are bytes, call our callback function and listening
            if (receivedBytes != null && isListening) {
                myCallback(receivedBytes)
            }
        }

        override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {
            // Bytes payloads are sent as a single chunk, so you'll receive a SUCCESS update immediately
            // after the call to onPayloadReceived().
        }
    }
}