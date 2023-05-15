package com.dev.james.data.repository_impl

import com.dev.james.data.database.ktorm_database.KTORMDatabaseManager
import com.dev.james.data.mappers.mapToToDoDomainObject
import com.dev.james.domain.models.ToDo
import com.dev.james.domain.models.ToDoDraft
import com.dev.james.domain.repository.ToDoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class MySqlToDoRepository : ToDoRepository {

    private val database = KTORMDatabaseManager()
    private val scope = CoroutineScope(Dispatchers.IO)

    override suspend fun getAllTodos(userId : String): List<ToDo> {

        val dataResponse = scope.async {
            database.getAllToDosByUserId(userId)
                .map { ToDo(it.id, it.title, it.done , it.user_id) }
        }

        return dataResponse.await()
    }

    override suspend fun getToDo(id: Int, userId: String): ToDo? {

            val result = scope.async {
                database.getTodo(id , userId)?.mapToToDoDomainObject()
            }

        return result.await()

    }

    override suspend fun addToDo(draft: ToDoDraft): ToDo {

            val todoDeferred = scope.async {
                database
                    .addTodo(draft)
            }

        return todoDeferred.await().mapToToDoDomainObject()
    }

    override suspend fun removeToDo(id: Int, userId: String): Boolean {
            val isRemovedDeferred = scope.async {
                database.removeToDo(id , userId)
            }
        return isRemovedDeferred.await()
    }

    override suspend fun updateToDo(id: Int, draft: ToDoDraft): Boolean {
            val isUpdated = scope.async {
                database.updateTodo(
                    id,
                    draft
                )
            }
        return isUpdated.await()
    }
}