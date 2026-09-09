package com.example.hamapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hamapp.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ContactLogAdapter
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initrvContactLog()
    }

    private fun initrvContactLog() {
        // 1. 어댑터 초기화 및 리사이클러뷰 연결
        adapter = ContactLogAdapter()
        binding.rvContactLog.adapter = adapter
        binding.rvContactLog.layoutManager = LinearLayoutManager(this)

        // 2. Room 데이터베이스 인스턴스 초기화
        database = AppDatabase.getDatabase(this)

        // 3. DB에서 데이터 관찰 및 UI 갱신 (비동기 처리)
        lifecycleScope.launch {
            database.contactLogDao().getAllLogs().collect { logList ->
                adapter.updateData(logList)
            }
        }
    }
}