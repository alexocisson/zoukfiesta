package ch.hearc.zoukfiesta.nearby.utils

interface INearbyServer {
    //Receive
    public fun onSkip(lmbd: (musicName : String) -> Void)
    public fun onWhat(lmbd: () -> Void)
    public fun onMusics(lmbd: () -> Void)
    public fun onAdd( lmbd: (musicName : String) -> Void)
    
    //Send
    public fun sendPlaylist(
            playlist : Array<String>,
            votes : Map<String, UInt>,
            currentlyPlaying : String,
            currentMusicTime : Float,
            currentMusicLength : Float)
    public fun sendKick()
}