package com.dev.james.data.authentication

interface UserRepository {

    fun loginUser(username : String , password : String) : User?

    data class User(
        val userId : Int ,
        val username : String
    )
}