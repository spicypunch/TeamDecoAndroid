package com.example.teamdecoandroid.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.teamdecoandroid.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: MainViewModel by viewModels()
    private val adapter by lazy { MainAdapter() }

    

    private var query = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.startListenOrderBook()

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

        }

        binding.linearlayoutTradeSort.setOnClickListener {

        }

        binding.linearlayoutTrade24Sort.setOnClickListener {

        }

        binding.tvHeaderTradePrice24.setOnClickListener {

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
}