package ch.hearc.zoukfiesta.nearby.utils

interface INearbyClient {
    //Receive
    var onPlaylist: ((votes: Map<String, UInt>, currentMusicTime: UInt, currentMusicLength: UInt) -> Unit)?
    var onAvailable: ((musics: Array<String>) -> Unit)?
    var onKick: (() -> Unit)?

    //Send
    public fun sendSkip(musicName : String)
    public fun sendWhat()
    public fun sendMusics()
    public fun sendAdd(musicName : String)
}