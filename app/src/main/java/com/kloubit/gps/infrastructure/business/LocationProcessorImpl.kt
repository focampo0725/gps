package com.kloubit.gps.infrastructure.business

import android.content.Context
import android.location.Location
import com.kloubit.gps.data.repository.AppRepository
import com.kloubit.gps.infrastructure.stateful.AppState

interface ILocationProcessor {
    fun onLocation(location: Location)
}
class LocationProcessorImpl (private val appRepository: AppRepository, private val appState: AppState, private val context: Context) : ILocationProcessor
{
    override fun onLocation(location: Location) {
        TODO("Not yet implemented")
    }
}