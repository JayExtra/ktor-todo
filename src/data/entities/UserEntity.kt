package com.dev.james.data.entities

data class UserEntity(
    val user_id: String ,
    val username: String ,
    val password: String ,
    val salt: String ,
    val email: String ,
    val refresh_token: String
)
