package ch.hearc.zoukfiesta.nearby.utils

import com.google.android.gms.nearby.connection.Payload
import kotlinx.serialization.*
import kotlinx.serialization.json.Json

class NearbyClient : INearbyClient {
    override fun sendSkip(musicName: String) {
        Json.encodeToString("test")
        //Payload
        var payload = Payload.fromBytes(musicName.toByteArray());
    }

    override fun sendWhat() {
        TODO("Not yet implemented")
    }

    override fun sendMusics() {
        TODO("Not yet implemented")
    }

    override fun sendAdd(musicName: String) {
        TODO("Not yet implemented")
    }

    override fun onPlaylist(lmbd: (playlist: Array<String>, votes: Map<String, UInt>, currentlyPlaying: String, currentMusicTime: Float, currentMusicLength: Float) -> Void) {
        TODO("Not yet implemented")
    }

    override fun onKick(lmbd: () -> Void) {
        TODO("Not yet implemented")
    }

}