package com.example.hamapp

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactLogDao {

    @Insert
    suspend fun insertLog(contactLog: ContactLog)


    // 모든 로그를 가져오기. Flow를 쓰면 DB 변경 시 자동으로 UI가 업데이트 됨
    @Query("SELECT * FROM contact_logs ORDER BY id DESC")
    fun getAllLogs(): Flow<List<ContactLog>>

}