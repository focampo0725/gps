package com.kloubit.gps.di.module

import com.kloubit.gps.infrastructure.receivers.LocationReceiver
import com.kloubit.gps.infrastructure.receivers.PhoneCallReceiver
import com.kloubit.gps.infrastructure.receivers.SMSReceiver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject


@Module
@InstallIn(SingletonComponent::class)
class BroadcastReceiverModule @Inject constructor(){

    
    @Provides
    fun providePhoneCallReceiver() : PhoneCallReceiver = PhoneCallReceiver()

    @Provides
    fun provideSMSReceiver() : SMSReceiver = SMSReceiver()

    @Provides
    fun provideLocationReceiver() : LocationReceiver = LocationReceiver()

}