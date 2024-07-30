package com.example.teamdecoandroid.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.teamdecoandroid.common.SortType
import com.example.teamdecoandroid.data.Coin
import com.example.teamdecoandroid.databinding.ItemCoinBinding

class MainAdapter : ListAdapter<Coin, MainAdapter.MyViewHolder>(diffUtil) {

    private var originalList: List<Coin> = listOf()
    private var currentSortType = SortType.VOLUME_24_DESC

    fun submitCoinList(list: List<Coin>) {
        originalList = list
        sortCoinList()
    }

    fun coinNameFilter(query: String) {
        val filteredList = originalList.filter { it.code.contains(query, ignoreCase = true) }
        submitList(filteredList)
    }

    private fun sortCoinList() {
        val sortedList = when (currentSortType) {
            SortType.TRADE_PRICE_ASC -> originalList.sortedBy { it.trade_price }
            SortType.TRADE_PRICE_DESC -> originalList.sortedByDescending { it.trade_price }
            SortType.VOLUME_24_ASC -> originalList.sortedBy { it.acc_trade_price_24h }
            SortType.VOLUME_24_DESC -> originalList.sortedByDescending { it.acc_trade_price_24h }
        }
        submitList(sortedList)
    }

    fun tradePriceSort(sortState: SortType) {
        currentSortType = sortState
        sortCoinList()
    }

    fun tradePrice24Sort(sortState: SortType) {
        currentSortType = sortState
        sortCoinList()
    }

    class MyViewHolder(private val binding: ItemCoinBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Coin) {
            binding.data = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemCoinBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Coin>() {

            override fun areItemsTheSame(oldItem: Coin, newItem: Coin): Boolean {
                return oldItem.code == newItem.code
            }

            override fun areContentsTheSame(oldItem: Coin, newItem: Coin): Boolean {
                return oldItem == newItem
            }
        }
    }
}
