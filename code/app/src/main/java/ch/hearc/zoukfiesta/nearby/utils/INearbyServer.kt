package ch.hearc.zoukfiesta.nearby.utils

interface INearbyServer {

    //Receive
    fun onSkip(lmbd: (musicName : String) -> Void)
    fun onWhat()
    fun onMusics()
    fun onAdd( lmbd: (musicName : String) -> Void)
    
    //Receive
    fun sendPlaylist(
            playlist : Array<String>,
            votes : Map<String, UInt>,
            currentlyPlaying : String,
            currentMusicTime : Float,
            currentMusicLength : Float)
    fun sendKick()
}