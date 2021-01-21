package ch.hearc.zoukfiesta.utils.music

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri

class Music(
    val name: String,
    val voteSkip: Int = 0,
    val resourceUri: Uri? = null,
    val context: Context? = null
) {
    var duration: Int = 0

    init {
        val mmr = MediaMetadataRetriever()
        mmr.setDataSource(context,  resourceUri)
        duration =
            (mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toInt() ?: mmr.release()) as Int
    }
}
