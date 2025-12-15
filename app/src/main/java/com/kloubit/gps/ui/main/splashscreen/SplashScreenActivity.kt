package com.kloubit.gps.ui.main.splashscreen

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.abx.shared.supportabx.extensions.logi
import com.google.gson.Gson
import com.hmdm.HeadwindMDM
import com.hmdm.MDMService
import com.kloubit.gps.databinding.ActivitySplashScreenBinding
import com.kloubit.gps.domain.dto.ConfigData
import com.kloubit.gps.ui.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenActivity : BaseActivity<SplashScreenViewModel, SplashScreenState>(), HeadwindMDM.EventHandler{

    private val viewModel: SplashScreenViewModel by viewModels()
    private lateinit var binding: ActivitySplashScreenBinding
    private var headwindMDM: HeadwindMDM? = null
    override fun processRenderState(renderState: SplashScreenState, context: Context) {
       when(renderState){
            is SplashScreenState.LoadActivity ->{
                onNextActivity(renderState.cls, null, true)
            }
           else -> {}
       }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViewModel(viewModel)
        inmerviseScreen(window,viewModel.appState)
        headwindMDM = HeadwindMDM.getInstance()

        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.loadScreen()
            if (!headwindMDM?.isConnected()!!) {
                if (!headwindMDM?.connect(this, this)!!) {

                }
            } else {
                Log.i("MDM","Desconectado")
            }
        }, 2800)
    }
    override fun onResume() {
        super.onResume()
        if (!headwindMDM?.isConnected()!!) {
            if (!headwindMDM?.connect(this, this)!!) {
                // Tu aplicación está corriendo fuera de Headwind MDM
            }
        } else {
            Log.i("MDM","Desconectado")
        }
    }
    override fun onHeadwindMDMConnected() {
        Log.i("[FrancoLogi]","onHeadwindMDMConnected -> [Conected]")
        loadConfigurations()
    }

    override fun onHeadwindMDMDisconnected() {
        Log.i("[FrancoLogi]","onHeadwindMDMConnected -> [DisConected]")
    }

    override fun onHeadwindMDMConfigChanged() {
        Log.i("[FrancoLogi]","onHeadwindMDMConnected -> [AlterConfiguration]")
    }
    fun loadConfigurations(){
        var data = MDMService.Preferences.get("deviceParams","FAILED")
        logi("[FrancoLogi] : data : $data ")
        if (data != "FAILED" && data.isNotEmpty()){
            val configData = Gson().fromJson(data,ConfigData::class.java)
            logi("[FrancoLogi] : data : $configData ")
        } else {
            logi("[FrancoLogi]  -> No hay datos válidos")
        }

    }
}