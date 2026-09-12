package com.example.hamapp

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "contact_logs")
data class ContactLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val callsign: String,          // 상대방 호출부호 (예: HL1XYZ)
    val dateUtc: Date,           // 교신 날짜 (YYYY-MM-DD, UTC 기준)
//    val timeOnUtc: String,         // 교신 시작 시간 (HH:MM, UTC 기준)
    val frequencyMhz: Double,      // 주파수 (예: 14.250)
//    val band: String,              // 밴드 (예: 20m, 2m)
//    val mode: String,              // 통신 모드 (예: SSB, CW, FT8)
    val rstSent: String? = null,           // 보낸 신호 리포트 (예: 59, 599)
    val rstRcvd: String? = null,           // 받은 신호 리포트 (예: 59, 599)
    val qth: String? = null,       // 상대방 위치 (도시 또는 지역명)
//    val gridSquare: String? = null,// 그리드 스퀘어 (예: PM37lg)
    val notes: String? = null      // 메모 사항
)