package ch.hearc.zoukfiesta.utils.nearby

interface INearbyServer {
    //Receive
    var onSkip: ((musicName : String) -> Unit)?
    var onWhat: (() -> Unit)?
    var onMusics: (() -> Unit)?
    var onAdd: ((musicName : String) -> Unit)?
    
    //Send
    public fun sendPlaylist(
            endpointId: String,
            musics : Map<String, String>,
            currentMusicTime : Int,
            currentMusicLength : Int)
    public fun sendAvailable(endpointId: String, musics : Array<String>)
    public fun sendKick(endpointId: String)
}