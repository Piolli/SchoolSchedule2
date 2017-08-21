package com.example.alexandr.schooltime.schedule

import android.util.Log

import io.realm.RealmModel
import io.realm.RealmObject

/**
 * Created by Alexandr on 02.04.2017.
 */

open class Lesson : RealmObject {
    var name: String? = null
    var office: String? = null
    var time: String? = null

    constructor()

    constructor(name: String, office: String, time: String) {
        this.name = name
        this.office = office
        this.time = time
    }

    override fun toString(): String {
        return "Lesson(name=$name, office=$office, time=$time)"
    }


}
