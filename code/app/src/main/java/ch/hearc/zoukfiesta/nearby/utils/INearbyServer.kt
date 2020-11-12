package ch.hearc.zoukfiesta.nearby.utils

interface INearbyServer {
    //Receive
    var onSkip: ((musicName : String) -> Unit)?
    var onWhat: (() -> Unit)?
    var onMusics: (() -> Unit)?
    var onAdd: ((musicName : String) -> Unit)?
    
    //Send
    public fun sendPlaylist(
            endpointId: String,
            votes : Map<String, Int>,
            currentMusicTime : Int,
            currentMusicLength : Int)
    public fun sendAvailable(endpointId: String, musics : Array<String>)
    public fun sendKick(endpointId: String)
}