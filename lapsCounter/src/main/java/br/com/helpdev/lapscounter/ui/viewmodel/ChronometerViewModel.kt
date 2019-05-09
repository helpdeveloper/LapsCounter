package br.com.helpdev.lapscounter.ui.viewmodel

import androidx.lifecycle.ViewModel
import br.com.helpdev.chronometerlib.Chronometer
import br.com.helpdev.lapscounter.model.repository.ActivityRepository
import br.com.helpdev.lapscounter.utils.ChronometerUtils
import br.com.helpdev.lapscounter.utils.toActivityEntity
import kotlin.concurrent.thread

class ChronometerViewModel(private val activityRepository: ActivityRepository) : ViewModel() {

    var chronometer: Chronometer = Chronometer()

    fun saveActivity(name: String, description: String, lapDistance: Float, countLastLap: Boolean) {
        thread {
            chronometer.toActivityEntity(name, description, lapDistance).also {
                it.countLastLap = countLastLap
                it.travelledDistance = ChronometerUtils.getDistanceTravelled(chronometer, lapDistance, countLastLap)
                activityRepository.save(it)
            }
        }
    }
}