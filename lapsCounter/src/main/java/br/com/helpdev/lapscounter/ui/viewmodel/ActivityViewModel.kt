package br.com.helpdev.lapscounter.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import br.com.helpdev.lapscounter.R
import br.com.helpdev.lapscounter.model.entity.ActivityEntity
import br.com.helpdev.lapscounter.model.repository.ActivityRepository
import br.com.helpdev.lapscounter.ui.viewmodel.objects.HeaderDistances
import br.com.helpdev.lapscounter.utils.ChronometerUtils
import br.com.helpdev.lapscounter.utils.getStringLapDistance

class ActivityViewModel(private val activityRepository: ActivityRepository) : ViewModel() {

    var activityEntity: ActivityEntity? = null
    val headerDistances = HeaderDistances()

    fun init(context: Context, activityEntity: ActivityEntity) {
        if (this.activityEntity != null) return
        this.activityEntity = activityEntity
        initHeaderDistances(context, activityEntity)
    }

    private fun initHeaderDistances(context: Context, activityEntity: ActivityEntity) {
        headerDistances.lapDistance.value = getStringLapDistance(context, activityEntity.lapDistance)
        headerDistances.travelledDistance.value = getStringTravelledDistance(context, activityEntity)
        headerDistances.pace.value = getStringPace(context, activityEntity)
    }

    private fun getStringTravelledDistance(context: Context, activityEntity: ActivityEntity) =
        context.getString(R.string.info_travelled, activityEntity.travelledDistance)

    private fun getStringPace(context: Context, activityEntity: ActivityEntity): String {
        val pace = getPace(activityEntity)
        return context.getString(R.string.info_pace, pace.first, pace.second)
    }

    private fun getPace(activityEntity: ActivityEntity) =
        ChronometerUtils.getPace(getRunningTimeToPace(activityEntity), activityEntity.travelledDistance)

    private fun getRunningTimeToPace(activityEntity: ActivityEntity): Long {
        val removedTimeToPace = if (activityEntity.countLastLap) {
            0
        } else
            activityEntity.chronometer!!.laps.last()?.runningTime ?: 0
        return (activityEntity.chronometer!!.runningTime - removedTimeToPace)
    }

    fun deleteActivity() {
        activityEntity?.let { activityRepository.delete(it) }
    }
}