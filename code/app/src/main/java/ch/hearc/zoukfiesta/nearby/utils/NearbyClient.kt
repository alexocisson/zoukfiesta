package ch.hearc.zoukfiesta.nearby.utils

import com.google.android.gms.nearby.connection.Payload
import kotlinx.serialization.*
import kotlinx.serialization.json.Json

class NearbyClient : INearbyClient, INearbyListener {
    override fun sendSkip(musicName: String) {
        //Command name
        val commandName = CommandsName.SKIP

        //Payload
        val payload = Payload.fromBytes(Tools.CreatePayload(commandName, arrayOf(musicName)))
    }

    override fun sendWhat() {
        //Command name
        val commandName = CommandsName.WHAT

        //Payload
        val payload = Payload.fromBytes(Tools.CreatePayload(commandName))
    }

    override fun sendMusics() {
        //Command name
        val commandName = CommandsName.MUSICS

        //Payload
        val payload = Payload.fromBytes(Tools.CreatePayload(commandName))
    }

    override fun sendAdd(musicName: String) {
        //Command name
        val commandName = CommandsName.ADD

        //Payload
        val payload = Payload.fromBytes(Tools.CreatePayload(commandName, arrayOf(musicName)))
    }

    override fun onPlaylist(callback: (playlist: Array<String>, votes: Map<String, UInt>, currentlyPlaying: String, currentMusicTime: Float, currentMusicLength: Float) -> Void) {
    }

    override fun onKick(callback: () -> Void) {
    }

    override fun listening() {

    }

}