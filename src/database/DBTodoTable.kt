package com.dev.james.database

import com.dev.james.entities.ToDo
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

}

interface DBTodoEntity : Entity<DBTodoEntity> {

    companion object : Entity.Factory<DBTodoEntity>()

    val id : Int
    val title : String
    val done : Boolean

}