package ch.hearc.zoukfiesta.nearby.utils

interface INearbyListener {
    var isListening : Boolean

    public fun start()
    {
        isListening = true
    }

    public fun stop()
    {
        isListening = false
    }

    public fun listening();
}