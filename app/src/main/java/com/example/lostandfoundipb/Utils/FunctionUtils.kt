package com.example.lostandfoundipb.Utils

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.DateUtils
import com.example.lostandfoundipb.R
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

fun  passwordValidator(password: String): Boolean{
    return password.length >= 6
}

fun telephoneValidator(phone: String): Boolean{
    val phoneRegex = Pattern.compile(
            "08[0-9]{7,11}"
    )
    return phone.length >= 10 && phoneRegex.matcher(phone).matches()
}

fun emailValidator(email: String?): Boolean {
    val expression = "(?im)^(?=.{1,64}@)(?:(\"[^\"\\\\]*(?:\\\\.[^\"\\\\]*)*\"@)|((?:[0-9a-z](?:\\.(?!\\.)|[-!#\\$%&'\\*\\+/=\\?\\^`\\{\\}\\|~\\w])*)?[0-9a-z]@))(?=.{1,255}$)(?:(\\[(?:\\d{1,3}\\.){3}\\d{1,3}\\])|((?:(?=.{1,63}\\.)[0-9a-z][-\\w]*[0-9a-z]*\\.)+[a-z0-9][\\-a-z0-9]{0,22}[a-z0-9])|((?=.{1,63}$)[0-9a-z][-\\w]*))$"
    val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
    val matcher: Matcher = pattern.matcher(email)
    return matcher.matches()
}

@SuppressLint("SimpleDateFormat")
fun formatDate(date: String?, context: Context?): String{
    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    val dateParsed= dateFormat.parse(date.toString())
    val dayOfWeek = dateParsed.day
    var dayOfWeekStr = ""
    when (dayOfWeek) {
        0 -> { dayOfWeekStr = context!!.getString(R.string.sun) }
        1 -> { dayOfWeekStr = context!!.getString(R.string.mon) }
        2 -> { dayOfWeekStr = context!!.getString(R.string.tue) }
        3 -> { dayOfWeekStr = context!!.getString(R.string.wed) }
        4 -> { dayOfWeekStr = context!!.getString(R.string.thu) }
        5 -> { dayOfWeekStr = context!!.getString(R.string.fri) }
        6 -> { dayOfWeekStr = context!!.getString(R.string.sat) }
    }
    return dayOfWeekStr+", "+ SimpleDateFormat("dd MMM yyyy").format(dateFormat.parse(date.toString()) as Date)
}

fun relativeTime(date: String): String {
    val dateFormat = SimpleDateFormat ("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    dateFormat.timeZone = TimeZone.getTimeZone("GMT")
    val dateParsed = dateFormat.parse(date).time
    val dateCurrent = System.currentTimeMillis()

    return DateUtils.getRelativeTimeSpanString(dateParsed,dateCurrent,DateUtils.MINUTE_IN_MILLIS).toString()
}