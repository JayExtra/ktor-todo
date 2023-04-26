package com.dev.james.database

import com.dev.james.data.authentication.UserRepository
import com.dev.james.entities.ToDo
import com.dev.james.entities.ToDoDraft
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.entity.filter
import org.ktorm.entity.firstOrNull
import org.ktorm.entity.sequenceOf
import org.ktorm.entity.toList

class DatabaseManager {

    private val hostname = "localhost"
    private val port = "3306"
    private val databaseName = "todos_ktor"
    private val username = "root"
    private val password = ""

    //  database
    private val ktormDatabase: Database

    init {
        val jdbcUrl = "jdbc:mysql://$hostname:3306/$databaseName"
        ktormDatabase = Database.connect(jdbcUrl, user = username, password = password)
    }

    //user

    fun addUser(user : UserRepository.User) : Boolean {

        val insertionProcess = ktormDatabase.insert(DBUserTable){
            set(DBUserTable.user_id, user.userId )
            set(DBUserTable.username , user.username)
            set(DBUserTable.password , user.password)
            set(DBUserTable.salt , user.salt)
            set(DBUserTable.email , user.email)
            set(DBUserTable.refreshToken , user.refreshToken)
        }

        return insertionProcess > 0
    }

    fun getUser(email : String) : DBUserEntity? {
        return ktormDatabase.sequenceOf(DBUserTable)
            .firstOrNull{ it.email eq email }
    }


    //todos
    fun getAllToDosByUserId(userId : String): List<DBTodoEntity> {
        return ktormDatabase.sequenceOf(
            DBTodoTable
        ).filter { it.user_id eq userId }.toList()
    }

    fun getTodo(id: Int , userId: String): DBTodoEntity? {
        return ktormDatabase.sequenceOf(DBTodoTable)
            .filter { it.user_id eq userId }
            .firstOrNull { it.id eq id }
    }

    fun addTodo(draft: ToDoDraft): ToDo {
        val insertedId = ktormDatabase.insertAndGenerateKey(DBTodoTable) {
            set(DBTodoTable.title, draft.title)
            set(DBTodoTable.done, draft.done)
            set(DBTodoTable.user_id , draft.user_id)
        } as Int

        return ToDo(insertedId, draft.title, draft.done , draft.user_id)
    }

    fun updateTodo(id: Int, draft: ToDoDraft): Boolean {
        val updatedRows = ktormDatabase.update(DBTodoTable) {
            set(DBTodoTable.title, draft.title)
            set(DBTodoTable.done, draft.done)
            where {
                it.user_id eq draft.user_id
                it.id eq id
            }
        }

        return updatedRows > 0
    }

    fun removeToDo(id: Int , userId: String): Boolean {
        val deletedRows = ktormDatabase.delete(DBTodoTable) {
            it.user_id eq userId
            it.id eq id
        }

        return deletedRows > 0
    }


}