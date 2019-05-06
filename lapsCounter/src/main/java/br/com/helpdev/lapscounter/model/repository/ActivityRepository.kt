package br.com.helpdev.lapscounter.model.repository

import br.com.helpdev.lapscounter.model.dao.ActivityDao
import br.com.helpdev.lapscounter.model.entity.ActivityEntity

class ActivityRepository(private val activityDao: ActivityDao) {

    companion object {
        @Volatile
        private var instance: ActivityRepository? = null

        fun getInstance(activityDao: ActivityDao) = instance ?: synchronized(this) {
            instance ?: ActivityRepository(activityDao).also { instance = it }
        }
    }

    fun save(activityEntity: ActivityEntity) {
        activityDao.insertOrUpdate(activityEntity)
    }
}