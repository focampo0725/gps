package com.kloubit.gps.ui.main.home

import androidx.lifecycle.MutableLiveData
import com.kloubit.gps.infrastructure.stateful.AppState
import com.kloubit.gps.ui.BaseViewModel
import com.kloubit.gps.ui.IViewModel
import com.kloubit.gps.ui.LCEState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

sealed class HomeState{

}
@HiltViewModel
class HomeViewModel @Inject constructor(appState :AppState) : BaseViewModel<LCEState<HomeState>, HomeState>(appState = appState),
IViewModel<HomeState>{
    override val renderState: MutableLiveData<LCEState<HomeState>>
        get() = getLiveData()

}