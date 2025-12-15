package com.kloubit.gps.ui.main.session

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.kloubit.gps.R
import com.kloubit.gps.databinding.FragmentSessionBinding
import com.kloubit.gps.infrastructure.extensions.toast
import com.kloubit.gps.ui.BaseFragment
import com.kloubit.gps.ui.dialog.RouteDialog
import com.kloubit.gps.ui.main.device_synchronization.DeviceSynchronizationActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SessionFragment @Inject constructor(): BaseFragment<SessionViewModel, SessionState>() {
    private val viewModel by viewModels<SessionViewModel>()
    private lateinit var binding : FragmentSessionBinding
    companion object{
        fun newInstanceSessionFragment(): SessionFragment{
            return SessionFragment()
        }
    }

    override fun processRenderState(renderState: SessionState, context: Context) {
        when(renderState){
            is SessionState.RouteLoaded -> {
                RouteDialog(routeDataList = renderState.routeDataList) { route ->
                    binding.tvRouteName.text = route.routeName
                }.show(parentFragmentManager, RouteDialog.TAG)
            }
            else -> {}
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSessionBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel(viewModel = viewModel)
        initView()
    }

    fun initView(){
        onClick()
        binding.tvRouteName.setOnClickListener {
            viewModel.requestRoute()
        }
        binding.btnContinue.setOnClickListener {
            if (binding.tvRouteName.text.isNotEmpty()   && binding.tvServiceCode.text.isNotEmpty() && binding.tvDriverCode.text.isNotEmpty()) {
                context?.toast("Enviar")
            } else {
                context?.toast("Falta llenar un campo")
            }

        }
        binding.tvServiceCode.setOnClickListener {
            it.requestFocus()
        }

        binding.tvDriverCode.setOnClickListener {
            it.requestFocus()
        }

        binding.btnDelete.setOnClickListener {
            val focusedView = activity?.currentFocus
            if (focusedView is TextView) {
                val currentText = focusedView.text.toString()
                if (currentText.isNotEmpty()) {
                    focusedView.text = currentText.substring(0, currentText.length - 1)
                }
            }
        }

    }


    fun onClick() {
        val numberButtons = listOf(
            binding.btnZero,
            binding.btnOne,
            binding.btnTwo,
            binding.btnThree,
            binding.btnFour,
            binding.btnFive,
            binding.btnSix,
            binding.btnSeven,
            binding.btnEight,
            binding.btnNine
        )

        // Asignar listener a cada botón
        numberButtons.forEach { button ->
            button.setOnClickListener { numberButton ->
                if (numberButton is Button) {
                    val focusedView = activity?.currentFocus
                    if (focusedView is TextView) {
                        if (focusedView.text.length < 4) {
                            focusedView.text = "${focusedView.text}${numberButton.text}"
                        }
                    }
                }
            }
        }
    }


}