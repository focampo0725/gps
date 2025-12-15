package com.kloubit.gps.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kloubit.gps.databinding.ItemRouteBinding
import com.kloubit.gps.domain.dto.RouteDataDTO


class RouteAdapter(private val context: Context, private var routeDataList : List<RouteDataDTO>, val onClick : (RouteData : RouteDataDTO) -> Unit) :
    RecyclerView.Adapter<RouteAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemRouteBinding.inflate(LayoutInflater.from(context), viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        with(viewHolder){
            with(routeDataList[position]){
                binding.tvRoutename.text = routeName
                binding.tvRouteCode.text = routeCode.toString()
                binding.tvIndex.text = (position + 1).toString()
                val onClick : (view : View) -> Unit = { v : View ->
                    onClick(this)
                }
                binding.root.setOnClickListener(onClick)

            }
        }

    }

    override fun getItemCount(): Int = routeDataList.size

    inner class ViewHolder(val binding : ItemRouteBinding) : RecyclerView.ViewHolder(binding.root)
}

