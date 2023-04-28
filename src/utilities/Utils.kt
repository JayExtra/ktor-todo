package com.dev.james.utilities

import java.security.SecureRandom

/*for practice purposes.
 *Find better way for creating secure random UUID
 */
fun generateRandomUid(len : Int) : String {
    val random = SecureRandom()
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray()
    return (1..len).map { chars[random.nextInt(chars.size)] }.joinToString("")
}

object Constants {
    /*
    * instead of thirty days , token expiry will be set for 5 min so just
    * to demonstrate token refresh endpoint
    * */
    const val THIRTY_DAYS_TIME = 300_000L
}