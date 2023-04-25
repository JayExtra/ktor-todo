package com.dev.james.entities

data class ToDo(
    val id : Int ,
    var title : String ,
    var done : Boolean ,
    val user_id : String
)
