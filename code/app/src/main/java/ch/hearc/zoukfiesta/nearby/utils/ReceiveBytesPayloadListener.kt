package ch.hearc.zoukfiesta.nearby.utils

import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.PayloadCallback
import com.google.android.gms.nearby.connection.PayloadTransferUpdate


internal class ReceiveBytesPayloadListener : PayloadCallback() {
    override fun onPayloadReceived(
        endpointId: String,
        payload: Payload
    ) {
        // This always gets the full data of the payload. Will be null if it's not a BYTES
        // payload. You can check the payload type with payload.getType().
        val receivedBytes = payload.asBytes()
    }

    override fun onPayloadTransferUpdate(
        endpointId: String,
        update: PayloadTransferUpdate
    ) {
        // Bytes payloads are sent as a single chunk, so you'll receive a SUCCESS update immediately
        // after the call to onPayloadReceived().
    }
}