package com.example.hamapp

import android.os.Bundle
import android.widget.Toast
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
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import java.util.Calendar

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

        // 주파수 포맷팅 (FocusChangeListener)
        dialogBinding.etFrequency.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val input = dialogBinding.etFrequency.text.toString()
                if (input.isNotEmpty()) {
                    val num = input.toDoubleOrNull()
                    if (num != null) {
                        if (num in 10.0..9999.999){
                            // 예를 들어 145 입력 시 145.000 으로 변경
                            dialogBinding.etFrequency.setText(
                                String.format(java.util.Locale.US, "%.3f", num)
                            )
                        }

                    }
                }
            }
        }

        // ==== 날짜/시간 선택 이벤트 ====
        dialogBinding.etDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(this, { _, year, month, dayOfMonth ->
                TimePickerDialog(this, { _, hourOfDay, minute ->
                    val formatted = String.format("%04d-%02d-%02d %02d:%02d", year, month + 1, dayOfMonth, hourOfDay, minute)
                    dialogBinding.etDate.setText(formatted)
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show()
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        // 3. 팝업창 안의 '입력' 버튼 클릭 시 이벤트
        dialogBinding.btnSubmit.setOnClickListener {
            val callSign = dialogBinding.etCallSign.text.toString()
            val date = dialogBinding.etDate.text.toString()
            val frequency = dialogBinding.etFrequency.text.toString()

            if(callSign.length != 6){
                Toast.makeText(this, "콜사인을 정확히 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (date.isEmpty()) {
                Toast.makeText(this, "날짜를 선택해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (frequency.isEmpty()) {
                Toast.makeText(this, "주파수를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    val sdf = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault())
                    val dateObj = sdf.parse(date)
                    val newLog = ContactLog(
                        callsign = callSign,
                        dateUtc = dateObj ?: java.util.Date(),
                        frequencyMhz = frequency.ifBlank { "0.000" }
                    )
                    database.contactLogDao().insertLog(newLog)
                    Toast.makeText(this@MainActivity, "로그가 저장되었습니다.", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, "데이터 변환 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

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