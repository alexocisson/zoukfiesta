package ch.hearc.zoukfiesta.utils.nearby
import kotlinx.serialization.*
import kotlinx.serialization.json.Json

@Serializable
class DataPlaylist(val commandsName: String, val musics: Map<String, String>, val currentMusicTime: Int, val currentMusicLength: Int)

class Tools{
    companion object {
        fun createPayload(commandsName: CommandsName, params: Array<String> = emptyArray()): ByteArray {
            // Concact command name and params in one array of string
            val data =  Array<String>(params.size + 1
            ) { i -> (if (i != 0) params[i - 1] else commandsName.toString()) }

            val json = Json.encodeToString(data)
            return json.toByteArray()
        }

        fun createPayload(commandsName: CommandsName, musics: Map<String, String>, currentMusicTime: Int, currentMusicLength: Int): ByteArray {
            val json = Json.encodeToString(DataPlaylist(commandsName.toString(), musics, currentMusicTime, currentMusicLength)) //Work
            return json.toByteArray()
        }
    }
}