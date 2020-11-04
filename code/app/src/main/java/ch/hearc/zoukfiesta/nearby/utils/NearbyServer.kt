package ch.hearc.zoukfiesta.nearby.utils

import com.google.android.gms.nearby.connection.Payload
import kotlinx.serialization.*
import kotlinx.serialization.json.Json

class NearbyServer(override var isListening: Boolean = false) : INearbyServer, INearbyListener {
    override fun sendPlaylist(
        votes: Map<String, UInt>,
        currentMusicTime: UInt,
        currentMusicLength: UInt
    ) {
        //Command name
        val commandName = CommandsName.PLAYLIST

        //Payload
        val payload = Payload.fromBytes(Tools.CreatePayload(commandName, votes, currentMusicTime, currentMusicLength))
    }

    override fun sendAvailable(musics: Array<String>) {
        //Command name
        val commandName = CommandsName.AVAILABLE

        //Payload
        val payload = Payload.fromBytes(Tools.CreatePayload(commandName, musics))
    }

    override fun sendKick() {
        //Command name
        val commandName = CommandsName.SKIP;

        //Payload
        val payload = Payload.fromBytes(Tools.CreatePayload(commandName))
    }

    override fun onSkip(callback: (musicName: String) -> Unit) {
    }

    override fun onWhat(callback: () -> Unit) {
    }

    override fun onMusics(callback: () -> Unit) {
    }

    override fun onAdd(callback: (musicName: String) -> Unit) {
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