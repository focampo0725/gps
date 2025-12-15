package com.kloubit.gps.data.spf

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

interface ISharedPreferences{
    var sharedPreferences : SharedPreferences

    var isPersonnelIdentification : Boolean
    var isGrantedPermissions : Boolean
    var isDeviceConfigured : Boolean
    var isDeviceSynchronization  : Boolean

    var jwt : String



}


@SuppressLint("ApplySharedPref")
class SharedPreferencesImpl constructor(private var context: Context, var spf: SharedPreferences) : ISharedPreferences {

    override var sharedPreferences: SharedPreferences
        get() = spf
        set(value) {}

    override var isPersonnelIdentification: Boolean
        get() = spf.getBoolean(KEY_IS_PERSONNEL_IDENTIFICATION, false)
        set(value) {
            spf.edit().apply{
                putBoolean(KEY_IS_PERSONNEL_IDENTIFICATION, value)
                commit()
            }
        }



    override var isDeviceSynchronization: Boolean
        get() = spf.getBoolean(KEY_IS_SYNCHRONIZATION, false)
        set(value) {
            spf.edit().apply{
                putBoolean(KEY_IS_SYNCHRONIZATION, value)
                commit()
            }
        }

    override
    var isGrantedPermissions: Boolean
        get() = spf.getBoolean(KEY_IS_GRANTED_PERMISSIONS, false)
        set(value) {
            spf.edit().apply{
                putBoolean(KEY_IS_GRANTED_PERMISSIONS, value)
                commit()
            }
        }
    override
    var isDeviceConfigured: Boolean
        get() = spf.getBoolean(KEY_IS_DEVICE_CONFIGURED, false)
        set(value) {
            spf.edit().apply{
                putBoolean(KEY_IS_DEVICE_CONFIGURED, value)
                commit()
            }
        }
    override
    var jwt: String
        get() = spf.getString(KEY_JWT, "") ?: ""
        set(value) {
            spf.edit().apply{
                putString(KEY_JWT, value)
                commit()
            }
        }




    companion object {

        private const val KEY_IS_GRANTED_PERMISSIONS = "KEY_IS_GRANTED_PERMISSIONS"
        private const val KEY_IS_DEVICE_CONFIGURED = "KEY_IS_DEVICE_CONFIGURED"
        private const val KEY_JWT = "KEY_JWT"
        private const val KEY_IS_SYNCHRONIZATION = "KEY_IS_SYNCHRONIZATION"
        private const val KEY_IS_PERSONNEL_IDENTIFICATION = "KEY_IS_PERSONNEL_IDENTIFICATION"


    }
}
