package com.example.vicerichallenge.di

import android.content.Context
import androidx.room.Room
import com.example.vicerichallenge.data.local.UserDao
import com.example.vicerichallenge.data.local.UserDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideUserDatabase(@ApplicationContext context: Context): UserDatabase =
        Room.databaseBuilder(
            context,
            UserDatabase::class.java,
            "users.db"
        ).build()

    @Provides
    fun provideUserDao(db: UserDatabase): UserDao = db.userDao()
}
