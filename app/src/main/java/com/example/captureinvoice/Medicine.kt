package com.example.captureinvoice

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Medicine(@PrimaryKey @ColumnInfo(name="string") val string: String)
