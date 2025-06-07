package com.example.medplus_user.di

import android.content.Context
import androidx.room.Room
import com.example.medplus_user.common.NetworkChecker
import com.example.medplus_user.data.local.dao.CategoryDao
import com.example.medplus_user.data.local.dao.MedicinesDao
import com.example.medplus_user.data.local.localDatabase.AppDatabase
import com.example.medplus_user.data.location.AddressResolver
import com.example.medplus_user.data.location.LocationProviderImpl
import com.example.medplus_user.data.remote.FirebaseService
import com.example.medplus_user.data.repository.LocationProvider
import com.example.medplus_user.data.repository.UserRepositoryImpl
import com.example.medplus_user.domain.repository.UserRepository
import com.example.medplus_user.domain.useCases.GetCategoriesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    //Room DB & DAOs
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
    fun provideCategoryDao(db: AppDatabase): CategoryDao = db.CategoryDao()
    @Provides
    fun provideMedicinesDao(db: AppDatabase): MedicinesDao = db.MedicinesDao()
    // NetworkChecker
    @Provides
    @Singleton
    fun provideNetworkChecker(@ApplicationContext context: Context): NetworkChecker {
        return NetworkChecker(context)
    }

    //FirebaseService
    @Provides
    @Singleton
    fun provideFirebaseService(): FirebaseService {
        return FirebaseService() // Assuming it's not already injected
    }

    // 🔹 Repository
    @Provides
    @Singleton
    fun provideUserRepository(
        networkChecker: NetworkChecker,
        firebaseService: FirebaseService,
        categoryDao: CategoryDao,
        medicinesDao: MedicinesDao
    ): UserRepository {
        return UserRepositoryImpl(networkChecker, firebaseService, categoryDao, medicinesDao)
    }

    //UseCase
    @Provides
    @Singleton
    fun provideGetCategoriesUseCase(repository: UserRepository): GetCategoriesUseCase {
        return GetCategoriesUseCase(repository)
    }

    //  Location Provider
    @Provides
    @Singleton
    fun provideLocationProvider(@ApplicationContext context: Context): LocationProvider {
        return LocationProviderImpl(context)
    }

    @Provides
    @Singleton
    fun provideAddressResolver(@ApplicationContext context: Context): AddressResolver {
        return AddressResolver(context)
    }
}
