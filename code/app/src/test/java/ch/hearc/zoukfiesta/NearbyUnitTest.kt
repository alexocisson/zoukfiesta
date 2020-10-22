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

    //Do a test for each command of the protocol (See documentation/protocoles/ZoukFiestProtocole.docx)

    @Test
    fun skip() {
        val expected = "Konnis Hupen"

        //Send the msg
        client.sendSkip(expected)

        //Receive the message
        server.onSkip { musicName : String -> assertEquals(expected, musicName)}
    }

    @Test
    fun what() {
        //Send the msg
        client.sendWhat()

        //Receive the message
        server.onWhat { assertTrue(true) }
    }

    @Test
    fun musics() {
        //Send the msg
        client.sendMusics()

        //Receive the message
        server.onMusics { assertTrue(true) }
    }

    @Test
    fun add() {
        val expected = "Konnis Hupen"

        //Send the msg
        client.sendAdd(expected)

        //Receive the message
        server.onAdd { musicName -> assertEquals(expected, musicName) }
    }

    @Test
    fun playlist() {
        val konnisHupen = "Konnis Hupen"
        val expectedVotes = mapOf(konnisHupen to 3u, "L'amour toujours" to 7u, "Le cactus" to 0u)
        val expetedCurrentMusic = konnisHupen
        val expectedTimestamp = 120u
        val expectedDuration = 200u

        //Send the msg
        server.sendPlaylist(expectedVotes, expectedTimestamp, expectedDuration)

        //Receive the message
        client.onPlaylist { votes, currentMusicTime, currentMusicLength ->
            assertTrue(expectedVotes.equals(votes))
            assertEquals(currentMusicTime, expectedTimestamp)
            assertEquals(expectedDuration, currentMusicLength)

            //Is the current music the right one ?
            assertEquals(votes.values.first(), konnisHupen)
        }
    }

    @Test
    fun available() {
        val expectedMusics = arrayOf("Konnis Huppen", "L'amour toujours", "Le cactus")

        //Send the msg
        server.sendAvailable(expectedMusics)

        //Receive the message
        client.onAvailable { musics ->  assertArrayEquals(expectedMusics, musics) }
    }

    @Test
    fun kick() {
        //Send the msg
        server.sendKick()

        //Receive the message
        client.onKick { assertTrue(true) }
    }
}