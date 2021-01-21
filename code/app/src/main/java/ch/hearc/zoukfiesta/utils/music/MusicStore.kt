package ch.hearc.zoukfiesta.utils.music

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.net.Uri

object MusicStore   {
    var musics: MutableList<Music> = emptyList<Music>().toMutableList()

    public fun addToStore(musicName : String, voteSkip : Int, resourceUri : Uri, context: Context?)
    {
        musics.add(Music(musicName, voteSkip, resourceUri, context));
    }

    public fun findMusicByName(name: String): Music? {
        var result: Music? = null

        for (music in musics) {
            if (music.name == name) {
                result = music
            }
        }
        return result
    }
}