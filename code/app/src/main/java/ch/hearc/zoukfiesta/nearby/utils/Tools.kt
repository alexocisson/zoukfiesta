package ch.hearc.zoukfiesta.nearby.utils
import android.os.Debug
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import java.util.*

@Serializable
class DataPlaylist(val commandsName: String, val musis: Map<String, Int>, val currentMusicTime: Int, val currentMusicLength: Int)

class Tools{
    companion object {
        fun createPayload(commandsName: CommandsName, params: Array<String> = emptyArray()): ByteArray {
            // Concact command name and params in one array of string
            val data =  Array<String>(params.size + 1
            ) { i -> (if (i != 0) params[i - 1] else commandsName.toString()) }

            val json = Json.encodeToString(data)
            return json.toByteArray()
        }

        fun createPayload(commandsName: CommandsName, musis: Map<String, Int>, currentMusicTime: Int, currentMusicLength: Int): ByteArray {
            val json = Json.encodeToString(DataPlaylist(commandsName.toString(), musis, currentMusicTime, currentMusicLength)) //Work
            return json.toByteArray()
        }
    }
}