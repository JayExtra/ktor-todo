package com.dev.james.domain.models

data class LoginResponseBody(
    val token : String ,
    val refresh_token : String ,
    val user : UserDetails
)
