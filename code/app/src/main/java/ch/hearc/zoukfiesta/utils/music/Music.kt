package ch.hearc.zoukfiesta.utils.music

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import java.io.File

class Music(
    private val fileName: String,
    var artist: String = "",
    var voteSkip: Int = 0,
    val resourceUri: Uri? = null,
    val context: Context? = null
) {
    var duration: Int = 0
    var voteNeeded:Int = 1
    var voters : MutableSet<String> = mutableSetOf()
    lateinit var name: String

    init {
        if (resourceUri != null && context!=null)
        {
            val mmr = MediaMetadataRetriever()
            mmr.setDataSource(context,  resourceUri)
            duration = (mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toInt() ?: mmr.release()) as Int
            val metadataName = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
            val metadataArtist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)

            name = metadataName ?: File(fileName).nameWithoutExtension
            artist = metadataArtist ?: "INCONNU"
        }
        else
        {
            name=fileName
        }
    }

    fun clone():Music
    {
        return Music(fileName,artist,voteSkip,resourceUri,context)
    }

    override fun toString(): String {
        return artist + " - " + name
    }
}
