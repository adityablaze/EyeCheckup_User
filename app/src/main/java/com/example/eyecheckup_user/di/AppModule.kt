package com.example.eyecheckup_user.di

import android.content.Context
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.room.Room
import com.example.eyecheckup_user.repository.AppointmentRepository
import com.example.eyecheckup_user.repository.FireRepository
import com.google.common.util.concurrent.ListenableFuture
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideFireBookRepository()
            = FireRepository(queryTrack = FirebaseFirestore.getInstance()
        .collection("clients"))

    @Singleton
    @Provides
    fun provideAppointmentRepository()
            = AppointmentRepository(queryTrack = FirebaseFirestore.getInstance()
        .collection("appointments"))
}

