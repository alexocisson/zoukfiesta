package ch.hearc.zoukfiesta.nearby.utils

interface INearbyClient {

    //Send
    public fun sendSkip(musicName : String)
    public fun sendWhat()
    public fun sendMusics()
    public fun sendAdd(musicName : String)

    //Receive
    public fun onPlaylist(
        lmbd: (
            playlist : Array<String>,
            votes : Map<String, UInt>,
            currentlyPlaying : String,
            currentMusicTime : Float,
            currentMusicLength : Float) -> Void
    )
    public fun onKick(lmbd : () -> Void)
}