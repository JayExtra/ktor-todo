package com.dev.james.domain.database

import com.dev.james.domain.repository.UserRepository
import com.dev.james.data.database.ktorm_database.tables.DBTodoEntity
import com.dev.james.data.database.ktorm_database.tables.DBUserEntity
import com.dev.james.data.entities.TodoEntity
import com.dev.james.data.entities.UserEntity
import com.dev.james.domain.models.ToDo
import com.dev.james.domain.models.ToDoDraft


interface DatabaseManager {

    fun addUser(user : UserRepository.User) : Boolean

    fun getUser(email : String) : UserEntity?

    fun getAllToDosByUserId(userId : String): List<DBTodoEntity>

    fun getTodo(id: Int , userId: String): TodoEntity?

    fun addTodo(draft: ToDoDraft): TodoEntity

    fun updateTodo(id: Int, draft: ToDoDraft): Boolean

    fun removeToDo(id: Int , userId: String): Boolean

}