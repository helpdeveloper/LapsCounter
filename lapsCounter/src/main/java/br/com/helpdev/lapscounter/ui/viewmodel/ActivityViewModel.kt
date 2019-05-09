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

    lateinit var activityEntity: ActivityEntity
    val headerDistances = HeaderDistances()

    fun init(context: Context, activityEntity: ActivityEntity) {
        this.activityEntity = activityEntity
        headerDistances.lapDistance.value = getStringLapDistance(context, activityEntity.lapDistance)
        headerDistances.travelledDistance.value =
            context.getString(R.string.info_travelled, activityEntity.travelledDistance)
        val pace = ChronometerUtils.getPace(activityEntity.chronometer!!.runningTime, activityEntity.travelledDistance)
        headerDistances.pace.value = context.getString(R.string.info_pace, pace.first, pace.second)
    }

    fun deleteActivity() {
        activityRepository.delete(activityEntity)
    }
}