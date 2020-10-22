package ch.hearc.zoukfiesta.nearby.utils

import com.google.android.gms.nearby.connection.Payload
import kotlinx.serialization.*
import kotlinx.serialization.json.Json

class NearbyServer(override var isListening: Boolean) : INearbyServer, INearbyListener {
    override fun sendPlaylist(
        playlist: Array<String>,
        votes: Map<String, UInt>,
        currentlyPlaying: String,
        currentMusicTime: Float,
        currentMusicLength: Float
    ) {
        //Command name
        val commandName = CommandsName.PLAYLIST

        //Payload
        val payload = Payload.fromBytes(Tools.CreatePayload(commandName, arrayOf(playlist, votes, currentlyPlaying, currentMusicTime, currentMusicLength)))
    }

    override fun sendKick() {
        //Command name
        val commandName = CommandsName.SKIP;

        //Payload
        val payload = Payload.fromBytes(Tools.CreatePayload(commandName))
    }

    override fun onSkip(callback: (musicName: String) -> Unit) {
    }

    override fun onWhat(callback: () -> Void) {
    }

    override fun onMusics(callback: () -> Void) {
    }

    override fun onAdd(callback: (musicName: String) -> Void) {
    }

    override fun listening() {
        while(isListening)
        {
            //Get JSON object
            val jsonObject = null

            //val commandName = jsonObject.commandName;
        }
    }
}