package com.dev.james.data.entities

data class TodoEntity(
    val id : Int ,
    val title : String ,
    val done : Boolean ,
    val user_id : String
)
