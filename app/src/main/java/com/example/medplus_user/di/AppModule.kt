package com.example.medplus_user.di

import android.content.Context
import androidx.room.Room
import com.example.medplus_user.common.NetworkChecker
import com.example.medplus_user.data.local.dao.CategoryDao
import com.example.medplus_user.data.local.dao.MedicinesDao
import com.example.medplus_user.data.local.localDatabase.AppDatabase
import com.example.medplus_user.data.remote.FirebaseService
import com.example.medplus_user.data.repository.UserRepositoryImpl
import com.example.medplus_user.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "medplus_database"
        )
            .fallbackToDestructiveMigration(true)
            .build()
    }
    @Provides
    fun provideCategoryDao(db: AppDatabase): CategoryDao {
        return db.CategoryDao()
    }

    @Provides
    fun provideMedicinesDao(db: AppDatabase): MedicinesDao {
        return db.MedicinesDao()
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        networkChecker: NetworkChecker,
        firebaseService: FirebaseService,
        categoryDao: CategoryDao,
        medicinesDao: MedicinesDao
    ): UserRepository {
        return UserRepositoryImpl(networkChecker, firebaseService , categoryDao , medicinesDao )
    }


    @Provides
    @Singleton
    fun provideNetworkChecker(@ApplicationContext context: Context): NetworkChecker {
        return NetworkChecker(context)
    }
}
