package com.dev.james.data.authentication

class InMemoryUserRepository : UserRepository {

    private val credentialsToUsers = mapOf<String , UserRepository.User>(
        "admin:admin" to UserRepository.User(1, "admin"),
        "jay:69420" to UserRepository.User(2, "jay")
    )
    override fun loginUser(username : String , password : String): UserRepository.User? {
       return credentialsToUsers["$username:$password"]
    }
}