package ch.hearc.zoukfiesta.utils.music

import android.content.res.AssetFileDescriptor
import ch.hearc.zoukfiesta.R

object MusicStore   {
    var musics: MutableList<Music> = emptyList<Music>().toMutableList()

    public fun addToStore(musicName : String, voteSkip : Int, ressourceId : Int, assetFileDescriptor: AssetFileDescriptor)
    {
        musics.add(Music(musicName, voteSkip, ressourceId, assetFileDescriptor));
    }

    public fun findMusicByID(id: Int): Music? {
        var result: Music? = null

        for (music in musics) {
            if (music.resourceId == id) {
                result = music
            }
        }
        return result
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