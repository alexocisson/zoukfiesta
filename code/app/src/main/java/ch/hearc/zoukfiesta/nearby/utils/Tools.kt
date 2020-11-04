package ch.hearc.zoukfiesta.nearby.utils
import android.os.Debug
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import java.util.*

class Tools{
    companion object {
        fun CreatePayload(commandsName: CommandsName, params: Array<String> = emptyArray()): ByteArray {
            // Concact command name and params in one array of string
            val data =  Array<String>(params.size + 1
            ) { i -> (if (i != 0) params[i - 1] else commandsName.toString()) }

            val json = Json.encodeToString(data)
            return json.toByteArray()
        }

        fun CreatePayload(commandsName: CommandsName, musis: Map<String, UInt>, currentMusicTime: UInt, currentMusicLength: UInt): ByteArray {
            print("here")
            val json = Json.encodeToString(
                object  {
                    val test = "yo"
                }
            )
            /*
            val data = arrayOf(commandsName, params)
            Json.encodeToString(params)
            val json = Json.encodeToString(data)
            val bytes = json.toByteArray()

             */

            return json.toByteArray()
        }
    }
}