package com.kloubit.gps.di.module


import android.content.Context
import com.kloubit.gps.data.repository.AppRepository
import com.kloubit.gps.infrastructure.clients.ClientMQTT
import com.kloubit.gps.infrastructure.clients.ClientObserver
import com.kloubit.gps.infrastructure.stateful.AppState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
class ServiceModule @Inject constructor(){


    @Provides
    fun providesClientMQTT(
        @ApplicationContext context: Context, appState: AppState, appRepository: AppRepository
    ) : ClientMQTT = ClientMQTT(appState, context, appRepository)

    @Provides
    fun provideClientObservables(@ApplicationContext context: Context, appState: AppState, appRepository: AppRepository
    ) : ClientObserver =
        ClientObserver(context,appState,appRepository)




}