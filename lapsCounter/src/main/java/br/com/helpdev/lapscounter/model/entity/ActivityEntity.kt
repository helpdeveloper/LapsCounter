package br.com.helpdev.lapscounter.model.entity

import io.realm.RealmObject
import java.util.*

open class ActivityEntity(
        var name: String = "empty",
        var description: String? = null,
        var chronometer: ChronometerEntity? = ChronometerEntity(),
        var lapDistance: Float = 0.0f,
        var dateStarted: Date? = null
) : RealmObject()