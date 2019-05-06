package br.com.helpdev.lapscounter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.helpdev.lapscounter.model.repository.ActivityRepository

class ActivityViewModelFactory(private val activityRepository: ActivityRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ActivityViewModel(activityRepository) as T
    }
}