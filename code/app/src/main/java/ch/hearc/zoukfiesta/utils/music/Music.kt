package ch.hearc.zoukfiesta.utils.music

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri

class Music(
    var name: String,
    var artist: String = "",
    val voteSkip: Int = 0,
    val resourceUri: Uri? = null,
    val context: Context? = null
) {
    var duration: Int = 0

    init {
        if (resourceUri != null && context!=null)
        {
            val mmr = MediaMetadataRetriever()
            mmr.setDataSource(context,  resourceUri)
            duration = (mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toInt() ?: mmr.release()) as Int
            name = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE).toString()
            artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST).toString()
        }
    }

    override fun toString(): String {
        return artist + " - " + name
    }
}
