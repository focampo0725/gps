package com.kloubit.gps.infrastructure.business

interface IPayload{
    val topicName : String
    override fun toString() : String
}