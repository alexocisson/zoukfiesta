package ch.hearc.zoukfiesta.utils.nearby

interface INearbyClient {
    //Receive
    var onPlaylist: ((votes: Map<String, String>, currentMusicTime: Int, currentMusicLength: Int) -> Unit)?
    var onAvailable: ((musics: Array<String>) -> Unit)?
    var onKick: (() -> Unit)?

    //Send
    public fun sendSkip(musicName : String)
    public fun sendWhat()
    public fun sendMusics()
    public fun sendAdd(musicName : String)
}