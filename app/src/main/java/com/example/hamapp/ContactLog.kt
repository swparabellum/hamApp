package com.example.hamapp

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "contact_logs")
data class ContactLog (
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // 고유 ID 자동 생성
    val callsign: String,
    val bandMode: String,
    val dateTime: String,
    val rst: String,
    val qth: String
)