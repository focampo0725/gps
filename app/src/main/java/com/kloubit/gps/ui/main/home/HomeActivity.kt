package com.kloubit.gps.ui.main.home

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.kloubit.gps.databinding.ActivityHomeBinding
import com.kloubit.gps.ui.BaseActivity
import com.kloubit.gps.ui.main.personnel_identification.PersonnelIdentificationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity<HomeViewModel, HomeState>() {
    val viewModel: HomeViewModel by viewModels()
    private lateinit var binding : ActivityHomeBinding
    override fun processRenderState(renderState: HomeState, context: Context) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViewModel(viewModel = viewModel)
    }
}