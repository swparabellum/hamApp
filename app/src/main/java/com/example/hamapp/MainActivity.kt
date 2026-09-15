package com.example.hamapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hamapp.databinding.ActivityMainBinding
import com.example.hamapp.databinding.AddContactLogBinding
import kotlinx.coroutines.launch
import androidx.appcompat.app.AlertDialog

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
        // ==== btnAddLog 버튼 클릭 이벤트 추가 ====
        binding.btnAddLog.setOnClickListener {
            showAddLogDialog()
        }
    }

    // ==== 다이얼로그 띄우기 함수 ====
    private fun showAddLogDialog() {
        // 1. 다이얼로그용 화면(add_contact_log.xml) 뷰바인딩 객체 생성
        val dialogBinding = AddContactLogBinding.inflate(layoutInflater)

        // 2. AlertDialog 생성
        val dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .create()

        // 3. 팝업창 안의 '입력' 버튼 클릭 시 이벤트
        dialogBinding.btnSubmit.setOnClickListener {
            val callSign = dialogBinding.etCallSign.text.toString()
            val date = dialogBinding.etDate.text.toString()
            val frequency = dialogBinding.etFrequency.text.toString()

            // TODO: 입력받은 데이터를 Room DB에 저장하는 코드 작성
            // ...

            dialog.dismiss() // 저장 후 팝업 닫기
        }

        // 4. 팝업창 안의 '취소' 버튼 클릭 시 이벤트
        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss() // 팝업 닫기
        }

        // 5. 화면에 띄우기
        dialog.show()
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