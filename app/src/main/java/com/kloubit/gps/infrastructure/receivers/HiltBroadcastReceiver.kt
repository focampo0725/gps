package com.kloubit.gps.infrastructure.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.annotation.CallSuper

/**
 * Update: According to the issue the problem should be fixed in version 2.29.1 of Dagger Hilt. So, just use version 2.29.1-alpha or above. Don't forget to update hilt-android-gradle-plugin version as well.

Original answer: There's a GitHub issue and a workaround. It seems that injection doesn't work because it actually happens inside onReceive() method of the generated parent class. The problem is that you cannot call the super method since it's abstract. But you can create a simple wrapper class that fixes the problem:
 */
abstract class HiltBroadcastReceiver : BroadcastReceiver() {
    @CallSuper
    override fun onReceive(context: Context, intent: Intent) {}
}