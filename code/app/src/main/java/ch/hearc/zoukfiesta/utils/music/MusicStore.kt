package ch.hearc.zoukfiesta.utils.music

object MusicStore   {
    var musics: MutableList<Music> = emptyList<Music>().toMutableList()

    var id : Int = 0

    init {
        musics.add(Music("Arouf Gangsta - PALALA", 11,1))
        musics.add(Music("Kaaris - Bouchon de Liège", 22,2))
        musics.add(Music("Gigi d'agostino - L'Amours Toujours", 33,3))
    }

    public fun addToStore(musicName : String, voteSkip : Int)
    {
        musics.add(Music(musicName, voteSkip, id));
        id++;
    }

    public fun findMusicByID(id: Int): Music? {
        var result: Music? = null

        for (music in musics) {
            if (music.id == id) {
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