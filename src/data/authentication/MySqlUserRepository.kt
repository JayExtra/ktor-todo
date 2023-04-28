package com.dev.james.data.authentication

import com.dev.james.data.authentication.security.HashingService
import com.dev.james.database.DatabaseManager
import java.security.SecureRandom

class MySqlUserRepository() : UserRepository {

    private val database = DatabaseManager()
    override fun loginUser(email: String, password: String): UserRepository.User? {
        val user = database.getUser(email)
        return if (user == null) {
            null
        } else {
            UserRepository.User(
                userId = user.user_id,
                username = user.username,
                salt = user.salt,
                password = user.password,
                email = user.email,
                refreshToken = user.refresh_token
            )
        }
    }

    override fun signUpUser(user: UserRepository.User): Boolean {
        return database.addUser(user)
    }

    override fun getUser(email: String): UserRepository.User? {
        val user = database.getUser(email = email)

        return if (user == null) {
            null
        } else {
            UserRepository.User(
                username = user.username,
                userId = user.user_id,
                password = user.password,
                email = user.email,
                salt = user.salt,
                refreshToken = user.refresh_token
            )
        }
    }
}