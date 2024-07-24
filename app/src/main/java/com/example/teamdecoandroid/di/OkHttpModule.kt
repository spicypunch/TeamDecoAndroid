package com.example.teamdecoandroid.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket

@Module
@InstallIn(SingletonComponent::class)
object OkHttpModule {
    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null

    fun start() {
        val request = Request.Builder()
            .url("https://api.upbit.com/websocket/v1")
            .build()
        val okHttpClient = OkHttpClient()
    }

}