package com.dev.james.data.authentication

interface UserRepository {

    fun loginUser(email : String, password : String) : User?

    fun signUpUser(user : UserRepository.User) : Boolean

    data class User(
        val userId : String ,
        val email : String ,
        val username : String ,
        val salt : String ,
        val password: String
    )
}