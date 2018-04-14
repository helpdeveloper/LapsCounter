package br.com.helpdev.swimlapscounter.chronometer.objects

import java.io.Serializable

class ObLap : Serializable {

    var chronometerTime = 0L

    var startTime = 0L
    var pausedTime = 0L
    var endTime = 0L
}