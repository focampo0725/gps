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

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.library.BuildConfig
import com.abx.shared.supportabx.eventbus.SecurityHandlerEventBus
import com.abx.shared.supportabx.handlers.ReceiverManager
import com.abx.shared.supportabx.receivers.INetworkStateChangeReceiver
import com.kloubit.gps.infrastructure.extensions.logi
import com.kloubit.gps.infrastructure.extensions.toast
import com.kloubit.gps.infrastructure.stateful.AppState

interface OnToGoNewActivity {
    fun onNextActivity(cls : Class<*>, bundle : Bundle?, isFinish : Boolean = false)
}

abstract class BaseActivity<T : IViewModel<K>, K> : AppCompatActivity(), OnToGoNewActivity {

//    var closureAppBackground : (()-> Unit)? = null
    var isClosureDone = false
    val closureOnStartDelay = Runnable {
        // Do what ever you want
//        SecurityUtils.onStart()
//        SecurityUtils.activity = this
        isClosureDone = true
    }
    var appState : AppState? = null
    private val handler = Handler(Looper.getMainLooper())

    fun updateUI(state : LCEState<K>){
        when (state) {
            is LCEState.Loading -> onLoading(isStarted = (state.state == LoadingTYPE.START))
            is LCEState.Content -> processRenderState(state.content, this@BaseActivity)
            is LCEState.Error -> logi("error >> ${state.error}")

        }
    }



    open fun onLoading(isStarted : Boolean){}
    abstract fun processRenderState(renderState: K, context: Context)

    fun setupViewModel(viewModel : T){
        viewModel.renderState.observe(::getLifecycle, ::updateUI)
    }

    /**
     * solo llamarla antes de q se renderice el layout
     */



    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        makeStatusBarTransparent()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        ReceiverManager.startNetworkStateChangeReceiver(this, object :
            INetworkStateChangeReceiver {
            override fun onConnectedStateReceiver() {
                if (appState != null){
                    logi("[FrancConection] onCreate -> ${appState?.isNetworkConnection}")
                }else{
                    logi("[FrancConection] onCreate -> no soy nul")
                }
                appState?.isNetworkConnection = true
                logi("[FrancConection] asd -> ${appState?.isNetworkConnection}")
            }
            override fun onDisconnectedStateReceiver() {
                if (appState == null){
                    toast("Soy nulo 02")
                }
                appState?.isNetworkConnection = false
                logi("[FrancConection] onCreate 02-> ${appState?.isNetworkConnection}")
            }
        })
    }

    override fun onStart(){
        super.onStart()
        lockStatusBarAndNavBarBeta()
    }

//    override fun onStart(){
//        super.onStart()
////        hideNavigationBar()
////        SecurityUtils.toListenGlobalActions(this)
////        SecurityUtils.onStart()
////        SecurityUtils.activity = this
////        handler.postDelayed(closureOnStartDelay, SecurityUtils.LIMIT_TIME_APP_BACKGROUND / 2)   // pequeño delay para que el onstart de security se llame luego del onstop de la actividad anterior..
//    }

    override fun onStop() {
        super.onStop()
//        if(isClosureDone)   // si no le da tiempo para q el closure se realice, entonces no alertaremos..
//            SecurityUtils.onStop()
    }

    fun Activity.makeStatusBarTransparent() {
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        supportActionBar?.hide()

        val attrs = this.window.attributes
        attrs.flags = attrs.flags and (WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
//        attrs.flags = attrs.flags and (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

//        this.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.window.attributes = attrs
        this.window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {   // Android 9
            this.window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            this.window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
//            attrs.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
//            this.window.attributes = attrs
        }
        this.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)    // mantendrà la pantalla encendida..
    }

    fun inmerviseScreen(window: Window, appState: AppState) {
        this.appState = appState
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        )
        window.addFlags(
            WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM or
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        )
        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                    or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
    }

    /**
     * corrige el problema del tamanio del layout en algunos modelos como
     * los SamsungA10S
     */


    /**
     * configura los paràmetros de detecciòn de pantalla minimizada..
     */
    fun setupRebootByManipulatedDevice(appState: AppState){
//        SecurityUtils.setup(appState)
    }
    open fun isProtectedActivity() : Boolean = true
    fun lockStatusBarAndNavBarBeta(){
        if(!isProtectedActivity())return
        try {

            Handler().postDelayed({
                com.kloubit.gps.App.enableSecurityStatusBarAndNavBar(this)
            }, 1000)

            ReceiverManager.startOpenStatusBarInterceptorReceiver(this, closure = { // inicia la suscripciòn a las notificaciones de cuando se toque el top bar flotante
                if(BuildConfig.DEBUG)
                    this.toast("Open status bar !!!1")
                SecurityHandlerEventBus.requestLockScreen(this, closure = {// habilita el bloqueo de pantalla por un determinado tiempo
                    if(BuildConfig.DEBUG)
                        this.toast("enableFloatingLockWindow says -> $it")
                    SecurityHandlerEventBus.requestLockStatusBarByTime(context = this, packageNameBusinessApp = this.packageName){// genera un evento swipe_up para deslizar la barra de notificaciones en caso se haya habilitado
                        if(BuildConfig.DEBUG)
                            this.toast("requestLockStatusBarByTime says -> ${it.message}")
                    }
                })
            })
        }catch (e : Exception){
            e.printStackTrace()
        }
    }

//    abstract fun injectDependencies(applicationComponent: ApplicationComponent)
//    abstract fun updateUI(screenState: ScreenState<Nothing>?)

//    fun updateUI(screenState: ScreenState<Nothing>?) {
//        when (screenState) {
//            is ScreenState.Render -> processRenderState(screenState.renderState, context)
//        }
//    }

    /**
     * envìa comando al supporsuite para ocultar la barra de navegaciòn..
     * Nota : no hay problema con que se vuelva a enviar repetidas veces sin haber cerrado antes, ya que
     * el widget està preparado para no crear una pila de widgets...
     */
//    private fun hideNavigationBar(){
//        if(!BuildConfig.DEBUG){
//            RequestIntent.requestShowFloatingWidgetOverNavigationBar(this, data = "", closure = { s ->
//                if(BuildConfig.DEBUG)
//                    this.logi("___ answer")
//            })
//        }
//    }

    override fun onNextActivity(cls: Class<*>, bundle : Bundle?, isFinish : Boolean) {
        val intent = Intent(this, cls)
        bundle?.let {
            intent.putExtras(bundle)
        }
        startActivity(intent)
//        overridePendingTransition(R.anim.slide_in_right, R.anim.no_animation);
        if (isFinish)
        {
            handler.removeCallbacks(closureOnStartDelay)
            finish()
        }
    }

    fun finishAnimate(){
        finish()
//        overridePendingTransition(R.anim.no_animation, R.anim.slide_out_right);
    }


}