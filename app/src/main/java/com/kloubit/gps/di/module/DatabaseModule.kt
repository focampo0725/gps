package com.kloubit.gps.di.module

import android.content.Context
import android.content.SharedPreferences
import com.kloubit.gps.data.room.AppDB
import com.kloubit.gps.data.room.dao.CompanyDao
import com.kloubit.gps.data.spf.ISharedPreferences
import com.kloubit.gps.data.spf.SharedPreferencesImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule @Inject constructor() {

    // region room database and dao_list
    @Provides
    @Singleton
    fun provideAppDBDatabase(@ApplicationContext context: Context): AppDB =
        AppDB.getInstance(context)!!


    @Provides @Singleton
    fun provideCompanyDao(appDB : AppDB) : CompanyDao = appDB.companyDao()

    @Provides
    @Singleton
    fun provideSPF(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideISPF(
        @ApplicationContext context: Context,
        sharedPreferences: SharedPreferences
    ): ISharedPreferences = SharedPreferencesImpl(context = context, spf = sharedPreferences)



}