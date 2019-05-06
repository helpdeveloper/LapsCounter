package br.com.helpdev.lapscounter.viewmodel

import androidx.lifecycle.ViewModel
import br.com.helpdev.chronometerlib.Chronometer
import br.com.helpdev.lapscounter.model.repository.ActivityRepository
import br.com.helpdev.lapscounter.utils.toActivityEntity
import kotlin.concurrent.thread

class ActivityViewModel(private val activityRepository: ActivityRepository) : ViewModel() {

    var chronometer: Chronometer = Chronometer()

    fun saveActivity(name: String, description: String, lapDistance: Float) {
        thread {
            chronometer.toActivityEntity(name, description, lapDistance).also {
                activityRepository.save(it)
            }
        }
    }
}