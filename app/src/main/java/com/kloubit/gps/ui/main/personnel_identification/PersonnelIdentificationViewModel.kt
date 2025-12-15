package com.kloubit.gps.ui.main.personnel_identification

import androidx.lifecycle.MutableLiveData
import com.kloubit.gps.infrastructure.stateful.AppState
import com.kloubit.gps.ui.BaseViewModel
import com.kloubit.gps.ui.IViewModel
import com.kloubit.gps.ui.LCEState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

sealed class PersonnelIdentificationState {

}

@HiltViewModel
class PersonnelIdentificationViewModel @Inject constructor(appState: AppState) : BaseViewModel<LCEState<PersonnelIdentificationState>,PersonnelIdentificationState>(appState = appState), IViewModel<PersonnelIdentificationState>{
    override val renderState: MutableLiveData<LCEState<PersonnelIdentificationState>>
        get() = getLiveData()

}