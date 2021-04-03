package com.example.lostandfoundipb.Utils

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
