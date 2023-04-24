package com.dev.james.data.authentication.security

data class SaltedHash(
    val hash : String ,
    val salt : String
)
