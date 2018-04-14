package br.com.helpdev.swimlapscounter.objects

class ObLap(val baseStart: Long) {

    var baseEnd: Long? = null
    var pausedTime = 0L

    fun addPausedTime(millis: Long) = pausedTime.plus(millis)
}