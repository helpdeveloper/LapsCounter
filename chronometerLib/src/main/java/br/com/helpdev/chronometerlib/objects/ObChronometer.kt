package br.com.helpdev.chronometerlib.objects

import java.io.Serializable

class ObChronometer : Serializable {

    private var pausedTime = 0L
    private var startTime = 0L
    private var endTime = 0L

    val laps: ArrayList<ObLap> = ArrayList()

    init {
        laps.add(ObLap())
    }

    fun setStartTime(startTime: Long) {
        this.startTime = startTime
        laps.last().startTime = this.startTime
    }

    fun addPausedTime(pausedTime: Long) {
        this.pausedTime = this.pausedTime.plus(pausedTime)
        laps.last().pausedTime = laps.last().pausedTime.plus(pausedTime)
    }

    fun setEndTime(endTime: Long) {
        this.endTime = endTime
        laps.last().endTime = endTime
    }

    fun newLap(currentTime: Long): Boolean {
        laps.last().endTime = currentTime
        val lap = ObLap()
        lap.startTime = currentTime
        lap.chronometerTime = (currentTime - startTime) - pausedTime
        return laps.add(lap)
    }

}
