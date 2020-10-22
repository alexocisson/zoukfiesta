package ch.hearc.zoukfiesta.nearby.utils

interface INearbyServer {
    //Receive
    public fun onSkip(lmbd: (musicName : String) -> Unit)
    public fun onWhat(lmbd: () -> Unit)
    public fun onMusics(lmbd: () -> Unit)
    public fun onAdd( lmbd: (musicName : String) -> Unit)
    
    //Send
    public fun sendPlaylist(
            playlist : Array<String>,
            votes : Map<String, UInt>,
            currentlyPlaying : String,
            currentMusicTime : Float,
            currentMusicLength : Float)
    public fun sendKick()
}