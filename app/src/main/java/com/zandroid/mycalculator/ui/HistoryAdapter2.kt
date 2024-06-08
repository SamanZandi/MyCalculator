package com.zandroid.mycalculator.ui


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zandroid.mycalculator.databinding.ItemHistoryBinding
import com.zandroid.mycalculator.room.CalcEntity
import javax.inject.Inject

class HistoryAdapter2 @Inject constructor() : RecyclerView.Adapter<HistoryAdapter2.ViewHolder>() {

    private lateinit var binding: ItemHistoryBinding
    private var historyList = emptyList<CalcEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //getItem from PagingDataAdapter
        holder.bind(historyList[position])
        //Not duplicate items
      //  holder.setIsRecyclable(false)
    }

    override fun getItemCount() = historyList.size

    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: CalcEntity) {
            binding.apply {
                txtHistoryCalc.text=item.expression
                txtHistoryResult.text=item.result
                //Click
                root.setOnClickListener {
                    onItemClickListener?.let { it(item) }
                }
            }
        }
    }

    private var onItemClickListener: ((CalcEntity) -> Unit)? = null

    fun setOnItemClickListener(listener: (CalcEntity) -> Unit) {
        onItemClickListener = listener
    }

    fun setData(data: List<CalcEntity>) {
        val historyDiffUtil = HistoryDiffUtils(historyList, data)
        val diffUtils = DiffUtil.calculateDiff(historyDiffUtil)
        historyList = data
        diffUtils.dispatchUpdatesTo(this)
    }

    class HistoryDiffUtils(private val oldItem: List<CalcEntity>, private val newItem: List<CalcEntity>) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldItem.size
        }

        override fun getNewListSize(): Int {
            return newItem.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItem[oldItemPosition] === newItem[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItem[oldItemPosition] === newItem[newItemPosition]
        }
    }
}