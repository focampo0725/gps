/*
 * Copyright (C) 2015 Antonio Leiva
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kloubit.gps.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.kloubit.gps.infrastructure.extensions.logi


abstract class BaseFragment<T : IViewModel<K>, K> : Fragment() {

    var activityCallback : OnToGoNewActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            activityCallback = context as? OnToGoNewActivity
        } catch (e: ClassCastException) {
            throw ClassCastException(context?.toString() + " must implement ToolbarListener")
        }
    }

    open fun onLoading(isStarted : Boolean){}
    abstract fun processRenderState(renderState: K, context: Context)
    fun updateUI(state : LCEState<K>){
        when (state) {
            is LCEState.Loading -> onLoading(isStarted = (state.state == LoadingTYPE.START))
            is LCEState.Content -> processRenderState(state.content, requireActivity())
            is LCEState.Error -> logi("error >> ${state.error}")

        }
    }
    fun setupViewModel(viewModel : T){
        viewModel.renderState.observe(::getLifecycle, ::updateUI)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


}