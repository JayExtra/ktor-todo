package com.dev.james.domain.models

data class ToDo(
    val id : Int ,
    var title : String ,
    var done : Boolean ,
    val user_id : String
)
