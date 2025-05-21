package com.example.vicerichallenge.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.example.vicerichallenge.data.local.UserDao
import com.example.vicerichallenge.data.remote.api.UserApi
import com.example.vicerichallenge.data.repository.UserRepositoryImpl
import com.example.vicerichallenge.domain.repository.UserRepository
import com.example.vicerichallenge.domain.usecase.GetUserByIdUseCase
import com.example.vicerichallenge.domain.usecase.GetUsersUseCase
import com.example.vicerichallenge.domain.usecase.SearchUsersUseCase

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(
        api: UserApi,
        dao: UserDao
    ): UserRepository = UserRepositoryImpl(api, dao)

    @Provides
    fun provideGetUsersUseCase(repository: UserRepository): GetUsersUseCase =
        GetUsersUseCase(repository)

    @Provides
    fun provideGetUserByIdUseCase(repository: UserRepository): GetUserByIdUseCase =
        GetUserByIdUseCase(repository)

    @Provides
    fun provideSearchUsersUseCase(repository: UserRepository): SearchUsersUseCase =
        SearchUsersUseCase(repository)
}
