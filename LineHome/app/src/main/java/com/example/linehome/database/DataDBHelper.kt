package com.example.linehome.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import com.example.linehome.models.Post
import com.example.linehome.models.PostPreview
import com.example.linehome.models.PublicationPhoto
import java.io.ByteArrayOutputStream
import java.lang.Exception

class DataDBHelper (var context: Context): SQLiteOpenHelper(context,SetDB.DB_NAME,null,SetDB.DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {

        try {

            val createUserAvatar: String = "CREATE TABLE " + SetDB.user.TABLE_NAME + "(" +
                    SetDB.user.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    SetDB.user.COL_UPLOAD + " INTEGER DEFAULT 0," +
                    SetDB.user.COL_AVATAR + " BLOB)"

            db?.execSQL(createUserAvatar)

            val createPublicationTable: String = "CREATE TABLE publication (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " titlePublication  VARCHAR(100)," +
                    "description VARCHAR(250)," +
                    "price INTEGER," +
                    "location VARCHAR(100)," +
                    "category VARCHAR(20)," +
                    "owner INTEGER ," +
                    "createdAt VARCHAR(50)," +
                    "upload INTEGER " +
                    ")"

            db?.execSQL(createPublicationTable)

            val createPublicationPhotoTable: String = "CREATE TABLE publication_photo (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " publicationId  INTEGER," +
                    "image BLOB " +
                    ")"

            db?.execSQL(createPublicationPhotoTable)


            val createPublicationPreview: String = "CREATE TABLE publication_preview (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "ownerName VARCHAR(100) ," +
                    "imageOwner BLOB ," +
                    "imagePublication BLOB,"  +
                    " titlePublication  VARCHAR(100)," +
                    "description VARCHAR(250)," +
                    "evaluation INTEGER," +
                    "location VARCHAR(100)," +
                    "price INTEGER," +
                    "createdAt VARCHAR(250) " +

                    ")"

            db?.execSQL(createPublicationPreview)




            Log.e("ENTRO", "CREO TABLAS")

        } catch (e: Exception) {
            Log.e("Execption", e.toString())
        }

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    public fun insertAvatar(avatar: ByteArray): Boolean {

        val dataBase: SQLiteDatabase = this.writableDatabase
        val values: ContentValues = ContentValues()
        var boolResult: Boolean = true

        values.put(SetDB.user.COL_AVATAR, avatar)


        try {
            val result = dataBase.insert(SetDB.user.TABLE_NAME, null, values)

            if (result == (0).toLong()) {
                Toast.makeText(this.context, "Failed", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this.context, "Success", Toast.LENGTH_SHORT).show()
            }

        } catch (e: Exception) {
            Log.e("Execption", e.toString())
            boolResult = false
        }

        dataBase.close()

        return boolResult
    }

    public fun getAvatar(): ByteArray? {

        val dataBase: SQLiteDatabase = this.writableDatabase

        val columns: Array<String> = arrayOf(SetDB.user.COL_AVATAR)

        var avatar: ByteArray? = null

        val data = dataBase.query(SetDB.user.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                SetDB.user.COL_ID + " DESC", "1")

        if (data.moveToFirst()) {

            avatar = data.getBlob(data.getColumnIndex(SetDB.user.COL_AVATAR))

        }

        data.close()
        return avatar
    }


    public fun insertPublication(post: Post, upload: Int): Boolean {
        //upload = 1 if is download from api
        //upload = 0 if is not upload to api
        val dataBase: SQLiteDatabase = this.writableDatabase
        val values: ContentValues = ContentValues()
        var boolResult: Boolean = true

        values.put("titlePublication", post.titlePublication)
        values.put("description", post.description)
        values.put("category", post.category)
        values.put("location", post.location)
        values.put("owner", post.owner)
        values.put("price", post.price)
        values.put("createdAt", post.createdAt)
        values.put("upload", upload)


        try {
            val result = dataBase.insert("publication", null, values)

            if (result == (0).toLong()) {
                Toast.makeText(this.context, "Failed", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this.context, "Success", Toast.LENGTH_SHORT).show()
            }

        } catch (e: Exception) {
            Log.e("Execption", e.toString())
            boolResult = false
        }

        dataBase.close()

        return boolResult
    }

    public fun getLastIdPublication(): Int? {
        val dataBase: SQLiteDatabase = this.writableDatabase

        val columns: Array<String> = arrayOf("id")

        var id: Int? = null

        val data = dataBase.query("publication",
                columns,
                null,
                null,
                null,
                null,
                SetDB.user.COL_ID + " DESC", "1")

        if (data.moveToFirst()) {

            id = data.getInt(data.getColumnIndex("id"))

        }

        data.close()
        return id
    }

    public fun insertPublicationPhoto(id: Int, img: ByteArray): Boolean {

        val dataBase: SQLiteDatabase = this.writableDatabase
        val values: ContentValues = ContentValues()
        var boolResult: Boolean = true

        values.put("publicationId", id)
        values.put("image", img)

        try {
            val result = dataBase.insert("publication_photo", null, values)

            if (result == (0).toLong()) {
                Toast.makeText(this.context, "Failed", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this.context, "Success", Toast.LENGTH_SHORT).show()
            }

        } catch (e: Exception) {
            Log.e("Execption", e.toString())
            boolResult = false
        }

        dataBase.close()

        return boolResult
    }


    public fun getPublicationsNotUpload(): MutableList<Post> {
        val List: MutableList<Post> = ArrayList()

        val dataBase: SQLiteDatabase = this.writableDatabase

        val columns: Array<String> = arrayOf("id","titlePublication", "description", "price", "location", "category", "owner", "createdAt")


        val data = dataBase.query("publication",
                columns,
                " upload = 0",
                null,
                null,
                null,
                null)

        if (data.moveToFirst()) {

            do {
                val post: Post = Post()
                post?.id = data.getInt(data.getColumnIndex("id"))
                post?.titlePublication = data.getString(data.getColumnIndex("titlePublication"))
                post?.description = data.getString(data.getColumnIndex("description"))
                post?.price = data.getInt(data.getColumnIndex("price"))
                post?.location = data.getString(data.getColumnIndex("location"))
                post?.category = data.getString(data.getColumnIndex("category"))
                post?.owner = data.getInt(data.getColumnIndex("owner"))
                post?.createdAt = data.getString(data.getColumnIndex("createdAt"))


                List.add(post)
            } while (data.moveToNext())

            data.close()

        }
        return List
    }

    public fun getPublicationsPhoto( publicationId: Int): MutableList<ByteArray> {
        val List: MutableList<ByteArray> = ArrayList()

        val dataBase: SQLiteDatabase = this.writableDatabase

        val columns: Array<String> = arrayOf("image")


        val data = dataBase.query("publication_photo",
                columns,
                " publicationId = $publicationId",
                null,
                null,
                null,
                null)

        if (data.moveToFirst()) {

            do {

                val img: ByteArray = data.getBlob(data.getColumnIndex("image"))



                List.add(img)
            } while (data.moveToNext())

            data.close()

        }
        return List
    }


    public fun updatePublicationUpload():Boolean{

        val dataBase:SQLiteDatabase = this.writableDatabase
        val values: ContentValues = ContentValues()
        var boolResult:Boolean =  true

        values.put("upload",1)


        try{

            val result =  dataBase.update("publication",
                    values,
                    "upload = 0",
                    null)

            if (result != -1 ) {
                print("success")
            }
            else {
                print("error")

            }

        }catch (e: Exception){
            boolResult = false
            Log.e("Execption", e.toString())
        }

        dataBase.close()
        return  boolResult
    }

    public fun insertPublicationPreview(post:PostPreview):Boolean{
        val dataBase: SQLiteDatabase = this.writableDatabase
        val values: ContentValues = ContentValues()
        var boolResult: Boolean = true
        val baos = ByteArrayOutputStream()
        post.imagePublication?.compress(Bitmap.CompressFormat.JPEG, 25, baos)
        val baos2 = ByteArrayOutputStream()
        post.imageOwner?.compress(Bitmap.CompressFormat.JPEG, 25, baos2)

        values.put("ownerName", post.ownerName)
        values.put("imageOwner",baos2.toByteArray() )
        values.put("imagePublication",baos.toByteArray() )
        values.put("titlePublication", post.titlePublication)
        values.put("description", post.description)
        values.put("price", post.price)
        values.put("createdAt", post.createdAt)
        values.put("evaluation", post.evaluation)
        values.put("location", post.location)


        try {
            val result = dataBase.insert("publication_preview", null, values)

            if (result == (0).toLong()) {
               // Toast.makeText(this.context, "Failed", Toast.LENGTH_SHORT).show()
            } else {
               // Toast.makeText(this.context, "Success", Toast.LENGTH_SHORT).show()
            }

        } catch (e: Exception) {
            Log.e("Execption", e.toString())
            boolResult = false
        }

        dataBase.close()

        return boolResult
    }

    public fun getPublicationPreview():MutableList<PostPreview>{
        val List: MutableList<PostPreview> = ArrayList()

        val dataBase: SQLiteDatabase = this.writableDatabase

        val columns: Array<String> = arrayOf("id","titlePublication", "description", "price", "location", "createdAt", "imagePublication", "imageOwner","ownerName","evaluation")


        val data = dataBase.query("publication_preview",
                columns,
                null,
                null,
                null,
                null,
                "id DESC","10"
                )

        if (data.moveToFirst()) {

            do {
                val post: PostPreview = PostPreview()
                post?.id = data.getInt(data.getColumnIndex("id"))
                post?.titlePublication = data.getString(data.getColumnIndex("titlePublication"))
                post?.description = data.getString(data.getColumnIndex("description"))
                post?.price = data.getInt(data.getColumnIndex("price"))
                post?.location = data.getString(data.getColumnIndex("location"))
                post?.ownerName = data.getString(data.getColumnIndex("ownerName"))
                post?.evaluation = data.getInt(data.getColumnIndex("evaluation"))
                post?.createdAt = data.getString(data.getColumnIndex("createdAt"))
                post?.imagePublication =  BitmapFactory.decodeByteArray(data.getBlob(data.getColumnIndex("imagePublication")), 0, data.getBlob(data.getColumnIndex("imagePublication")).size)
                post?.imageOwner = BitmapFactory.decodeByteArray(data.getBlob(data.getColumnIndex("imageOwner")), 0, data.getBlob(data.getColumnIndex("imageOwner")).size)


                List.add(post)
            } while (data.moveToNext())

            data.close()

        }
        return List
    }

    public fun truncatePublicatePreview():Boolean{
        val dataBase: SQLiteDatabase = this.writableDatabase
        var boolResult: Boolean = true
        try {
            dataBase.execSQL("DElETE FROM publication_preview")
        }catch(e:Exception){
                boolResult=false
            print(e)
        }
        return boolResult
    }

    public fun getPublicationPreviewById(id:Int):PostPreview{

        val post: PostPreview = PostPreview()
        val dataBase: SQLiteDatabase = this.writableDatabase

        val columns: Array<String> = arrayOf("id","titlePublication", "description", "price", "location", "createdAt", "imagePublication", "imageOwner","ownerName","evaluation")


        val data = dataBase.query("publication_preview",
            columns,
            "id = $id",
            null,
            null,
            null,
            "id DESC","1"
        )

        if (data.moveToFirst()) {



                post?.id = data.getInt(data.getColumnIndex("id"))
                post?.titlePublication = data.getString(data.getColumnIndex("titlePublication"))
                post?.description = data.getString(data.getColumnIndex("description"))
                post?.price = data.getInt(data.getColumnIndex("price"))
                post?.location = data.getString(data.getColumnIndex("location"))
                post?.ownerName = data.getString(data.getColumnIndex("ownerName"))
                post?.evaluation = data.getInt(data.getColumnIndex("evaluation"))
                post?.createdAt = data.getString(data.getColumnIndex("createdAt"))
                post?.imagePublication =  BitmapFactory.decodeByteArray(data.getBlob(data.getColumnIndex("imagePublication")), 0, data.getBlob(data.getColumnIndex("imagePublication")).size)
                post?.imageOwner = BitmapFactory.decodeByteArray(data.getBlob(data.getColumnIndex("imageOwner")), 0, data.getBlob(data.getColumnIndex("imageOwner")).size)





            data.close()

        }
        return post
    }





}