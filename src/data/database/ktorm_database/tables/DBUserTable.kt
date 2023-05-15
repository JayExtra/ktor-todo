package com.dev.james.data.database.ktorm_database.tables

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.varchar

object DBUserTable : Table<DBUserEntity>(tableName = "user") {

    val user_id = varchar("user_id").bindTo { it.user_id }
    val username = varchar("username").bindTo { it.username }
    val password = varchar("password").bindTo { it.password }
    val salt = varchar("salt").bindTo { it.salt }
    val email = varchar("email").bindTo { it.email }
    val refreshToken = varchar("refresh_token").bindTo { it.refresh_token }

}

interface DBUserEntity : Entity<DBUserEntity> {

    companion object : Entity.Factory<DBUserEntity>()

    val user_id: String
    val username: String
    val password: String
    val salt: String
    val email: String
    val refresh_token: String

}