package com.kloubit.gps.domain.dto

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.internal.LinkedTreeMap

/***
{
"hd":{
"uid":"40158",
"tr":"nulito",
"cmd":"setLocationBus",
"ctype":"FIRE_AND_FORGET",
"origin":"BM.0000000002",
"destiny":"BD.0000000001",
"seq":1,
"seqt":1
},
"bd":{
"vehicleIndicators":[
{
"firstLabel":"19",
"secondLabel":"19",
"thirdLabel":"19",
"isRedCardIcon":true,
"percentageLocation":80,
"idCar":"STU123",
"isBorderAll":false,
"scheduleTime:"10\n5",
"differenceArriveTimeWithScheduleTime" = "5",
},
{
"firstLabel":"22",
"secondLabel":"22",
"thirdLabel":"22",
"isRedCardIcon":false,
"percentageLocation":25,
"idCar":"VWX456",
"isBorderAll":false,
"scheduleTime:"10\n5",
"differenceArriveTimeWithScheduleTime" = "5",
}
],
"busStop":[
{
"busStopId":"BS123",
"busStopName":"AUA",
"routeName":"1",
"percentageLocation":75,
"scheduleTime:"10\n5",
"differenceArriveTimeWithScheduleTime" = "5",
},
{
"busStopId":"BS151",
"busStopName":"STU",
"routeName":"9",
"percentageLocation":25,
"isBorderAll":true
}
]
},
"ft":{
"ver":"1.0.0",
"sig":"null"
}
}
 */
data class ABROKDTO<T> (
        val hd: HdDTO,
        val bd: T,
        val ft: FtDTO?){

        fun getBdAsJSON() : String{
                if(bd is LinkedTreeMap<*, *>){
                        return GsonBuilder().create().toJson(bd)
                }
                return Gson().toJson(bd)
        }
}



data class FtDTO (
        val ver: String?,
        val sig: String?
)

data class HdDTO (
        val uid: String?,
        val tr: String?,
        val cmd: String?,
        val ctype: String?,
        val origin: String?,
        val destiny: String?,
        val seq: Long?,
        val seqt: Long?
)

