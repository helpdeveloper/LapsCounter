package br.com.helpdev.swimlapscounter.objects

class ObChronometer(val baseStart: Long) {
    var baseEnd: Long? = null
    private val laps: ArrayList<ObLap> = ArrayList()

    fun addLap(obLap: ObLap) {
        laps.add(obLap)
    }

    fun addPausedTimeToCurrentLap(millis: Long) =
            if (laps.isEmpty()) 0L
            else laps[laps.size - 1].addPausedTime(millis)

}
