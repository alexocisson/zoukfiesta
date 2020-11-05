package ch.hearc.zoukfiesta.nearby.utils

import android.app.Activity
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.Payload


class NearbyServer(
    private val endpointId: String, private val context : Activity,
    override var onSkip: ((musicName: String) -> Unit)? = null,
    override var onWhat: (() -> Unit)? = null,
    override var onMusics: (() -> Unit)? = null,
    override var onAdd: ((musicName: String) -> Unit)? = null
) : INearbyServer, NearbyListener() {

    override fun sendPlaylist(
        votes: Map<String, Int>,
        currentMusicTime: Int,
        currentMusicLength: Int
    ) {
        //Command name
        val commandName = CommandsName.PLAYLIST

        //Payload
        val payload = Payload.fromBytes(
            Tools.createPayload(
                commandName,
                votes,
                currentMusicTime,
                currentMusicLength
            )
        )

        //Send
        Nearby.getConnectionsClient(context).sendPayload(endpointId, payload)
    }

    override fun sendAvailable(musics: Array<String>) {
        //Command name
        val commandName = CommandsName.AVAILABLE

        //Payload
        val payload = Payload.fromBytes(Tools.createPayload(commandName, musics))

        //Send
        //Nearby.getConnectionsClient(context).sendPayload(endpointId, payload)
    }

    override fun sendKick() {
        //Command name
        val commandName = CommandsName.SKIP;

        //Payload
        val payload = Payload.fromBytes(Tools.createPayload(commandName))

        //Send
        Nearby.getConnectionsClient(context).sendPayload(endpointId, payload)
    }

    override fun myCallback(bytes: ByteArray) {

    }
}