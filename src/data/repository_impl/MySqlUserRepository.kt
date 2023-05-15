package com.dev.james.data.repository_impl

import com.dev.james.data.database.ktorm_database.KTORMDatabaseManager
import com.dev.james.data.mappers.mapToUserDomainObject
import com.dev.james.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class MySqlUserRepository() : UserRepository {

    private val database = KTORMDatabaseManager()
    private val scope = CoroutineScope(Dispatchers.IO)

    override suspend fun loginUser(email: String, password: String): UserRepository.User? {

        val userDeferred = scope.async { database.getUser(email) }

        userDeferred.invokeOnCompletion {
            println(it?.message)
        }

        return userDeferred.await()?.mapToUserDomainObject()
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

        return userDeferred.await()?.mapToUserDomainObject()
    }
}