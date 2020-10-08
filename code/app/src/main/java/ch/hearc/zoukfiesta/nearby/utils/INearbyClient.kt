package ch.hearc.zoukfiesta.nearby.utils

interface INearbyClient {

    //Send
    fun sendSkip(musicName : String)
    fun sendWhat()
    fun sendMusics()
    fun sendAdd(musicName : String)

    //Receive
    fun onPlaylist(
        lmbd: (
            playlist : Array<String>,
            votes : Map<String, UInt>,
            currentlyPlaying : String,
            currentMusicTime : Float,
            currentMusicLength : Float) -> Void
    )
    fun onKick(lmbd : () -> Void)
}