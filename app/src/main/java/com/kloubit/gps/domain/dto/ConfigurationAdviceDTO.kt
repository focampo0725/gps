package com.kloubit.gps.domain.dto

import android.view.View
import com.kloubit.gps.R


data class ConfigurationAdviceDTO(
    val title: String,
    val caption: String,
    val isCancelable: Boolean = true,
    val highlightText: String = "",
    val alignment: Int = View.TEXT_ALIGNMENT_CENTER,
    val isScrollable: Boolean = false,
    val fontFamily: Int = R.font.segoeuil
)

class DeviceDataDTO(var imei : String = "",var phoneNumber : String = "")