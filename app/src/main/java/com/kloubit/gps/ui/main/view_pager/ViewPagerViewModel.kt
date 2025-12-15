package com.kloubit.gps.ui.main.view_pager

import androidx.lifecycle.MutableLiveData

import com.kloubit.gps.infrastructure.stateful.AppState
import com.kloubit.gps.ui.BaseViewModel
import com.kloubit.gps.ui.IViewModel
import com.kloubit.gps.ui.LCEState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

sealed class ViewPagerState {

}
@HiltViewModel
class ViewPagerViewModel @Inject constructor(appState: AppState) : BaseViewModel<LCEState<ViewPagerState>, ViewPagerState>(appState = appState),
    IViewModel<ViewPagerState> {


    //    fun isDispacthExecute( viewpager : ViewPager2) {
//        appState.onRechargeLocalizatorView = {
//            viewpager = MyFragmentPagerAdapter.EFRAGMENTS.HomeFragment.value
//        }
//    }
    override val renderState: MutableLiveData<LCEState<ViewPagerState>>
        get() = getLiveData()

}