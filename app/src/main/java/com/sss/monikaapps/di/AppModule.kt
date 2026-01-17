package com.sss.monikaapps.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.google.android.gms.location.LocationServices
import com.sss.monikaapps.network.ApiConfig
import com.sss.monikaapps.feature.activity.data.repository.ActivitiesRepository
import com.sss.monikaapps.feature.activity.data.repository.ActivitiesRepositoryImpl
import com.sss.monikaapps.feature.login.data.repository.AuthRepository
import com.sss.monikaapps.feature.login.data.repository.AuthRepositoryImpl
import com.sss.monikaapps.feature.expanse.data.repository.ExpansesRepository
import com.sss.monikaapps.feature.expanse.data.repository.ExpansesRepositoryImpl
import com.sss.monikaapps.common.repository.location.LocationRepository
import com.sss.monikaapps.common.repository.location.LocationRepositoryImpl
import com.sss.monikaapps.common.repository.photo.PhotoRepository
import com.sss.monikaapps.common.repository.photo.PhotoRepositoryImpl
import com.sss.monikaapps.database.AppDatabase
import com.sss.monikaapps.database.AppDatabaseManager
import com.sss.monikaapps.feature.activity.presentation.ActivitiesViewModel
import com.sss.monikaapps.feature.login.presentation.AuthViewModel
import com.sss.monikaapps.feature.expanse.presentation.ExpansesViewModel
import com.sss.monikaapps.feature.home.presentasi.HomeViewModel
import com.sss.monikaapps.common.viewmodel.LocationViewModel
import com.sss.monikaapps.common.viewmodel.PhotoViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object AppModule {
    val apiModule = module {
        single { ApiConfig.getApiService() }
    }

    val databaseModule = module {
        single {
            Room.databaseBuilder(androidContext(), AppDatabase::class.java, "app_database")
                .setJournalMode(RoomDatabase.JournalMode.TRUNCATE).fallbackToDestructiveMigration()
                .build().also { AppDatabaseManager.setDatabase(it) }
        }
        single { get<AppDatabase>().photoDao() }
        single { get<AppDatabase>().activityDao() }
    }

    val repositoryModule = module {

        single {
            LocationServices.getFusedLocationProviderClient(androidContext())
        }

        single<AuthRepository> { AuthRepositoryImpl(get()) }
        single<ActivitiesRepository> {
            ActivitiesRepositoryImpl(
                androidApplication(), get(), get(), get()
            )
        }
        single<ExpansesRepository> { ExpansesRepositoryImpl(get()) }
        single<PhotoRepository> { PhotoRepositoryImpl(get()) }
        single<LocationRepository> { LocationRepositoryImpl(get()) }
    }


    val viewModelModule = module {
        viewModel { AuthViewModel(get()) }
        viewModel { ActivitiesViewModel(get()) }
        viewModel { ExpansesViewModel(get()) }
        viewModel { HomeViewModel() }
        viewModel { PhotoViewModel(get()) }
        viewModel { LocationViewModel(get()) }
    }
}