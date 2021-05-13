package com.example.linehome.models

data class Post(
        var id:Int? = null,
        var user:Int? = null,
        var title:String? = null,
        var description:String? = null,
        var location:String? = null,
        var price: String? = null,
        var date: Any? = null
) {

}