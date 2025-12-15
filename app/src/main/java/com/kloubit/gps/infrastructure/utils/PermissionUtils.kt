package com.kloubit.gps.infrastructure.utils

import android.Manifest
import android.os.Build
import com.abx.shared.supportabx.utils.PatchDeviceUtils
import com.kloubit.gps.infrastructure.extensions.GetBuildModel
import java.util.*

class PermissionUtils {
    companion object{

        /**
         * todos los permisos que usará esta app
         * (Omitiendo los permisos que se tienen que otorgar manualmente)
         */
        fun getPermissions(): ArrayList<String> {
            val permissions = ArrayList<String>()

            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)

            PatchDeviceUtils.requestBackgroundLocationPermissionIfVersionAllowed(GetBuildModel()){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    permissions.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) permissions.add(
                Manifest.permission.ANSWER_PHONE_CALLS
            )
            return permissions
        }
    }
}