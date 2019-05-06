package br.com.helpdev.lapscounter.injector

import br.com.helpdev.lapscounter.model.dao.ActivityDao
import br.com.helpdev.lapscounter.model.repository.ActivityRepository
import br.com.helpdev.lapscounter.viewmodel.ActivityViewModelFactory

object ActivityInjector {

    fun provideActivityViewModelFactory() = ActivityViewModelFactory(provideActivityRepository())

    private fun provideActivityRepository() = ActivityRepository.getInstance(provideActivityDao())

    private fun provideActivityDao() = ActivityDao.getInstance()

}