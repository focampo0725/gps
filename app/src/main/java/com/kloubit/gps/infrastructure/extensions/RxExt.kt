package com.kloubit.gps.infrastructure.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import androidx.room.RoomDatabase

import com.google.gson.Gson
import com.kloubit.gps.domain.dto.ApiErrorDTO
import com.kloubit.gps.domain.dto.ApiWebErrorDTO
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception
import java.util.*


fun <T> Flowable<T>.applySchedulers(): Flowable<T> = this
    .subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())

fun <T> Observable<T>.applySchedulers(): Observable<T> = this
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

fun <T> Single<T>.applySchedulersThread(): Single<T> = this
    .subscribeOn(Schedulers.io())

fun <T> Single<T>.applySchedulers(): Single<T> = this
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

fun <T> Single<T>.subscribeBy(
    onError: ((Throwable) -> Unit)? = null,
    onSuccess: (T) -> Unit
): Disposable = subscribe(onSuccess, { onError?.invoke(it) })

fun <T, K> Single<T>.subscribeBy(
    onError: ((K?) -> Unit)? = null,
    onSuccess: (T) -> Unit,
    errorClass : Class<K>
): Disposable = subscribe(onSuccess, { onError?.invoke(it.castError(errorClass)) })

//fun <T, K> Single<T>.subscribeAllEvents(
//        onError: ((K?, Throwable) -> Unit)? = null,
//        onSuccess: (T) -> Unit,
//        errorClass : Class<K>
//): Disposable = subscribe(onSuccess, { onError?.invoke(it.castError(errorClass), it) })

/**
 * extrae el mensaje de error de la request : ApiWebErrorDTO || Throwable || Default String
 */
fun <T> Single<T>.subscribeApp(
        onError: ((message : String) -> Unit)? = null,
        onSuccess: (T) -> Unit
): Disposable = subscribe(onSuccess, {
    onError?.invoke(it.castError(ApiWebErrorDTO::class.java)?.error ?: it.message ?: "empty error")
})

fun <T> Single<T>.toExecuteSequently(context: Context, timeInMs : Long = 10 * 1000
): T? {
    var response : T? = null
    this.subscribeApp(
            onSuccess = {
                response = it
            },
            onError = {
                this.logerror(it)
            })
//    SyncFunctionsUtils.startAutomaticSyncFunction(context, timeInMs) {response != null}
    return response
}



//mposRepository.mposApi.postLogIn(serializedControlCode = 0L, unitCode = 0, validatorCode = 0, driverCode = 0, driverPassword = 0, latitudeStart = 0F, longitudeStart = 0F, serializedTicketCode = 0, startDate = "").applySchedulers().subscribeAllEvents(
//onSuccess = {
//}, onError = { k, throwable ->
//    context.toast(k?.error ?:throwable.messageEmbebed())
//    state.value = ScreenState.ShowLoading(false)
//}, errorClass = MPOSErrorApiDTO::class.java
//).let {
//    compositeDisposable.add(it)
//}

fun <T>Throwable.castError(classOfT : Class<T>) : T?{
    if (this is com.jakewharton.retrofit2.adapter.rxjava2.HttpException)  // retrofit2 exception
    {
        try {
            return Gson().fromJson(
                this.response().errorBody()!!.charStream(),
                classOfT
            )
        }catch (e : Exception){
            e.printStackTrace()
        }
    }
    return null
}


fun Throwable.messageEmbebed() : String{
    if (this is com.jakewharton.retrofit2.adapter.rxjava2.HttpException)  // retrofit2 exception
    {
        val apiErrorDTO = Gson().fromJson(
            this.response().errorBody()!!.charStream(),
            ApiErrorDTO::class.java
        )
        return apiErrorDTO.message!!
    }

    this.message?.let {
        logerror(it)
    }
    return this.message!!
}


fun <T> Maybe<T>.applySchedulers(): Maybe<T> = this
    .subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())

operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
    add(disposable)
}

// string converters
fun String.timeStampToDate(): Date{
    val timeStamp = (this.toLong() * 1000)
    return Date(timeStamp)
}

//@SuppressLint("SimpleDateFormat")
//fun Date.toFormatString(formatString : String = "MM/dd/yyyy"): String
//{
//    val sdf = SimpleDateFormat(formatString)
//    return sdf.format(this)
//}

@SuppressLint("CheckResult")
class doAsynTask<T>( val job : () -> T, val response : (response : T) -> Unit, val error : ((throwable : Throwable) -> Unit)? = null)
{
    init {
        Single.fromCallable<T> {
            job()
        }.applySchedulers().subscribe({
            response(it)
        },{
            it.printStackTrace()
            error?.invoke(it)
        })
    }
}

@SuppressLint("CheckResult")
class doAsynTaskOptional<T>( val job : () -> T?, val response : (response : T?) -> Unit)
{
    init {
        Single.fromCallable<T> {
            job()
        }.applySchedulers().subscribe({
            response(it)
        },{
            it.printStackTrace()
        })
    }
}

@SuppressLint("CheckResult")
class doHandler<T>( val job : () -> T, val delayMilis : Long)
{
    init {
        Handler().postDelayed({ job() }, delayMilis)
    }
}

@SuppressLint("CheckResult")
class doTaskInFirstThread(val action : () -> Unit)
{
    init {
        Single.fromCallable {
        }.applySchedulers().subscribe({
            action()
        },{
            it.printStackTrace()
        })
    }
}



/**
 * importante tener en cuenta q no se estàn manejando las excepciones desde aquì..
 */
@SuppressLint("CheckResult")
class doAsync<T>( val job : () -> T)
{
    init {
        try
        {
            Single.fromCallable<T> {
                job()
            }.applySchedulersThread().subscribe()
        }catch (e : Exception){
            e.printStackTrace()
        }
    }
}

@SuppressLint("CheckResult")
fun <T> RoomDatabase.runTransactionInBackground(job : () -> T, response: (response: T) -> Unit, error: (throwable : Throwable) -> Unit) {
    Single.fromCallable<T> {
        this.runInTransaction(job)
    }.applySchedulers().subscribe({
        response(it)
    }, {
        error(it)
    })
}



//it.runInTransaction(Callable<Boolean> {
//    //                        update of session : status operating, startConfirmationDate , driver box code, ticketTransactionNumber(ticketTransactionLast), driverName
//    //                        update of vehicle : ticketTransactionLast, serializedControlCode, serializedTicketCode, driverBoxCode, routeName*, sideName*,routeTypeCode*
//    sessionDao.updateStartSession(
//        id = session.id,
//        driverBoxCode = sessionApiDTO.session.driverBoxCode,
//        driverName = sessionApiDTO.driver.name,
//        ticketTransactionNumber = sessionApiDTO.session.lastSQLTicketTransaction
//    )
//    vehicleDao.updateSessionedVehicle(
//        ticketTransactionLast = sessionApiDTO.session.lastSQLTicketTransaction,
//        serializedTicketCode = sessionApiDTO.tickets.code,
//        driverBoxCode = sessionApiDTO.session.driverBoxCode,
//        serializedControlCode = sessionApiDTO.controls.code,
//        sideName = sessionApiDTO.route.sideName,
//        routeTypeCode = sessionApiDTO.route.routeTypeCode,
//        routeName = sessionApiDTO.route.routeName
//    )
////                    val vehicle = vehicleDao.get()
////                    vehicle?.let {
////                        throw Exception("test eror..")
////                    }
//    true
//})