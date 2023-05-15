package com.dev.james.data.database.ktorm_database.tables

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.boolean
import org.ktorm.schema.int
import org.ktorm.schema.varchar


//table name and columns should correspond with actual table in mysql

object DBTodoTable : Table<DBTodoEntity>(tableName = "todos") {
    //these fields represent columns of the table
    val id = int("id").primaryKey().bindTo { it.id }
    val title = varchar("title").bindTo { it.title }
    val done = boolean("done").bindTo { it.done }
    val user_id = varchar("user_id").bindTo { it.user_id }

}

interface DBTodoEntity : Entity<DBTodoEntity> {

    companion object : Entity.Factory<DBTodoEntity>()

    val id : Int
    val title : String
    val done : Boolean
    val user_id : String

}