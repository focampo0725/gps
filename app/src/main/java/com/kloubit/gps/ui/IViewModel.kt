package com.kloubit.gps.ui

import androidx.lifecycle.MutableLiveData

interface IViewModel<T>{
    val renderState : MutableLiveData<LCEState<T>>
//    val timeStamp : Long
}