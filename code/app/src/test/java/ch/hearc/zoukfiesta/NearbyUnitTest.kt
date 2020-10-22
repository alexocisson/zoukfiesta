package ch.hearc.zoukfiesta

import ch.hearc.zoukfiesta.nearby.utils.NearbyClient
import ch.hearc.zoukfiesta.nearby.utils.NearbyServer
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class NearbyUnitTest {

    //Create a server
    private val server = NearbyServer()

    //Create a client
    private val client = NearbyClient()

    init {
        //Start both listener
        server.listening()
        client.listening()
    }

    //Do a test for each command
    @Test
    fun skip() {
        val expected = "Konnis Hupen"

        //Send the msg
        client.sendSkip(expected)

        //Receive the message
        server.onSkip { musicName : String -> assertEquals(expected, musicName)}
    }
}