package com.kloubit.gps.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.abx.shared.supportabx.extensions.logi
import com.kloubit.gps.R
import com.kloubit.gps.databinding.ItemBodyDateroBinding
import com.kloubit.gps.databinding.ItemHeaderDateroBinding
import com.kloubit.gps.databinding.ItemRouteMapBinding
import com.kloubit.gps.domain.dto.ListItem
import com.kloubit.gps.domain.dto.LocatorControlDTO
import com.kloubit.gps.domain.dto.TimekeepingDTO

class DateroAdapter(private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items: List<ListItem> = emptyList()
    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_INFO = 1
    }

    fun setLinearDatero(linearDatero: List<ListItem>) {
        this.items = linearDatero
        notifyDataSetChanged()
    }

    // Decide el tipo de ítem según la posición
    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is ListItem.TimekeepingHeaderDTO -> TYPE_HEADER
            is ListItem.TimekeepingBodyDTO -> TYPE_INFO
        }
    }

    // Crear ViewHolder según el tipo
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                val binding = ItemHeaderDateroBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                HeaderViewHolder(binding)
            }
            TYPE_INFO -> {
                val binding = ItemBodyDateroBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                InfoViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Tipo de vista desconocido")
        }
    }


    // Vincular datos con cada ViewHolder
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is ListItem.TimekeepingHeaderDTO -> (holder as HeaderViewHolder).bind(item)
            is ListItem.TimekeepingBodyDTO -> (holder as InfoViewHolder).bind(item)
        }
    }

    override fun getItemCount() = items.size

    // -------------------
    // ViewHolders
    // -------------------

    class HeaderViewHolder(private val binding: ItemHeaderDateroBinding) :
        RecyclerView.ViewHolder(binding.root) {  // <-- aquí usamos binding.root

        fun bind(item: ListItem.TimekeepingHeaderDTO) {
            binding.tvHeader.text = item.titulo
        }
    }

    class InfoViewHolder(private val binding: ItemBodyDateroBinding) :
        RecyclerView.ViewHolder(binding.root) {  // <-- binding.root

        fun bind(item: ListItem.TimekeepingBodyDTO) {
            binding.tvScheduledDateTime.text = item.scheduledDateTime
            binding.tvArrivalDateTime.text = item.arrivalDiferenceTime
        }
    }
}

