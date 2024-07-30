package com.example.teamdecoandroid.ui

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.teamdecoandroid.common.SortType
import com.example.teamdecoandroid.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: MainViewModel by viewModels()
    private val adapter by lazy { MainAdapter() }

    private var tradePriceSortState = SortType.TRADE_PRICE_ASC
    private var volume24SortState = SortType.VOLUME_24_ASC
    private var query = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.startListenOrderBook()

        viewModel.tradePriceSortState.observe(this) {
            tradePriceSortState = it
        }

        viewModel.volume24SortState.observe(this) {
            volume24SortState = it
        }

        lifecycleScope.launch {
            viewModel.coinDataList.collect { coin ->
                if (query.isEmpty()) {
                    adapter.submitCoinList(
                        coin.filterNotNull()
                    )
                } else {
                    adapter.submitCoinList(
                        coin.filterNotNull().filter {
                            it.code.contains(
                                query,
                                ignoreCase = true
                            )
                        }
                    )
                }
            }
        }

        binding.tvHeaderTradePrice.setOnClickListener {
            adapter.tradePriceSort(tradePriceSortState)
            changeArrowColor(tradePriceSortState)
            viewModel.toggleTradePriceSortState(tradePriceSortState)
        }

        binding.tvHeaderTradePrice24.setOnClickListener {
            adapter.tradePrice24Sort(volume24SortState)
            changeArrowColor(volume24SortState)
            viewModel.toggleVolume24SortState(volume24SortState)
        }

        binding.btnSearch.setOnClickListener {
            if (binding.editSearch.text.toString().isBlank()) {
                query = ""
                Toast.makeText(this, "전체 코인을 검색합니다.", Toast.LENGTH_SHORT).show()
            } else {
                query = binding.editSearch.text.toString()
                adapter.coinNameFilter(query)
            }
        }
    }

    private fun changeArrowColor(sortType: SortType) {

        // 모든 아이콘 검정색으로
        listOf(
            binding.iconTradeAsc,
            binding.iconTradeDesc,
            binding.iconVolume24Asc,
            binding.iconVolume24Desc
        ).forEach { it.setColorFilter(Color.BLACK) }

        val iconToHighlight = when (sortType) {
            SortType.TRADE_PRICE_ASC -> binding.iconTradeAsc
            SortType.TRADE_PRICE_DESC -> binding.iconTradeDesc
            SortType.VOLUME_24_ASC -> binding.iconVolume24Asc
            SortType.VOLUME_24_DESC -> binding.iconVolume24Desc
        }

        iconToHighlight.setColorFilter(Color.WHITE)
    }
}