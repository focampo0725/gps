package com.kloubit.gps.domain.dto

data class LocatorControlDTO(
    var scheduledDateTime : Long,
    val controlCode: Int,
    val controlName: String,
    val controlType: Int,
    val controlAbbreviatedName: String,
    var arrivalDateTime : Long,
    )

data class TimekeepingDTO(
    val isItem: Boolean,
    val text: String,
    val difference: Int
)

sealed class ListItem {

    data class TimekeepingHeaderDTO(
        val titulo: String
    ) : ListItem()

    data class TimekeepingBodyDTO(
        val scheduledDateTime: String,
        val arrivalDiferenceTime : String
    ) : ListItem()
}

data class CountControlWithoutArriveAndArrive(val arrivedMarkedCount : Int, val withouArrivedMarkedCount : Int)


