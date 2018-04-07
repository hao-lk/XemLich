package com.example.ac13c.xemlch.local

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.net.Uri
import java.io.Serializable

/**
 * Created by ac13c on 1/17/2018.
 */
@Entity(tableName = "calendar")
class Alarm(
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,
        @ColumnInfo(name = "tieu_de")
        var title: String = "",
        @ColumnInfo(name = "dia_diem")
        var address: String = "",
        @ColumnInfo(name = "noi_dung")
        var content: String = "",
        @ColumnInfo(name = "day")
        var day: Int = 0,
        @ColumnInfo(name = "month")
        var month: Int = 0,
        @ColumnInfo(name = "year")
        var year: Int = 0,
        @ColumnInfo(name = "hour")
        var hour: Int = 0,
        @ColumnInfo(name = "minute")
        var minute: Int = 0,
        @ColumnInfo(name = "tone")
        var tone: String = ""
) : Serializable
