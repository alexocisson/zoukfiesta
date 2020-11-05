package ch.hearc.zoukfiesta.nearby.utils

interface INearbyServer {
    //Receive
    public fun onSkip(lmbd: (musicName : String) -> Unit)
    public fun onWhat(lmbd: () -> Unit)
    public fun onMusics(lmbd: () -> Unit)
    public fun onAdd( lmbd: (musicName : String) -> Unit)
    
    //Send
    public fun sendPlaylist(
            votes : Map<String, Int>,
            currentMusicTime : Int,
            currentMusicLength : Int)
    public fun sendAvailable(musics : Array<String>)
    public fun sendKick()
}