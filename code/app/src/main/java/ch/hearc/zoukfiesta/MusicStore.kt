package ch.hearc.zoukfiesta

import java.util.ArrayList

object MusicStore   {
    val MUSICS: MutableList<Music> = ArrayList()

    init {

        MUSICS.add(Music(1,"Arouf Gangsta - PALALA"))
        MUSICS.add(Music(2,"Kaaris - Bouchon de Liège"))
        MUSICS.add(Music(3,"Gigi d'agostino - L'Amours Toujours"))
    }

    fun findMusicByID(id: Int): Music? {
        var result: Music? = null

        for (music in MUSICS) {
            if (music.id == id) {
                result = music
            }
        }
        return result
    }

    fun findMusicByName(name: String): Music? {
        var result: Music? = null

        for (music in MUSICS) {
            if (music.name == name) {
                result = music
            }
        }
        return result
    }
}