package ch.hearc.zoukfiesta.nearby.utils

import com.google.android.gms.nearby.connection.Payload

class NearbyClient(override var isListening: Boolean = false) : INearbyClient, INearbyListener {
    override fun sendSkip(musicName: String) {
        //Command name
        val commandName = CommandsName.SKIP

        //Payload
        val payload = Payload.fromBytes(Tools.createPayload(commandName, arrayOf(musicName)))
    }

    override fun sendWhat() {
        //Command name
        val commandName = CommandsName.WHAT

        //Payload
        val payload = Payload.fromBytes(Tools.createPayload(commandName))
    }

    override fun sendMusics() {
        //Command name
        val commandName = CommandsName.MUSICS

        //Payload
        val payload = Payload.fromBytes(Tools.createPayload(commandName))
    }

    override fun sendAdd(musicName: String) {
        //Command name
        val commandName = CommandsName.ADD

        //Payload
        val payload = Payload.fromBytes(Tools.createPayload(commandName, arrayOf(musicName)))
    }

    override fun onPlaylist(lmbd: (votes: Map<String, UInt>, currentMusicTime: UInt, currentMusicLength: UInt) -> Unit) {
    }

    override fun onAvailable(lmbd: (musics: Array<String>) -> Unit) {

    }

    override fun onKick(lmbd: () -> Unit) {
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