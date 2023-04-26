package com.dev.james.utilities

import java.security.SecureRandom

fun generateRandomUid(len : Int) : String {
    /*for practice purposes , not secure method. Find better way for creating secure random UUID*/
    val random = SecureRandom()
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray()
    return (1..len).map { chars[random.nextInt(chars.size)] }.joinToString("")
}

object Constants {
    //const val THIRTY_DAYS_TIME = 2_592_000_000L
    const val THIRTY_DAYS_TIME = 60_000L
}