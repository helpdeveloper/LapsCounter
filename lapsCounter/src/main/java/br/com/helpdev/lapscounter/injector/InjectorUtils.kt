package br.com.helpdev.lapscounter.injector

import br.com.helpdev.lapscounter.model.dao.ActivityDao
import br.com.helpdev.lapscounter.model.repository.ActivityRepository
import br.com.helpdev.lapscounter.ui.viewmodel.ActivityViewModelFactory

object InjectorUtils {

    fun provideActivityViewModelFactory() = ActivityViewModelFactory(provideActivityRepository())

    private fun provideActivityRepository() = ActivityRepository.getInstance(provideActivityDao())

    private fun provideActivityDao() = ActivityDao.getInstance()

}