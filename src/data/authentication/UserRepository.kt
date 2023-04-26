package com.dev.james.data.authentication

interface UserRepository {

    fun loginUser(email : String, password : String) : User?

    fun signUpUser(user : UserRepository.User) : Boolean

    fun getUser(email : String) : User?

    data class User(
        val userId : String ,
        val email : String ,
        val username : String ,
        val salt : String ,
        val password: String ,
        val refreshToken : String
    )
}