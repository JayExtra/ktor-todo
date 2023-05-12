package com.dev.james.data.authentication

import com.dev.james.database.DatabaseManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class MySqlUserRepository() : UserRepository {

    private val database = DatabaseManager()
    private val scope = CoroutineScope(Dispatchers.IO)

    override suspend fun loginUser(email: String, password: String): UserRepository.User? {

        val userDeferred = scope.async {database.getUser(email)}

        userDeferred.invokeOnCompletion {
            println(it?.message)
        }

        val user = userDeferred.await()

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

    override suspend fun signUpUser(user: UserRepository.User): Boolean {
        val isAddedDeferred = scope.async { database.addUser(user) }

        isAddedDeferred.invokeOnCompletion {
            println(it?.message)
        }

        return isAddedDeferred.await()
    }

    override suspend fun getUser(email: String): UserRepository.User? {

        val userDeferred = scope.async { database.getUser(email = email) }

        userDeferred.invokeOnCompletion {
            println(it?.message)
        }

        val user = userDeferred.await()

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