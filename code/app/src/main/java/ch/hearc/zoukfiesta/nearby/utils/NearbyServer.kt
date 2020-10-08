package ch.hearc.zoukfiesta.nearby.utils

import com.google.android.gms.nearby.connection.Payload
import kotlinx.serialization.*
import kotlinx.serialization.json.Json

class NearbyServer : INearbyServer {

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

    override fun onSkip(lmbd: (musicName: String) -> Void) {
        //Command name
        val commandName = CommandsName.SKIP
    }

    override fun onWhat() {
        //Command name
        val commandName = CommandsName.WHAT
    }

    override fun onMusics() {
        //Command name
        val commandName = CommandsName.MUSICS
    }

    override fun onAdd(lmbd: (musicName: String) -> Void) {
        //Command name
        val commandName = CommandsName.ADD
    }
}