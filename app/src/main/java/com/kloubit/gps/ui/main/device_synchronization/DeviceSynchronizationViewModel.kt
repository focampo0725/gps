package com.kloubit.gps.ui.main.device_synchronization

import androidx.lifecycle.MutableLiveData
import com.kloubit.gps.infrastructure.stateful.AppState
import com.kloubit.gps.ui.BaseViewModel
import com.kloubit.gps.ui.IViewModel
import com.kloubit.gps.ui.LCEState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

sealed class DeviceSynchronizationState{

}

@HiltViewModel
class DeviceSynchronizationViewModel @Inject constructor(appState: AppState) : BaseViewModel<LCEState<DeviceSynchronizationState>, DeviceSynchronizationState>(appState = appState), IViewModel<DeviceSynchronizationState>{
    override val renderState: MutableLiveData<LCEState<DeviceSynchronizationState>>
        get() = getLiveData()

}