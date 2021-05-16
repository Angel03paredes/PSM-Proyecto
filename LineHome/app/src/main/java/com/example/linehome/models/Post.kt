package com.example.linehome.models

data class Post(
        var id:Int? = null,
        var titlePublication:String? = null,
        var description:String? = null,
        var price:Int? = null,
        var location:String? = null,
        var category:String? = null,
        var owner:Int? = null,
        var createdAt: String? = null
) {

}