package ch.hearc.zoukfiesta.utils.nearby

interface INearbyClient {
    //Receive
    var onPlaylist: ((votes: Map<String, String>, currentMusicTime: Int, currentMusicLength: Int,/* isPlaying : Boolean*/) -> Unit)?
    var onAvailable: ((musics: Map<String, String>) -> Unit)?
    var onKick: (() -> Unit)?
    var onPause: ((isPlaying : Boolean) -> Unit)?

    //Send
    public fun sendSkip(musicIndex : Int)
    public fun sendAdd(musicName : String)
}