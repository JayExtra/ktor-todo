package com.dev.james.data.authentication

interface UserRepository {

    suspend fun loginUser(email : String, password : String) : User?

    suspend fun signUpUser(user : UserRepository.User) : Boolean

    suspend fun getUser(email : String) : User?

    data class User(
        val userId : String ,
        val email : String ,
        val username : String ,
        val salt : String ,
        val password: String ,
        val refreshToken : String
    )
}