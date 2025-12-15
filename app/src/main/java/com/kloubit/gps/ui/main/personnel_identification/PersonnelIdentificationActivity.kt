package com.kloubit.gps.ui.main.personnel_identification

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import com.kloubit.gps.databinding.ActivityPersonnelIdentificationBinding
import com.kloubit.gps.ui.BaseActivity
import com.kloubit.gps.ui.main.device_synchronization.DeviceSynchronizationActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PersonnelIdentificationActivity @Inject constructor() : BaseActivity<PersonnelIdentificationViewModel,PersonnelIdentificationState>() {
    val viewModel: PersonnelIdentificationViewModel by viewModels()
    private lateinit var binding : ActivityPersonnelIdentificationBinding
    override fun processRenderState(renderState: PersonnelIdentificationState, context: Context) {
        TODO("Not yet implemented")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonnelIdentificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViewModel(viewModel)

        initView()
    }

    private fun initView() {
        binding.numericKeyboard.btnContinue.setOnClickListener {
            if (binding.tvSecurityCode.text.isNotEmpty()   && binding.tvSecurityCode.text.length == 4) {
                onNextActivity(DeviceSynchronizationActivity::class.java, null, true)
            } else {
                Toast.makeText(this, "Debe ingresar un Token", Toast.LENGTH_SHORT).show()
            }

        }

        binding.numericKeyboard.btnDelete.setOnClickListener {
            binding.tvSecurityCode.let {
                if (it.text.isNotEmpty()) {
                    it.text = it.text.substring(0, it.text.length - 1)
                } else {
                    Toast.makeText(this, "Back", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    fun onClick(numberButton: View) {
        if (numberButton is Button) {
            if (binding.tvSecurityCode.text.length < 4)
                binding.tvSecurityCode.text = "${binding.tvSecurityCode.text}${numberButton.text}"
        }
    }
}