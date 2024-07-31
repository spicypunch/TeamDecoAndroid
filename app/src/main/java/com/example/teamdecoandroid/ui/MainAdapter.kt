package com.example.teamdecoandroid.ui

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
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

    private val previousPrices = mutableMapOf<String, Double>()

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

    inner class MyViewHolder(private val binding: ItemCoinBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Coin, previousPrice: Double?) {
            binding.data = item

            val backgroundColor = when {
                previousPrice == null -> Color.TRANSPARENT
                item.trade_price > previousPrice -> Color.parseColor("#D32F2F")
                item.trade_price < previousPrice -> Color.parseColor("#1E88E5")
                else -> Color.TRANSPARENT
            }

            if (previousPrice != null && item.trade_price != previousPrice) {
                animateBackgroundColor(backgroundColor)
            }

            previousPrices[item.code] = item.trade_price
        }

        private fun animateBackgroundColor(color: Int) {
            val baseColor = Color.TRANSPARENT
            val animator = ValueAnimator.ofObject(ArgbEvaluator(), baseColor, color)
            animator.duration = 500
            animator.addUpdateListener { animator ->
                binding.root.setBackgroundColor(animator.animatedValue as Int)
            }
            animator.start()

            val resetAnimator = ValueAnimator.ofObject(ArgbEvaluator(), color, baseColor)
            resetAnimator.duration = 500
            resetAnimator.startDelay = 500
            resetAnimator.addUpdateListener { animator ->
                binding.root.setBackgroundColor(animator.animatedValue as Int)
            }
            resetAnimator.start()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemCoinBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val coin = currentList[position]
        val previousPrice = previousPrices[coin.code]
        holder.bind(coin, previousPrice)
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
