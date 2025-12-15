package com.kloubit.gps.infrastructure.utils

import android.content.Context
import android.content.Intent
import com.kloubit.gps.R
import com.kloubit.gps.infrastructure.extensions.logi
import com.kloubit.gps.infrastructure.extensions.toast

import kotlinx.coroutines.*

/**
 * utilitario de lanzamiento..
 * Posteriormente se podrìan usar algunos de estos mètodos
 * en el launcher app..
 */
class LaunchAppUtils {

    companion object{


        fun launchToAppAgain(context: Context, delayMilis : Long = 500, comprobationClosure : (()-> Boolean), closure : (()->Unit)? ){
            GlobalScope.launch(Dispatchers.Main) { // launch a new coroutine in the main thread
                delay(500)
                if(comprobationClosure()){
                    logi("context get launch intent for package")
                    context.packageManager.getLaunchIntentForPackage(context.packageName)?.let {

                        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP

                        context.startActivity(it)
                        context.toast("Retornando a la app")
                        closure?.invoke()
                    }
                }else{
                    launchToAppAgain(context, delayMilis, comprobationClosure, closure)  // recursively..
                }
            }
        }





    }
}