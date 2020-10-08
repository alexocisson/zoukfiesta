package ch.hearc.zoukfiesta.nearby.utils
import kotlinx.serialization.*
import kotlinx.serialization.json.Json

class Tools{
    companion object {
        fun CreatePayload(commandsName: CommandsName, datas: Array<Any> = emptyArray()): ByteArray {
            val data = arrayOf(commandsName, datas)
            val json = Json.encodeToString(data)
            val bytes = json.toByteArray()

            return bytes
        }
    }
}