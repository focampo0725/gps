package com.kloubit.gps.ui.main.session

import androidx.lifecycle.MutableLiveData
import com.kloubit.gps.domain.dto.RouteDataDTO
import com.kloubit.gps.infrastructure.stateful.AppState
import com.kloubit.gps.ui.BaseViewModel
import com.kloubit.gps.ui.IViewModel
import com.kloubit.gps.ui.LCEState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

sealed class SessionState {
    class RouteLoaded(val routeDataList: List<RouteDataDTO>) : SessionState()
}

@HiltViewModel
class SessionViewModel @Inject constructor(appState: AppState) :
    BaseViewModel<LCEState<SessionState>, SessionState>(appState = appState),
    IViewModel<SessionState> {

    fun requestRoute(){
        val routeList = listOf(
            RouteDataDTO("1201",12),
            RouteDataDTO("2587",15),
            RouteDataDTO("4897",15),
            RouteDataDTO("789",14)
        )
        renderState.value = LCEState.Content(SessionState.RouteLoaded(routeList))
    }
    override val renderState: MutableLiveData<LCEState<SessionState>>
        get() = getLiveData()

}