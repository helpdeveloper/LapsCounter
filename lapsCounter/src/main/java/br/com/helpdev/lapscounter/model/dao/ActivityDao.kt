package br.com.helpdev.lapscounter.model.dao

import br.com.helpdev.lapscounter.model.RealmLiveData
import br.com.helpdev.lapscounter.model.asLiveData
import br.com.helpdev.lapscounter.model.entity.ActivityEntity
import io.realm.Realm

class ActivityDao {

    companion object {
        @Volatile
        private var instance: ActivityDao? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: ActivityDao().also { instance = it }
        }
    }

    fun insertOrUpdate(activityEntity: ActivityEntity) {
        Realm.getDefaultInstance().use { db ->
            db.executeTransaction { realm ->
                realm.insertOrUpdate(activityEntity)
            }
        }
    }

    fun selectAll(): RealmLiveData<ActivityEntity> {
        return Realm.getDefaultInstance()
                .where(ActivityEntity::class.java)
                .findAllAsync()
                .asLiveData()
    }
}