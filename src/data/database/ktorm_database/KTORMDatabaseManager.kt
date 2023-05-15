package com.dev.james.data.database.ktorm_database

import com.dev.james.domain.repository.UserRepository
import com.dev.james.data.database.ktorm_database.tables.DBTodoEntity
import com.dev.james.data.database.ktorm_database.tables.DBTodoTable
import com.dev.james.data.database.ktorm_database.tables.DBUserTable
import com.dev.james.data.entities.TodoEntity
import com.dev.james.data.entities.UserEntity
import com.dev.james.domain.database.DatabaseManager
import com.dev.james.domain.models.ToDoDraft
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.entity.filter
import org.ktorm.entity.firstOrNull
import org.ktorm.entity.sequenceOf
import org.ktorm.entity.toList

class KTORMDatabaseManager : DatabaseManager {

    private val hostname = "localhost"
    private val port = "3306"
    private val databaseName = "todos_ktor"
    private val username = "root"
    private val password = ""

    //  database
    private val ktormDatabase: Database

    init {
        val jdbcUrl = "jdbc:mysql://$hostname:$port/$databaseName"
        ktormDatabase = Database.connect(jdbcUrl, user = username, password = password)
    }

    //user

    override fun addUser(user : UserRepository.User) : Boolean {

        val insertionProcess = ktormDatabase.insert(DBUserTable){
            set(DBUserTable.user_id, user.userId )
            set(DBUserTable.username, user.username)
            set(DBUserTable.password, user.password)
            set(DBUserTable.salt, user.salt)
            set(DBUserTable.email, user.email)
            set(DBUserTable.refreshToken, user.refreshToken)
        }

        return insertionProcess > 0
    }

    override fun getUser(email : String) : UserEntity? {
        val dbUserEntity =  ktormDatabase.sequenceOf(DBUserTable)
            .firstOrNull{ DBUserTable.email eq email }

        return if(dbUserEntity == null){
            null
        }else{
            UserEntity(
                username = dbUserEntity.username ,
                user_id = dbUserEntity.user_id ,
                password = dbUserEntity.password ,
                salt = dbUserEntity.salt ,
                email = dbUserEntity.email ,
                refresh_token = dbUserEntity.refresh_token
            )
        }
    }


    //todos
    override fun getAllToDosByUserId(userId : String): List<DBTodoEntity> {
        return ktormDatabase.sequenceOf(
            DBTodoTable
        ).filter { DBTodoTable.user_id eq userId }.toList()
    }



    override fun getTodo(id: Int, userId: String): TodoEntity? {
       val dbTodoEntity =  ktormDatabase.sequenceOf(DBTodoTable)
            .filter { DBTodoTable.user_id eq userId }
            .firstOrNull { DBTodoTable.id eq id }

        return if (dbTodoEntity == null){
            null
        }else {
            TodoEntity(
                id = dbTodoEntity.id ,
                title = dbTodoEntity.title ,
                done = dbTodoEntity.done ,
                user_id = dbTodoEntity.user_id
            )
        }
    }

    override fun addTodo(draft: ToDoDraft): TodoEntity {
        val insertedId = ktormDatabase.insertAndGenerateKey(DBTodoTable) {
            set(DBTodoTable.title, draft.title)
            set(DBTodoTable.done, draft.done)
            set(DBTodoTable.user_id, draft.user_id)
        } as Int

        return TodoEntity(insertedId, draft.title, draft.done , draft.user_id)
    }

    override fun updateTodo(id: Int, draft: ToDoDraft): Boolean {
        val updatedRows = ktormDatabase.update(DBTodoTable) {
            set(DBTodoTable.title, draft.title)
            set(DBTodoTable.done, draft.done)
            where {
                DBTodoTable.user_id eq draft.user_id
                DBTodoTable.id eq id
            }
        }

        return updatedRows > 0
    }

    override fun removeToDo(id: Int, userId: String): Boolean {
        val deletedRows = ktormDatabase.delete(DBTodoTable) {
            DBTodoTable.user_id eq userId
            DBTodoTable.id eq id
        }

        return deletedRows > 0
    }


}