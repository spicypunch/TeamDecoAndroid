package com.example.teamdecoandroid.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.teamdecoandroid.data.Coin
import com.example.teamdecoandroid.databinding.ItemCoinBinding

class MainAdapter : ListAdapter<Coin, MainAdapter.MyViewHolder>(diffUtil) {

    private var originalList: List<Coin> = listOf()

    fun submitCoinList(list: List<Coin>) {
        originalList = list
        submitList(originalList)
    }

    fun coinNameFilter(query: String) {
        val filteredList = originalList.filter { it.code.contains(query, ignoreCase = true)}
        submitList(filteredList)
    }

    class MyViewHolder(private val binding: ItemCoinBinding) : RecyclerView.ViewHolder(binding.root) {
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
