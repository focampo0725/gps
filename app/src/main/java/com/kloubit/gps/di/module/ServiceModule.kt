package com.kloubit.gps.di.module


import android.content.Context
import com.kloubit.gps.data.repository.AppRepository
import com.kloubit.gps.domain.utils.ControlIdentifierUtils
import com.kloubit.gps.infrastructure.business.ControlProcessorImpl
import com.kloubit.gps.infrastructure.business.IControlProcessor
import com.kloubit.gps.infrastructure.business.ITrackProcessor
import com.kloubit.gps.infrastructure.business.TrackProcessorImpl
import com.kloubit.gps.infrastructure.clients.ClientLocation
import com.kloubit.gps.infrastructure.clients.ClientMQTT
import com.kloubit.gps.infrastructure.clients.ClientObserver
import com.kloubit.gps.infrastructure.clients.ClientThread
import com.kloubit.gps.infrastructure.stateful.AppState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

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

    @Provides
    fun providesClientThread(
        @ApplicationContext context: Context, appRepository: AppRepository,  appState: AppState
    ) : ClientThread = ClientThread(context, appRepository, appState)

    @Provides
    @Singleton
    fun providesClientLocation(@ApplicationContext context: Context, appState: AppState, appRepository: AppRepository,
                               iTrackProcessor: ITrackProcessor, iControlProcessor: IControlProcessor
    ) : ClientLocation = ClientLocation(context, appRepository, appState, iTrackProcessor, iControlProcessor)

    @Provides
    @Singleton
    fun provideProcessTrack(appState: AppState, appRepository: AppRepository, @ApplicationContext context: Context) : ITrackProcessor
            = TrackProcessorImpl(appState = appState, appRepository = appRepository, context = context)

    @Provides
    @Singleton
    fun provideControlProcessor(appState: AppState,  appRepository: AppRepository, controlIdentifierUtils : ControlIdentifierUtils, @ApplicationContext context: Context) : IControlProcessor
            = ControlProcessorImpl(appState = appState, appRepository = appRepository, context = context, controlIdentifierUtils = controlIdentifierUtils)

    @Provides
    @Singleton
    fun provideControlIdentifierUtils() : ControlIdentifierUtils = ControlIdentifierUtils()




}