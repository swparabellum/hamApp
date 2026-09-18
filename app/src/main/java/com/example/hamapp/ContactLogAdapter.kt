package com.example.hamapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hamapp.databinding.ItemContactLogBinding

class ContactLogAdapter(
    private var logList: List<ContactLog> = emptyList(),
    private val onItemClick: ((ContactLog) -> Unit)? = null
) :
    RecyclerView.Adapter<ContactLogAdapter.LogViewHolder>() {

    inner class LogViewHolder(val binding: ItemContactLogBinding) : RecyclerView.ViewHolder(binding.root) {

        // 뷰홀더가 생성될 때 클릭 리스너 설정
        init {
            binding.root.setOnClickListener {
                val position = getAbsoluteAdapterPosition()
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick?.invoke(logList[position])
                }
            }
        }
        fun bind(log: ContactLog) {
            binding.tvID.text = log.id.toString()
            binding.tvCallsign.text = log.callsign
            binding.tvFrequencyMhz.text = log.frequencyMhz.toString()
            binding.tvDateTime.text = log.dateUtc.toString()
//            binding.tvRst.text = log.rstRcvd
            binding.tvQth.text = log.qth
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        val binding = ItemContactLogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        holder.bind(logList[position])
    }

    override fun getItemCount(): Int = logList.size

    // DB에서 새로운 데이터가 들어오면 어댑터의 리스트를 갱신하는 함수
    fun updateData(newData: List<ContactLog>) {
        this.logList = newData
        notifyDataSetChanged() // 데이터가 바뀌었음을 RecyclerView에 알림
    }
}