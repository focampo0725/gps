package com.kloubit.gps.ui.adapter

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.abx.shared.supportabx.extensions.logi
import com.kloubit.gps.R
import com.kloubit.gps.databinding.ItemRouteMapBinding
import com.kloubit.gps.domain.dto.LocatorControlDTO
import com.kloubit.gps.infrastructure.utils.DifferenceBetweenDatesUtils


class LocatorControlsAdapter(private val context: Context) :
    RecyclerView.Adapter<LocatorControlsAdapter.ViewHolder>() {

    private var linearControlList: List<LocatorControlDTO> = emptyList()

    fun setLinearControls(linearControlList: List<LocatorControlDTO>) {
        context.logi("[FrancoAdapter] setLinearControls -> $linearControlList")
        this.linearControlList = linearControlList
        notifyDataSetChanged()
    }
    fun clear() {
        linearControlList = emptyList()
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): LocatorControlsAdapter.ViewHolder {
        val view = ItemRouteMapBinding.inflate(LayoutInflater.from(context), viewGroup, false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return linearControlList.size
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {


        with(viewHolder){
            with(linearControlList[position]){
                binding.tvControlName.text = controlAbbreviatedName
                binding.tvScheduledDateTime.text = DifferenceBetweenDatesUtils.longToHour(scheduledDateTime)
                var arrivalDifference = DifferenceBetweenDatesUtils.getDifferenceBetweenDates(scheduledDateTime,arrivalDateTime)
                if (arrivalDateTime != 0L && arrivalDateTime != null){
                    setArrivalBackground(binding.tvArrivalDateTime,arrivalDifference.toInt(),controlType)
                    binding.tvArrivalDateTime.text = arrivalDifference
                }

            }
        }
    }
    fun setArrivalBackground(tv: TextView, arrivalDifference: Int, controlType: Int) {
        val backgroundRes = when {
            controlType == 1 -> R.drawable.bg_terminal_indicator       // Caso controlType = 1
            arrivalDifference > 0 -> R.drawable.bg_delay_indicator // Caso arrivalDifference > 0
            arrivalDifference < 0 -> R.drawable.bg_early_indicator // Caso arrivalDifference < 0
            else -> R.drawable.bg_punctuality_indicator                   // Caso arrivalDifference == 0
        }

        tv.setBackgroundResource(backgroundRes)
    }



    inner class ViewHolder(val binding : ItemRouteMapBinding) : RecyclerView.ViewHolder(binding.root)
}