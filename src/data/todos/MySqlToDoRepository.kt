package com.dev.james.data.todos

import com.dev.james.database.DatabaseManager
import com.dev.james.entities.ToDo
import com.dev.james.entities.ToDoDraft
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class MySqlToDoRepository : ToDoRepository {

    private val database = DatabaseManager()
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
                database.getTodo(id , userId)?.let {
                    ToDo(
                        it.id,
                        it.title,
                        it.done ,
                        it.user_id
                    )
                }
            }

        return result.await()

        /*return database.getTodo(id , userId)?.let {
            ToDo(
                it.id,
                it.title,
                it.done ,
                it.user_id
            )
        }*/

    }

    override suspend fun addToDo(draft: ToDoDraft): ToDo {

            val todoDeferred = scope.async {
                database
                    .addTodo(draft)
            }

        return todoDeferred.await()
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