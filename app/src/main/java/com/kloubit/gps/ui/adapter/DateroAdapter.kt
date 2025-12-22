//package com.kloubit.gps.ui.adapter
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.kloubit.gps.domain.dto.TimekeepingDTO
//
//class DateroAdapter(private val context: Context) :
//    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//
//    private var linearDateroAbovelList: List<TimekeepingDTO> = emptyList()
//    private var textSize: Float = 16f
//
//    companion object {
//        private const val TYPE_DATERO = 0
//        private const val TYPE_ANOTHER = 1
//    }
//
//    fun updateAboveListData(linearDateroAboveList: List<TimekeepingDTO>, textSize: Float) {
//        this.linearDateroAbovelList = linearDateroAboveList
//        this.textSize = textSize
//        notifyDataSetChanged()
//    }
//
//    // Definir el tipo de item según una propiedad del DTO
//    override fun getItemViewType(position: Int): Int {
//        val item = linearDateroAbovelList[position]
//        return if (item.isItem) TYPE_DATERO else TYPE_ANOTHER
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        val inflater = LayoutInflater.from(context)
//        return when (viewType) {
//            TYPE_DATERO -> {
//                val binding = ItemDateroBinding.inflate(inflater, parent, false)
//                DateroViewHolder(binding)
//            }
//            TYPE_ANOTHER -> {
//                val binding = ItemAnotherBinding.inflate(inflater, parent, false)
//                AnotherViewHolder(binding)
//            }
//            else -> throw IllegalArgumentException("Tipo desconocido")
//        }
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        val item = linearDateroAbovelList[position]
//
//        when (holder) {
//            is DateroViewHolder -> holder.bind(item, textSize)
//            is AnotherViewHolder -> holder.bind(item)
//        }
//    }
//
//    override fun getItemCount(): Int = linearDateroAbovelList.size
//
//    // ViewHolder para ItemDatero
//    inner class DateroViewHolder(private val binding: ItemDateroBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//
//        fun bind(item: TimekeepingDTO, textSize: Float) {
//            binding.textView.text = item.text
//            binding.textView.textSize = textSize
//        }
//    }
//
//    // ViewHolder para otro layout
//    inner class AnotherViewHolder(private val binding: ItemAnotherBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//
//        fun bind(item: TimekeepingDTO) {
//            binding.textView.text = item.text
//        }
//    }
//}
