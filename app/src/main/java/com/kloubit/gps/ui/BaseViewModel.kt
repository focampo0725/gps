package com.kloubit.gps.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kloubit.gps.infrastructure.extensions.postDelay
import com.kloubit.gps.infrastructure.stateful.AppState


open class BaseViewModel<T : LCEState<K>, K> (val appState: AppState): ViewModel() {
    private var mutableLiveData : MutableLiveData<Any>? = null

    fun <T>getLiveData() : MutableLiveData<T> {
        if(mutableLiveData == null)
            mutableLiveData = MutableLiveData()
        return mutableLiveData as MutableLiveData<T>
    }

    /**
     * parsea el valor recibido del LCE para enviar el dato en el tipo
     * correspondiente.
     * Nota : Se hizo uso del Any para no tener limitaciones con los tipos de Loading,
     * Error..
     */
    private fun<K> parseValueLCE(data : K): Any {
        if(data is LCEState.Loading<*> || data is LCEState.Error<*>)
            return data
        else(data is LCEState.Content<*>)
        return LCEState.content(data)
    }
    fun <K>sendValue(message : K){
        mutableLiveData?.value = parseValueLCE(message)
    }
    fun <K>postMessage(message : K){
        mutableLiveData?.postValue(parseValueLCE(message))
    }
    fun <K>postMessageWithDelay(message : K, delay : Long = 100){
        mutableLiveData?.postDelay(parseValueLCE(message), delay = delay)
    }
}