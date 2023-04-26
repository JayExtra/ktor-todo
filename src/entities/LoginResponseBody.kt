package com.dev.james.entities

import com.dev.james.data.authentication.UserRepository

data class LoginResponseBody(
    val token : String ,
    val refresh_token : String ,
    val user : UserDetails
)
