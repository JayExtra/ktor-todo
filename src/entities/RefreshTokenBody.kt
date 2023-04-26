package com.dev.james.entities

data class RefreshTokenBody(
    val email : String ,
    val refresh_token : String
)
