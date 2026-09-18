package com.example.hamapp

import android.R
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactLogDao {

    @Insert
    suspend fun insertLog(contactLog: ContactLog)


    // 모든 로그를 가져오기. Flow를 쓰면 DB 변경 시 자동으로 UI가 업데이트 됨
    @Query("SELECT * FROM contact_logs ORDER BY id DESC")
    fun getAllLogs(): Flow<List<ContactLog>>


//    @Query("UPDATE contact_logs SET isUse = 'false' WHERE id = :userID")
//    suspend fun deleteLog(userID: Long)

    @Query("DELETE FROM contact_logs WHERE id = :userID")
    suspend fun deleteLog(userID: Long)
}