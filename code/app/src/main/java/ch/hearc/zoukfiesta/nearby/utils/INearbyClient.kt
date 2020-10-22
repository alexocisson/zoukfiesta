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
            votes : Map<String, UInt>,
            currentMusicTime : UInt,
            currentMusicLength : UInt) -> Unit
    )
    public fun onAvailable(
        lmbd: (musics : Array<String>)-> Unit
    )
    public fun onKick(lmbd : () -> Unit)
}