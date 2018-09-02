package com.simpleapp.savi.model.suggestion

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

data class NameFrequency(var name: String, var frequency: Int)

interface NameFrequencyAccessor {
    fun grabName() : String
    fun grabFrequency() : Int
    fun updateName(name: String)
    fun updateFrequency(frequency: Int)
}

open class NameFrequency_1(
        @PrimaryKey open var name: String = "",
        open var frequency: Int = 0
) : RealmObject(), NameFrequencyAccessor {
    override fun grabName(): String { return name }
    override fun grabFrequency(): Int { return frequency }
    override fun updateName(name: String) { this.name = name }
    override fun updateFrequency(frequency: Int) { this.frequency = frequency }
}

open class NameFrequency_2(
        @PrimaryKey open var name: String = "",
        open var frequency: Int = 0
) : RealmObject(), NameFrequencyAccessor {
    override fun grabName(): String { return name }
    override fun grabFrequency(): Int { return frequency }
    override fun updateName(name: String) { this.name = name }
    override fun updateFrequency(frequency: Int) { this.frequency = frequency }
}

open class NameFrequency_3(
        @PrimaryKey open var name: String = "",
        open var frequency: Int = 0
) : RealmObject(), NameFrequencyAccessor {
    override fun grabName(): String { return name }
    override fun grabFrequency(): Int { return frequency }
    override fun updateName(name: String) { this.name = name }
    override fun updateFrequency(frequency: Int) { this.frequency = frequency }
}

open class NameFrequency_4(
        @PrimaryKey open var name: String = "",
        open var frequency: Int = 0
) : RealmObject(), NameFrequencyAccessor {
    override fun grabName(): String { return name }
    override fun grabFrequency(): Int { return frequency }
    override fun updateName(name: String) { this.name = name }
    override fun updateFrequency(frequency: Int) { this.frequency = frequency }
}

open class NameFrequency_5(
        @PrimaryKey open var name: String = "",
        open var frequency: Int = 0
) : RealmObject(), NameFrequencyAccessor {
    override fun grabName(): String { return name }
    override fun grabFrequency(): Int { return frequency }
    override fun updateName(name: String) { this.name = name }
    override fun updateFrequency(frequency: Int) { this.frequency = frequency }
}