package br.com.helpdev.lapscounter.ui.viewmodel

import androidx.lifecycle.ViewModel
import br.com.helpdev.lapscounter.model.RealmLiveData
import br.com.helpdev.lapscounter.model.entity.ActivityEntity
import br.com.helpdev.lapscounter.model.repository.ActivityRepository

class ListActivityViewModel(private val activityRepository: ActivityRepository) : ViewModel() {

    val activities: RealmLiveData<ActivityEntity> by lazy { activityRepository.getAllActivities() }

}