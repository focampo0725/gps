package com.kloubit.gps.ui.main.route_map

import androidx.lifecycle.MutableLiveData
import com.kloubit.gps.domain.dto.ListItem
import com.kloubit.gps.infrastructure.stateful.AppState
import com.kloubit.gps.ui.BaseViewModel
import com.kloubit.gps.ui.IViewModel
import com.kloubit.gps.ui.LCEState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

sealed class RouteState {
    class DateroTest(val listItem: List<ListItem>) : RouteState()

}
@HiltViewModel
class RouteViewModel @Inject constructor(appState: AppState) :
    BaseViewModel<LCEState<RouteState>, RouteState>(appState = appState),
    IViewModel<RouteState> {

    fun dateroTest(){
        val listItem: List<ListItem> = listOf(
            ListItem.TimekeepingHeaderDTO("Turno Mañana"),
            ListItem.TimekeepingBodyDTO("04:55", "1"),
            ListItem.TimekeepingBodyDTO("05:10:20444", "2"),
            ListItem.TimekeepingHeaderDTO("Turno Tarde"),
            ListItem.TimekeepingBodyDTO("14", "1"),
            ListItem.TimekeepingBodyDTO("1413", "2")
        )
        renderState.postValue(LCEState.content(RouteState.DateroTest(listItem)))
    }
    fun getDummyData(): List<ListItem> {
        return listOf(

        )
    }


    override val renderState: MutableLiveData<LCEState<RouteState>>
        get() = getLiveData()

}