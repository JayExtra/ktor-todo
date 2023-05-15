package com.dev.james.domain.models

data class SaltedHash(
    val hash : String ,
    val salt : String
)
