package com.example.linehome.database

class SetDB {

    companion object{
        val DB_NAME =  "dbLineHome"
        val DB_VERSION =  1
    }


    abstract class user{

        companion object{
            val TABLE_NAME = "user"
            val COL_ID = "id"
            val COL_AVATAR = "avatar"
            val COL_UPLOAD = "upload"

        }
    }

    abstract class post{
        companion object{
            val TABLE_NAME = "post"

        }
    }

}