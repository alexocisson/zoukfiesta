package ch.hearc.zoukfiesta.utils.music

import android.content.res.AssetFileDescriptor
import android.media.MediaMetadataRetriever
import ch.hearc.zoukfiesta.R
import java.io.FileDescriptor

class Music(val name: String, val voteSkip: Int = 0, val resourceId: Int? = null, val assetFileDescriptor: AssetFileDescriptor? = null) {

    var duration: Int = 0

    init {

        //Set the music duration
        if (assetFileDescriptor != null) {
            val mmr = MediaMetadataRetriever()
            mmr.setDataSource(assetFileDescriptor.fileDescriptor, assetFileDescriptor.startOffset, assetFileDescriptor.length);

            duration =
                (mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toInt() ?: mmr.release()) as Int
        }
    }
}
