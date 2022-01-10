package com.tuwaiq.halfway.utility

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
import java.util.regex.Matcher
import java.util.regex.Pattern

object Common {

    fun showNoInternetMessage(activity: Context) {
        val builder = android.app.AlertDialog.Builder(activity)
        builder.setMessage("Please check your internet connection.")
        builder.setPositiveButton("Ok") { dialog, id -> }.show()

    }

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun startNewActivity(
        activity: Activity,
        className: Class<*>,
        clearStack: Boolean
    ) {
        val intent = Intent(activity, className)
        if (clearStack)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        activity.startActivity(intent)
    }

    fun startNewActivity(
        activity: Activity,
        className: Class<*>,
        bundle: Bundle,
        clearStack: Boolean
    ) {
        val intent = Intent(activity, className)
        intent.putExtras(bundle)
        if (clearStack)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        activity.startActivity(intent)
    }

    fun showToast(activity: Activity, message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    fun isNameFormat(name: String?) =
        (name.isNullOrBlank() || name.length > 25).not()

    fun isValidMobile(phone: String?) =
        (phone.isNullOrBlank() || phone.length != 10).not() && Patterns.PHONE.matcher(
            phone
        ).matches()

    fun isValidPassword(password: String?): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$"
        pattern = Pattern.compile(PASSWORD_PATTERN)
        matcher = pattern.matcher(password)
        return matcher.matches()
    }

    fun isValidEmail(target: CharSequence?) =
        target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()

    fun midPoint(lat1: Double, lon1: Double, lat2: Double, lon2: Double): LatLng {
        var lat1 = lat1
        var lon1 = lon1
        var lat2 = lat2
        val dLon = Math.toRadians(lon2 - lon1)

        //convert to radians
        lat1 = Math.toRadians(lat1)
        lat2 = Math.toRadians(lat2)
        lon1 = Math.toRadians(lon1)
        val Bx = Math.cos(lat2) * Math.cos(dLon)
        val By = Math.cos(lat2) * Math.sin(dLon)
        val lat3 = Math.atan2(
            Math.sin(lat1) + Math.sin(lat2),
            Math.sqrt((Math.cos(lat1) + Bx) * (Math.cos(lat1) + Bx) + By * By)
        )
        val lon3 = lon1 + Math.atan2(By, Math.cos(lat1) + Bx)
//        println("===*****====> " +Math.toDegrees(lat3) + " " + Math.toDegrees(lon3))
        //print out in degrees
        return LatLng(Math.toDegrees(lat3),Math.toDegrees(lon3))

    }

    fun midPointString(lat1: Double, lon1: Double, lat2: Double, lon2: Double): String {
        var lat1 = lat1
        var lon1 = lon1
        var lat2 = lat2
        val dLon = Math.toRadians(lon2 - lon1)

        //convert to radians
        lat1 = Math.toRadians(lat1)
        lat2 = Math.toRadians(lat2)
        lon1 = Math.toRadians(lon1)
        val Bx = Math.cos(lat2) * Math.cos(dLon)
        val By = Math.cos(lat2) * Math.sin(dLon)
        val lat3 = Math.atan2(
            Math.sin(lat1) + Math.sin(lat2),
            Math.sqrt((Math.cos(lat1) + Bx) * (Math.cos(lat1) + Bx) + By * By)
        )
        val lon3 = lon1 + Math.atan2(By, Math.cos(lat1) + Bx)

        //print out in degrees
        return "$lat3,$lon3"
//        println(.toString() + " " + Math.toDegrees(lon3))
    }
}