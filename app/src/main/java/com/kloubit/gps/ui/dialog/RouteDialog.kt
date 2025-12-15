package com.kloubit.gps.ui.dialog

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kloubit.gps.R
import com.kloubit.gps.databinding.DialogRouteBinding
import com.kloubit.gps.domain.dto.RouteDataDTO
import com.kloubit.gps.ui.adapter.RouteAdapter

class RouteDialog (val routeDataList : List<RouteDataDTO>, val onClick : (routeData: RouteDataDTO) -> Unit) : DialogFragment(){
    private lateinit var binding : DialogRouteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogLight)
    }
    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogRouteBinding.inflate(LayoutInflater.from(context))
        this.dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        this.dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        val adapter = RouteAdapter(context!!, routeDataList){
            onClick(it)
            dismiss()
        }
        binding.rvAreaWarehouse.layoutManager = LinearLayoutManager(context)
        binding.rvAreaWarehouse.adapter = adapter
        binding.rvAreaWarehouse.setHasFixedSize(true)
        return binding.root
    }

    companion object {
        const val TAG = "RouteDialog"
    }
}