package com.example.teamdecoandroid.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teamdecoandroid.common.SortType
import com.example.teamdecoandroid.data.Coin
import com.example.teamdecoandroid.data.Ticket
import com.example.teamdecoandroid.data.Type
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val startWebSocket: Request,
    private val gson: Gson
) : ViewModel() {

    private val _coinDataList = MutableStateFlow<List<Coin?>>(emptyList())
    val coinDataList = _coinDataList.asStateFlow()

    private var webSocketList: MutableList<WebSocket> = mutableListOf()

    private var _tradePriceSortState = MutableLiveData<SortType>()
    val tradePriceSortState: LiveData<SortType> = _tradePriceSortState

    private var _volume24SortState = MutableLiveData<SortType>()
    val volume24SortState: LiveData<SortType> = _volume24SortState

    fun startListenOrderBook() {
        val request = startWebSocket
        val webSocket = okHttpClient.newWebSocket(request, orderBookListener())
        webSocketList.add(webSocket)
    }

    fun createTicket(): String {
        val ticket = Ticket("ticker")
        val codeList = arrayListOf(
            "KRW-SAND", "KRW-BTC", "KRW-XRP", "KRW-SOL", "KRW-ETH",
            "KRW-SHIB", "KRW-SEI", "KRW-NEAR", "KRW-ID", "KRW-AVAX",
            "KRW-DOGE", "KRW-SUI", "KRW-ETC", "KRW-BTG", "KRW-CTC",
            "KRW-ASTR", "KRW-MINA", "KRW-SC", "KRW-ZRX"
        )
        val type = Type("ticker", codeList)
        return gson.toJson(arrayListOf(ticket, type))
    }

    private fun orderBookListener(): WebSocketListener {
        return object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                webSocket.send(createTicket())
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                super.onMessage(webSocket, bytes)

                val bytesToString = bytes.toByteArray().decodeToString()
                val coin = gson.fromJson(bytesToString, Coin::class.java)

                viewModelScope.launch(Dispatchers.IO) {
                    _coinDataList.update { currentList ->
                        currentList.toMutableList().apply {
                            val index = indexOfFirst { it?.code == coin.code }
                            if (index != -1) {
                                set(index, coin)
                            } else {
                                add(coin)
                            }
                        }
                    }
                }
            }
        }
    }

    fun toggleTradePriceSortState(currentState: SortType) {
        if (currentState == SortType.TRADE_PRICE_ASC) {
            _tradePriceSortState.value = SortType.TRADE_PRICE_DESC
        } else {
            _tradePriceSortState.value = SortType.TRADE_PRICE_ASC
        }
    }

    fun toggleVolume24SortState(currentState: SortType) {
        if (currentState == SortType.VOLUME_24_ASC) {
            _volume24SortState.value = SortType.VOLUME_24_DESC
        } else {
            _volume24SortState.value = SortType.VOLUME_24_ASC
        }
    }

    override fun onCleared() {
        super.onCleared()
        webSocketList.forEach {
            it.close(1000, null)
        }
    }
}