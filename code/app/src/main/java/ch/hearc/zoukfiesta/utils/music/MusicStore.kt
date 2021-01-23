package ch.hearc.zoukfiesta.utils.music

import android.content.Context
import android.net.Uri

object MusicStore   {
    var availableMusics: MutableList<Music> = emptyList<Music>().toMutableList()
    var musicQueue: MutableList<Music> = emptyList<Music>().toMutableList()

    public fun addToStore(musicName : String, artistName:String,voteSkip : Int, resourceUri : Uri, context: Context?)
    {
        availableMusics.add(Music(musicName, artistName,voteSkip, resourceUri, context));
    }

    public fun findMusicByName(name: String): Music? {
        var result: Music? = null

        for (music in availableMusics) {
            if (music.name == name) {
                result = music
            }
        }
        return result
    }
}