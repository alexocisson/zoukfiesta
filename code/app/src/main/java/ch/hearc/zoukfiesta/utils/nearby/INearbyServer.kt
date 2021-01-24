package ch.hearc.zoukfiesta.utils.nearby

interface INearbyServer {
    //Receive
    var onSkip: ((endpointId:String,musicIndex : Int) -> Unit)?
    var onAdd: ((musicName : String) -> Unit)?
    
    //Send
    public fun sendPlaylist(
            endpointId: String,
            musics : Map<String, String>,
            currentMusicTime : Int,
            currentMusicLength : Int)
    public fun sendAvailable(endpointId: String, musics : Map<String, String>)
    public fun sendKick(endpointId: String)
    public fun sendPause(endpointId: String, isPlaying: Boolean)
}