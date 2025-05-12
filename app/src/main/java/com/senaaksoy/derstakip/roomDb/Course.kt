package com.senaaksoy.derstakip.roomDb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "courses")
data class Course(
    @PrimaryKey(autoGenerate = true)
    val id : Int=0,
    @ColumnInfo(name = "Name")
    val name : String
)