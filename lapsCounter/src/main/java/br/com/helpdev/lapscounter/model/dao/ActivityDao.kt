package br.com.helpdev.lapscounter.model.dao

import br.com.helpdev.lapscounter.model.RealmLiveData
import br.com.helpdev.lapscounter.model.asLiveData
import br.com.helpdev.lapscounter.model.entity.ActivityEntity
import io.realm.Realm
import io.realm.Sort

class ActivityDao {

    companion object {
        @Volatile
        private var instance: ActivityDao? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: ActivityDao().also { instance = it }
        }
    }

    fun insertOrUpdate(activityEntity: ActivityEntity) {
        Realm.getDefaultInstance()
            .executeTransactionAsync { realm ->
                realm.insertOrUpdate(activityEntity)
            }
    }

    fun selectAll(): RealmLiveData<ActivityEntity> {
        return Realm.getDefaultInstance()
            .where(ActivityEntity::class.java)
            .sort("dateStarted", Sort.DESCENDING)
            .findAllAsync()
            .asLiveData()
    }

    fun delete(activityEntity: ActivityEntity) {
        Realm.getDefaultInstance().use { db ->
            db.executeTransaction { realm ->
                realm.where(ActivityEntity::class.java)
                    .equalTo("id", activityEntity.id)
                    .findFirst()?.deleteFromRealm()
            }
        }
    }

    fun select(activityEntityId: String): ActivityEntity {
        return Realm.getDefaultInstance()
            .where(ActivityEntity::class.java)
            .equalTo("id", activityEntityId)
            .findFirst()!!
    }
}