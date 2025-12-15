package com.kloubit.gps.ui.main.splashscreen

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.abx.shared.supportabx.extensions.doAsync
import com.abx.shared.supportabx.extensions.logi
import com.kloubit.gps.data.repository.AppRepository
import com.kloubit.gps.infrastructure.stateful.AppState
import com.kloubit.gps.ui.BaseViewModel
import com.kloubit.gps.ui.IViewModel
import com.kloubit.gps.ui.LCEState
import com.kloubit.gps.ui.main.device_synchronization.DeviceSynchronizationActivity
import com.kloubit.gps.ui.main.personnel_identification.PersonnelIdentificationActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject

sealed class SplashScreenState {
    class LoadActivity(val cls: Class<*>) : SplashScreenState()
}

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    appState: AppState,
    val appRepository: AppRepository
) : BaseViewModel<LCEState<SplashScreenState>, SplashScreenState>(appState = appState),
    IViewModel<SplashScreenState> {

    override val renderState: MutableLiveData<LCEState<SplashScreenState>>
        get() = getLiveData()

    fun loadScreen(){
        val toGoPersonnelIdentification: () -> Unit = {
            super.postMessage(SplashScreenState.LoadActivity(PersonnelIdentificationActivity::class.java))
        }
        val toGoDeviceSynchronization: () -> Unit = {
            super.postMessage(SplashScreenState.LoadActivity(DeviceSynchronizationActivity::class.java))
        }

        if (appRepository.spf.isPersonnelIdentification) {
            toGoDeviceSynchronization.invoke()
        }else if(appRepository.spf.isDeviceSynchronization){

        }else{
            toGoPersonnelIdentification.invoke()
        }
    }


}


