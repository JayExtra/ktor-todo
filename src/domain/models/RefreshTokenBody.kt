package com.dev.james.domain.models

data class RefreshTokenBody(
    val email : String ,
    val refresh_token : String
)
