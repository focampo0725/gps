package com.kloubit.gps.ui.main.view_pager

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.kloubit.gps.R
import com.kloubit.gps.databinding.FragmentViewPagerBinding
import com.kloubit.gps.ui.BaseFragment
import com.kloubit.gps.ui.adapter.MyFragmentPagerAdapter
import com.kloubit.gps.ui.main.session.SessionViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ViewPagerFragment @Inject constructor() : BaseFragment<ViewPagerViewModel,ViewPagerState>(){
    private val viewModel by viewModels<ViewPagerViewModel>()
    private lateinit var binding : FragmentViewPagerBinding
    private lateinit var adapterViewPager: MyFragmentPagerAdapter
    override fun processRenderState(renderState: ViewPagerState, context: Context) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewPagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel(viewModel = viewModel)
        adapterViewPager = MyFragmentPagerAdapter(requireActivity())
        binding.vpContainer?.let {

            it.adapter = adapterViewPager
            it.currentItem = MyFragmentPagerAdapter.EFRAGMENTS.SessionFragment.value


        }
        binding.vpContainer.setOffscreenPageLimit(3)
    }

}