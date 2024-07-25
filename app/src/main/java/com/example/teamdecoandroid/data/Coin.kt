package com.example.teamdecoandroid.data

data class Coin(
    val ask_bid: String,
    val change: String,
    val change_price: Double,
    val code: String,
    val prev_closing_price: Double,
    val sequential_id: Long,
    val stream_type: String,
    val timestamp: Long,
    val trade_date: String,
    val trade_price: Double,
    val trade_time: String,
    val trade_timestamp: Long,
    val trade_volume: Double,
    val type: String
)