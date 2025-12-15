package com.kloubit.gps.infrastructure.business

import android.content.Context
import com.kloubit.gps.data.repository.AppRepository
import com.kloubit.gps.domain.dto.ABROKDTO
import com.kloubit.gps.infrastructure.stateful.AppState


interface IMQTTCommand {
    fun invoke(appState : AppState, appRepository: AppRepository, abrokdto: ABROKDTO<*>, context: Context)
}