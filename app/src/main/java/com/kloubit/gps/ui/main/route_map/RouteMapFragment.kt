package com.kloubit.gps.ui.main.route_map

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kloubit.gps.databinding.FragmentRouteMapragmentBinding
import com.kloubit.gps.ui.BaseFragment
import com.kloubit.gps.ui.adapter.DateroAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RouteMapFragment @Inject constructor(): BaseFragment<RouteViewModel, RouteState>() {
    private val viewModel by viewModels<RouteViewModel>()
    private lateinit var binding : FragmentRouteMapragmentBinding
    private lateinit var dateroAdapter : DateroAdapter
    companion object{
        fun newInstanceRouteMapFragment(): RouteMapFragment {
            return RouteMapFragment()
        }
    }
    override fun processRenderState(renderState: RouteState, context: Context) {
        when(renderState){
            is RouteState.DateroTest -> {
                dateroAdapter.setLinearDatero(renderState.listItem)
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
        binding = FragmentRouteMapragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel(viewModel = viewModel)
        var layoutManager = object : LinearLayoutManager(requireContext()) {
            override fun canScrollVertically(): Boolean {
                return false // Bloquea el scroll vertical
            }
        }
        binding.rvDatero?.layoutManager = layoutManager
        dateroAdapter = DateroAdapter(requireContext())
        binding.rvDatero?.adapter = dateroAdapter

        viewModel.dateroTest()

    }


}