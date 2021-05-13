package com.example.linehome.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import java.lang.Exception

class DataDBHelper (var context: Context): SQLiteOpenHelper(context,SetDB.DB_NAME,null,SetDB.DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {

        try {

            val createUserAvatar: String = "CREATE TABLE " + SetDB.user.TABLE_NAME + "(" +
                    SetDB.user.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    SetDB.user.COL_UPLOAD + " INTEGER DEFAULT 0," +
                    SetDB.user.COL_AVATAR + " BLOB)"

            db?.execSQL(createUserAvatar)
                /*
            val createGenreTable: String = "CREATE TABLE " + SetDB.tblGenre.TABLE_NAME + "(" +
                    SetDB.tblGenre.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            SetDB.tblGenre.COL_TITLE + " VARCHAR(50))"

            db?.execSQL(createGenreTable) */

            Log.e("ENTRO", "CREO TABLAS")

        } catch (e: Exception) {
            Log.e("Execption", e.toString())
        }

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    public fun insertAvatar(avatar:ByteArray):Boolean{

        val dataBase:SQLiteDatabase = this.writableDatabase
        val values: ContentValues = ContentValues()
        var boolResult:Boolean =  true

        values.put(SetDB.user.COL_AVATAR,avatar)


        try {
            val result =  dataBase.insert(SetDB.user.TABLE_NAME, null, values)

            if (result == (0).toLong()) {
                Toast.makeText(this.context, "Failed", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this.context, "Success", Toast.LENGTH_SHORT).show()
            }

        }catch (e: Exception){
            Log.e("Execption", e.toString())
            boolResult =  false
        }

        dataBase.close()

        return boolResult
    }

    public fun getAvatar():ByteArray?{

        val dataBase:SQLiteDatabase = this.writableDatabase

        val columns:Array<String> =  arrayOf(SetDB.user.COL_AVATAR)

        var avatar : ByteArray? = null

        val data =  dataBase.query(SetDB.user.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                SetDB.user.COL_ID + " DESC","1")

        if(data.moveToFirst()){

            avatar = data.getBlob(data.getColumnIndex(SetDB.user.COL_AVATAR))

        }

        data.close()
        return avatar
    }


}