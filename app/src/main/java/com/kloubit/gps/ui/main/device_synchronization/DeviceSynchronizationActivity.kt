package com.kloubit.gps.ui.main.device_synchronization

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kloubit.gps.databinding.ActivityDeviceSynchronizationBinding
import com.kloubit.gps.ui.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeviceSynchronizationActivity : BaseActivity<DeviceSynchronizationViewModel,DeviceSynchronizationState>() {
    private lateinit var binding : ActivityDeviceSynchronizationBinding
    override fun processRenderState(renderState: DeviceSynchronizationState, context: Context) {
        TODO("Not yet implemented")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceSynchronizationBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}