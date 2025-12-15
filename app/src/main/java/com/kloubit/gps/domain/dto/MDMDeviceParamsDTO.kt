package com.kloubit.gps.domain.dto

data class DeviceOptions(
    val rebootsPerDay: Int,
    val companyCode: Int
)

data class PingConnection(
    val isPingEnabled: Boolean,
    val pingAttemps: Int,
    val address: String,
    val pingTime: Long
)

data class TrackParams(
    val isTrackEnabled: Boolean,
    val isSendPendingTracks: Boolean,
    val timeUploadPendingTracks: Long,
    val minimumSpeed: Int,
    val maximumSpeed: Int,
    val maximumTimeStopped: Int,
    val smallInterval: Int,
    val largeInterval: Int
)

data class CallParams(
    val rtcEnabled: Boolean,
    val signalServerUrl: String
)

data class IdentityServerCredentials(
    val username: String,
    val password: String,
    val clientId: String,
    val clientSecret: String,
    val scope: String,
    val grantType: String
)

data class BrokerParams(
    val brokerUrl: String,
    val brokerUsername: String,
    val brokerPassword: String
)

data class ConfigData(
    val deviceOptions: DeviceOptions,
    val pingConnection: PingConnection,
    val trackParams: TrackParams,
    val callParams: CallParams,
    val identityServerCredentials: IdentityServerCredentials,
    val brokerParams: BrokerParams
)
