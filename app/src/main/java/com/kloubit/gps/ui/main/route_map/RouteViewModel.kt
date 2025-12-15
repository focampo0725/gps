package com.kloubit.gps.ui.main.route_map

import androidx.lifecycle.MutableLiveData
import com.kloubit.gps.infrastructure.stateful.AppState
import com.kloubit.gps.ui.BaseViewModel
import com.kloubit.gps.ui.IViewModel
import com.kloubit.gps.ui.LCEState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

sealed class RouteState {

}
@HiltViewModel
class RouteViewModel @Inject constructor(appState: AppState) :
    BaseViewModel<LCEState<RouteState>, RouteState>(appState = appState),
    IViewModel<RouteState> {
    override val renderState: MutableLiveData<LCEState<RouteState>>
        get() = getLiveData()

}