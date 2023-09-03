package com.youcancook.gobong.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.youcancook.gobong.model.datasource.GoBongRemoteDataSource
import com.youcancook.gobong.model.datasource.UserDataSource
import com.youcancook.gobong.model.network.GoBongService
import com.youcancook.gobong.model.network.ImageService
import com.youcancook.gobong.model.network.UserService
import com.youcancook.gobong.model.repository.GoBongRepositoryImpl
import com.youcancook.gobong.model.repository.UserRepositoryImpl
import com.youcancook.gobong.util.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GoBongContainer {

    private val gson: Gson = GsonBuilder()
        .setLenient()
        .create()

    private val goBongRetrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl(BASE_URL)
        .build()
        .create(GoBongService::class.java)

    private val userRetrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl(BASE_URL)
        .build()
        .create(UserService::class.java)

    private val imageRetrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl(BASE_URL)
        .build()
        .create(ImageService::class.java)

    private val remoteGoBongDataSource = GoBongRemoteDataSource(goBongRetrofit,userRetrofit,imageRetrofit)
    private val remoteUserDataSource = UserDataSource(userRetrofit,imageRetrofit)

    val goBongRepository = GoBongRepositoryImpl(remoteGoBongDataSource)
    val userRepository = UserRepositoryImpl(remoteUserDataSource)
}