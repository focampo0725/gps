package com.kloubit.gps.infrastructure.stateful

import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.kloubit.gps.infrastructure.business.IPayload
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppState @Inject constructor(){
    companion object{

    }
    var isNetworkConnection : Boolean = false
    var location : Location? = null
    var payloadMutableLiveData : MutableLiveData<IPayload> = MutableLiveData()
}