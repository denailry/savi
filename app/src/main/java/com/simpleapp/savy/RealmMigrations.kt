package com.simpleapp.savy

import io.realm.*


class RealmMigrations : RealmMigration {
    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
//        val schema = realm.schema
//        if (oldVersion == 1L) {
//            val userSchema = schema.get("Record")
//            userSchema!!.addField("id", Long::class.java, FieldAttribute.PRIMARY_KEY)
//            userSchema.addField("datestamp", Int::class.java)
//            userSchema.addField("value", Int::class.java)
//            userSchema.addField("notes", String::class.java)
//            userSchema.addField("type", Byte::class.java)
//        }
    }
}