package com.kloubit.gps.infrastructure.clients

import android.content.Context
import com.abx.shared.supportabx.extensions.applySchedulers
import com.abx.shared.supportabx.extensions.logerror
import com.abx.shared.supportabx.utils.NetworkUtils
import com.kloubit.gps.data.repository.AppRepository
import com.kloubit.gps.infrastructure.stateful.AppState
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.*
import java.util.concurrent.TimeUnit

class ClientThread (val context: Context, val appRepository: AppRepository, val appState: AppState):IClient{

    companion object{
        private val DELAY : Long = 10000

        /**
         * crea un requestAccess dto para enviarse en el payload de la solicitud de access token..
         */
//        fun getRequestAccessDTO(appState: AppState) = RequestAccessDTO(
//            grantType = appState.appParamsDTO?.identityServerCredentials?.grantType!!,
//            userName = appState.appParamsDTO?.identityServerCredentials?.username!!,
//            password = appState.appParamsDTO?.identityServerCredentials?.password!!,
//            scope = appState.appParamsDTO?.identityServerCredentials?.scope!!)


    }
    private var jobDisposable : Disposable? = null  // job in another thread
    private var sendingPingLastDate = Date()
    private var successPingLastDate = Date()
    override fun onStart(context: Context) {
        jobDisposable = Observable.interval(0, DELAY, TimeUnit.MILLISECONDS)
            .doOnNext {
                checkAccessTokenRequest(appState, appRepository)
            }
            .applySchedulers().subscribe({
            }, {
                context.logerror(it.message!!)
            })

        // inicia el proceso de validación ANR
//        if(appState.appParamsDTO?.anrParamsDTO?.anrSubprocessValidationEnabled == true){
//            context.logi("[ANRListener] -->ANR detected -->ANR detected:")
//            anrWatchDogClient.onStart(context)
//        }

    }
    /**
     * comprueba el estado del access token , en caso de no ser vàlido
     * por haber expirado o no existir, entonces se volverà a solicitar..
     */
    private fun checkAccessTokenRequest(appState: AppState, appRepository: AppRepository){
//        if(!appRepository.accessTokenService.isValidAccessToken()) {
//            appRepository.identityServerApi.getAccessToken(ClientThread.getRequestAccessDTO(appState)).applySchedulers().subscribeApp(
//                onSuccess = {
//                    logi("[PersonalIdentifier] Token")
//                    appRepository.accessTokenService.saveSession(it)
//                }, onError = {
//                    logi("[PersonalIdentifier] ERROR TOKEN error al solicitar el access token $it")
//                })
//        }
    }


    


    override fun onDestroy() {
        jobDisposable?.dispose()
    }
}