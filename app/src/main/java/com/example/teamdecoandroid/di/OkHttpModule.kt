package com.example.teamdecoandroid.di

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OkHttpModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    @Provides
    @Singleton
    fun startWebSocket(): Request {
        val request = Request.Builder()
            .url("https://api.upbit.com/websocket/v1")
            .build()
        return request
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }
}