package br.com.helpdev.lapscounter.model.entity

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.io.Serializable
import java.util.*

open class ActivityEntity(
        @PrimaryKey @Required var id: String = UUID.randomUUID().toString(),
        var name: String = "empty",
        var description: String? = null,
        var chronometer: ChronometerEntity? = ChronometerEntity(),
        var lapDistance: Float = 0.0f,
        var dateStarted: Date? = null
) : RealmObject(), Serializable {
    override fun toString(): String {
        return "ActivityEntity(id='$id', name='$name', description=$description, chronometer=$chronometer, lapDistance=$lapDistance, dateStarted=$dateStarted)"
    }
}