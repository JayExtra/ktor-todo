package com.dev.james.data.authentication

class InMemoryUserRepository : UserRepository {

    private val credentialsToUsers = mutableMapOf<String , UserRepository.User>(
        "admin:admin" to UserRepository.User("1", username = "admin" , salt = "abcd" , password = "123" , email = "admin"),
        "jay:69420" to UserRepository.User(userId = "2", email = "jay" , password = "123" , username = "jay" , salt = "efg")
    )
    override fun loginUser(email : String, password : String): UserRepository.User? {
       return credentialsToUsers["$email:$password"]
    }

    override fun signUpUser(user: UserRepository.User): Boolean {
        credentialsToUsers["${user.username}:$user.password"] = UserRepository.User("3" , username = user.username , salt = "hijk" , password = user.password , email = user.email)
        return credentialsToUsers.containsKey("${user.username}:$user.password")
    }
}