package com.kloubit.gps.infrastructure.clients


import android.content.Context
import com.kloubit.gps.data.repository.AppRepository
import com.kloubit.gps.infrastructure.stateful.AppState


/**
 * este client tendrà la tarea de iniciar un seguimiento a distintos mutable live datas para setear
 * en el appstate el nuevo valor del objeto compartido en cuestiòn.. (ex : Vehicle)
 *
 * Nota: Està siendo llamado desde un ForegroundService porque se irà ejecutando a lo largo de la vida de la app..
 */
class ClientObserver(
    val context: Context,
    val appState: AppState,
    val appRepository: AppRepository,

    ) : IClient {





    override fun onStart(context: Context) {


    }

    override fun onDestroy() {

    }


}