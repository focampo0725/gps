package com.kloubit.gps.infrastructure.extensions

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Point
import android.os.*
import android.text.Editable
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.lifecycle.MutableLiveData
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.kloubit.gps.BuildConfig
import com.marcoscg.materialtoast.MaterialToast
import java.lang.Exception
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*

fun GetBuildModel() = Build.MODEL.replace(" ", "")
inline fun <reified T> T.logi(message: String) = Log.i(T::class.java.simpleName, message)
inline fun <reified T> T.logd(message: String) = Log.d(T::class.java.simpleName, message)
inline fun <reified T> T.logw(message: String) = Log.w(T::class.java.simpleName, message)
inline fun <reified T> T.logerror(message: String) = Log.e(T::class.java.simpleName, message)
inline fun <reified T> T.isUIThread() = Looper.myLooper() == Looper.getMainLooper()
inline fun <reified T> T.randomBetweenRange(min : Int, max : Int) : Int = Random().nextInt(max - min) + min


// imprime solo si estuviera en modo debug
inline fun <reified T> T.logIfDebug(message: String) {
    if (BuildConfig.DEBUG)Log.i(T::class.java.simpleName, message)
    return
}


// convierte de horas a milisegundos
fun Int.hourToMiliseconds() : Long{
    return (this.toLong() * 60 * 1 * 60 * 1000)
}

fun Int.minutesToMiliseconds() : Long{
    return (this.toLong() * 1 * 60 * 1000)
}

fun Double.minutesToMiliseconds() : Long{
    return (this * 1 * 60 * 1000).toLong()
}
fun Long.secondsToMiliseconds() : Long{
    return (this *  1000)
}

fun Float.minutesToMiliseconds() : Long{
    return (this * 1 * 60 * 1000).toLong()
}

fun Long.milisecondsToSeconds() : Long{
    return (this /  1000)
}

/**
 * convert miliseconds to Date
 */
fun Long.toDate() : Date?{
    var date : Date? = null
    try {
        date = Date(this)
    }catch (e : Exception){
        this.logerror(e.message ?: "")
        e.printStackTrace()
    }
    return date
}

//inline fun <reified T> T.fromArrayJson(json : String, classOfT : Class<Array<T>>) : Array<T>? = GsonBuilder().create().fromJson(json, classOfT)
//
//fun <T> Gson.fromArrayJson(json : String, classOfT : Class<T>) : List<T>{
//    val myType = object : TypeToken<List<T>>() {}.type
//    return Gson().fromJson<List<T>>(json, myType)
//}

//inline fun <reified T> T.fromArrayJson(json : String, classOfT : Class<T>) : Array<T>? = GsonBuilder().create().fromJson(json, Array<T>::class.java)

//inline fun <reified T> fromArrayJson(s: String, clazz: Class<Array<T>>): MutableList<Array<T>> = mutableListOf((Gson().fromJson(s, clazz)))

inline fun <reified T> T.GetBuildModel() = Build.MODEL.replace(" ", "")

fun <T> String.fromArrayJson(clazz: Class<Array<T>>): MutableList<Array<T>> {
    val arr: Array<T> = Gson().fromJson(this, clazz)
    return mutableListOf(arr) //or return Arrays.asList(new Gson().fromJson(s, clazz)); for a one-liner
}

fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

@SuppressLint("MissingPermission")
fun Context.vibrate(time : Long = 500) {
    val vibrator: Vibrator? = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
    vibrator?.let {
        if (Build.VERSION.SDK_INT >= 26) {
            it.vibrate(VibrationEffect.createOneShot(time, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            it.vibrate(time)
        }
    }
}

fun Boolean.toInt() : Int{
    return if (this) 1 else 0
}

fun String?.toStringOptional() : String{
    if(this == null)
        return "-"
    return this
}

fun String.captureLettersWithRegex() : String?{
    return Regex("([a-z]+)").find(this)?.groupValues?.firstOrNull()
}

fun String.captureOnlyDigitsWithRegex() : String?{
    return Regex("(\\d+)").find(this)?.groupValues?.firstOrNull()
}

//val FORMAT_STRING = "yyyy-MM-dd HH:mm:ss"


//fun Long.toStringFormat(format : String = "yyyy-MM-dd HH:mm:ss") : String{
//    return SimpleDateFormat(format, Locale.getDefault()).format(this)
//}

fun Date.toStringFormat(format : String = "yyyy-MM-dd HH:mm:ss") : String{
    return SimpleDateFormat(format, Locale.getDefault()).format(this)
}

// compara la fecha de la instancia Date con la fecha del momento (Date.now())
fun Date.toCompareWithDateNowInMiliSeconds() : Long {
    return Date().time - this.time
//    val diffInSec: Long = TimeUnit.MILLISECONDS.toSeconds((diffInMs))
}

fun Context.toast(text: String, length: Int = Toast.LENGTH_SHORT) {
//    Toast.makeText(this, text, length).show()
    MaterialToast.makeText(this, text, length).show();
}

fun Context.toastIfDebug(text: String, length: Int = Toast.LENGTH_SHORT) {
    MaterialToast.makeText(this, text, length).show();
}

fun <T : Parcelable> Activity.extra(key: String, default: T? = null): Lazy<T> = lazy { intent?.extras?.getParcelable<T>(key) ?: default ?: throw Error("No value $key in extras") }

inline fun <reified T : Activity> Context.getIntent() = Intent(this, T::class.java)


fun Double.toRound(scale : Int): BigDecimal{
    return BigDecimal(this).setScale(scale, RoundingMode.HALF_EVEN)
}

fun String.containsWithLowerCase(comparableText : String) : Boolean{
    return (this.toLowerCase().contains(comparableText.toLowerCase()))
}


// TODO : ----------------------------------------------------------------
// TODO : DIALOG ----------------
fun Dialog.showStateExpanded() {
    this.setOnShowListener{ dialogInterface ->
        val d = dialogInterface as BottomSheetDialog
        val bottomSheet = d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
        // Right here!
        BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED)
    }
}

//todo comprobar si es necesario recurrir a estas funciones para llevar el punto pivot al centro del view..
fun Int.locationWidgetScreenX(view : View) : Int{
    val x = this + view.width / 2
    return x
}

fun Int.randomBetweenRange(min : Int, max : Int) : Int{
    return Random().nextInt(max - min) + min
}


fun Int.locationWidgetScreenY(view : View) : Int{
    val y = this + view.height / 2
    return y
}


fun View.locationScreenX(isAddWidth : Boolean = false) : Float{
    val location = IntArray(2)
    this.getLocationOnScreen(location)
    var x = 0f
    if(isAddWidth)
        x = location[0].toFloat() + this.width / 2
    else
        x = location[0].toFloat() - this.width / 2
    return x
}


fun View.locationScreenY(isAddWidth : Boolean = false) : Float{
    val location = IntArray(2)
    this.getLocationOnScreen(location)
    var y = 0f
    if(isAddWidth)
        y = location[1].toFloat() + this.height / 2
    else
        y = location[1].toFloat() - this.height / 2
    return y
}

// TODO : ----------------------------------------------------------------
// TODO : View ----------------
fun View.loadAnimScale (isIncrementScale : Boolean, duration : Long = 300) {
    this.visibility = View.VISIBLE
//    val listScalePoint : Array<Float> = if (isIncrementScale) arrayOf(0f, 0.7f, 0.6f, 1f) else arrayOf(1f, 0f)
    if (isIncrementScale) {
        val scaleDown: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(
            this,
            PropertyValuesHolder.ofFloat("scaleX", 0f, 1.4f, 1f),
            PropertyValuesHolder.ofFloat("scaleY", 0f, 1.4f, 1f)
        )
        scaleDown.duration = duration
        scaleDown.start()
    } else {
        val scaleDown: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(
            this,
            PropertyValuesHolder.ofFloat("scaleX", 1f, 1.4f, 0f),
            PropertyValuesHolder.ofFloat("scaleY", 1f, 1.4f, 0f)
        )
        scaleDown.duration = duration
        scaleDown.start()
        scaleDown.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                TODO("Not yet implemented")
            }

            override fun onAnimationEnd(animation: Animator) {
                visibility = View.GONE
            }

            override fun onAnimationCancel(animation: Animator) {
                TODO("Not yet implemented")
            }

            override fun onAnimationRepeat(animation: Animator) {
                TODO("Not yet implemented")
            }
        })
    }
}


// TODO : ----------------------------------------------------------------
// TODO : INT ----------------
fun Int.colorDrawabletoColorStatelist (context: Context) : ColorStateList{
    val colorInt = context.resources.getColor(this)
    val colorStateList = ColorStateList.valueOf(colorInt)
    return colorStateList
}

// TODO : ----------------------------------------------------------------
// TODO : ACTIVITY ----------------
fun Activity.hideKeyboard (){
    val view = this.currentFocus
    view?.let { v ->
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(v.windowToken, 0)
    }
}



// TODO : ----------------------------------------------------------------
// TODO : ANIMATOR ----------------
fun Animator.addListenerEnd(onAnimationEnd : () -> Unit){
    this.addListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animator: Animator) {}
        override fun onAnimationEnd(animator: Animator) {
            onAnimationEnd()
        }
        override fun onAnimationCancel(animator: Animator) {}
        override fun onAnimationRepeat(animator: Animator) {}
    })
}


// TODO : ----------------------------------------------------------------
// TODO : WINDOW MANAGER ----------------
/**
 * retorna la mitad del ancho de la pantalla
 */
fun WindowManager.defaultDisplayMiddle() : Float{
    val display = this.defaultDisplay
    val point = Point()
    display.getSize(point)
    val width = point.x // screen width
    return width / 2.0f // half the width or to any value required,global to class
}
// TODO : ----------------------------------------------------------------
// TODO : IMAGEVIEW ----------------
fun TextView.textLimitCharacters(text : String, limit : Int = 25){
    var text : String = text
    if(text.length > limit)
        text = (text.substring(0, limit) + "...")
    this.text = text
}

// TODO : ----------------------------------------------------------------
// TODO : IMAGEVIEW ----------------
fun String.textLimitCharacters(limit : Int = 25) : String{
    var reduced = this
    if(this.length > limit)
        reduced = (this.substring(0, limit))
    return reduced
}

fun AutoCompleteTextView.textEditable(text : String){
    this.text = Editable.Factory.getInstance().newEditable(text)
}

// TODO : ----------------------------------------------------------------
fun Window.inmerviseScreen(){
    this.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
    )
    this.addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    this.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                    or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
}

/**
 * post with delay
 */
fun <T> MutableLiveData<T>.postDelay(value : T, delay : Long = 500){
    doTaskInFirstThread{
        Handler().postDelayed({
            this.postValue(value)
        }, delay)
    }
}


/**
 * llama al receiver customizado
 */
fun Context.sendBroadcastAllVersions(action : String = "com.abexa.action.ACTION_NAME", packageName : String = "com.abexa.mpos", receiverClass : String = "com.abexa.mpos.domain.receivers.BootReceiver"){
    this.sendBroadcast(Intent().apply {
        this.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
        this.action = action

        // android >= 8
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            this.component = ComponentName(packageName, receiverClass )
    })    // llamando al receptor propietario..
}

fun Context.pxToDp(px: Int): Int {
    return (px / resources.displayMetrics.density).toInt()
}

fun Context.dpToPx(dp: Int): Int {
    return (dp * resources.displayMetrics.density).toInt()
}

//@SuppressLint("ClickableViewAccessibility")
//fun ImageView.toListenerClickWithDraggable(context: Context, pressColor : Int = Color.WHITE, defaultColor : Int  = Color.GRAY, onClickClosure : (() -> Unit)? = null, onDoubleClickClosure : (() -> Unit)? = null){
//    val detector = GestureDetector(context, GestureTap(context, onClickBubble = onClickClosure, onDoubleClickBubble = onDoubleClickClosure,
//            onDownState = {
//                this.setColorFilter(pressColor);
//            }))
//
//    this.setOnTouchListener { v, motionEvent -> // TODO Auto-generated method stub
//        detector.onTouchEvent(motionEvent)
//        if (motionEvent.action == MotionEvent.ACTION_UP || motionEvent.action == MotionEvent.ACTION_CANCEL) {
//            this.setColorFilter(defaultColor);
//        }
//        true
//    }
//}

/**
 * actualiza los màrgenes dinàmicamente..
 * considerar enviar los pixeles
 */
fun View.setMarginDinamically(   left: Int = 0,
                                       top: Int = 0,
                                       right: Int = 0,
                                       bottom: Int = 0){
    if (this.layoutParams is ViewGroup.MarginLayoutParams) {
        val p = this.layoutParams as ViewGroup.MarginLayoutParams
        p.setMargins(left, top, right, bottom)
        this.requestLayout()
    }
}


fun ImageView.toggleColorIndicator(isDangerColor : Boolean = false){
    this.setColorFilter(if(isDangerColor) Color.RED else Color.WHITE)
}

