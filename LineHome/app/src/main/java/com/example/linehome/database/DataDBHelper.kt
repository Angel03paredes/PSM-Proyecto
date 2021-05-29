package com.example.linehome.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import com.example.linehome.models.*
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

            //Tabla para las imagenes de las publicaciones que se guardan
            val createPublicationPhotoPreview: String = "CREATE TABLE publication_photo_preview (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " publicationId  INTEGER," +
                    "image BLOB " +
                    ")"

            db?.execSQL(createPublicationPhotoPreview)

            val createSavePublication: String = "CREATE TABLE save_publication (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "ownerName VARCHAR(100) ," +
                    "ownerId INTEGER," +
                    "imageOwner BLOB ," +
                    "imagePublication BLOB,"  +
                    " titlePublication  VARCHAR(100)," +
                    "description VARCHAR(250)," +
                    "evaluation INTEGER," +
                    "location VARCHAR(100)," +
                    "price INTEGER," +
                    "createdAt VARCHAR(250) " +
                    ")"

            db?.execSQL(createSavePublication)

            val createNotificationPreview: String = "CREATE TABLE notification_preview (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "userName VARCHAR(100) ," +
                    "imageUrl BLOB ," +
                    "createdAt VARCHAR(250) " +
                    ")"

            db?.execSQL(createNotificationPreview)



            val createPublicationPreview: String = "CREATE TABLE publication_preview (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "ownerName VARCHAR(100) ," +
                    "ownerId INTEGER," +
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
        values.put("ownerId", post.ownerId)
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
                //Toast.makeText(this.context, "Failed", Toast.LENGTH_SHORT).show()
            } else {
               //Toast.makeText(this.context, "Success", Toast.LENGTH_SHORT).show()
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

        val columns: Array<String> = arrayOf("id","titlePublication", "description", "price", "location", "createdAt", "imagePublication", "imageOwner","ownerName", "ownerId","evaluation")


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
                post?.ownerId = data.getInt(data.getColumnIndex("ownerId"))
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

        val columns: Array<String> = arrayOf("id","titlePublication", "description", "price", "location", "createdAt", "imagePublication", "imageOwner","ownerName", "ownerId","evaluation")


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
                post?.ownerId = data.getInt(data.getColumnIndex("ownerId"))
                post?.evaluation = data.getInt(data.getColumnIndex("evaluation"))
                post?.createdAt = data.getString(data.getColumnIndex("createdAt"))
                post?.imagePublication =  BitmapFactory.decodeByteArray(data.getBlob(data.getColumnIndex("imagePublication")), 0, data.getBlob(data.getColumnIndex("imagePublication")).size)
                post?.imageOwner = BitmapFactory.decodeByteArray(data.getBlob(data.getColumnIndex("imageOwner")), 0, data.getBlob(data.getColumnIndex("imageOwner")).size)





            data.close()

        }
        return post
    }

    public fun insertSavePublication(post:PostPreview):Boolean{
        val dataBase: SQLiteDatabase = this.writableDatabase
        val values: ContentValues = ContentValues()
        var boolResult: Boolean = true
        val baos = ByteArrayOutputStream()
        post.imagePublication?.compress(Bitmap.CompressFormat.JPEG, 25, baos)
        val baos2 = ByteArrayOutputStream()
        post.imageOwner?.compress(Bitmap.CompressFormat.JPEG, 25, baos2)

        values.put("ownerName", post.ownerName)
        values.put("ownerId", post.ownerId)
        values.put("imageOwner",baos2.toByteArray() )
        values.put("imagePublication",baos.toByteArray() )
        values.put("titlePublication", post.titlePublication)
        values.put("description", post.description)
        values.put("price", post.price)
        values.put("createdAt", post.createdAt)
        values.put("evaluation", post.evaluation)
        values.put("location", post.location)


        try {
            val result = dataBase.insert("save_publication", null, values)

            if (result == (0).toLong()) {
                //Toast.makeText(this.context, "Failed", Toast.LENGTH_SHORT).show()
            } else {
                //Toast.makeText(this.context, "Success", Toast.LENGTH_SHORT).show()
            }

        } catch (e: Exception) {
            Log.e("Execption", e.toString())
            boolResult = false
        }

        dataBase.close()

        return boolResult
    }

    public fun getSavePublication(id:Int):MutableList<PostPreview>{
        val List: MutableList<PostPreview> = ArrayList()

        val dataBase: SQLiteDatabase = this.writableDatabase

        val columns: Array<String> = arrayOf("id","titlePublication", "description", "price", "location", "createdAt", "imagePublication", "imageOwner","ownerName", "ownerId","evaluation")

        val data = dataBase.query("save_publication",
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
                post?.ownerId = data.getInt(data.getColumnIndex("ownerId"))
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

    public fun truncateSavePublication():Boolean{
        val dataBase: SQLiteDatabase = this.writableDatabase
        var boolResult: Boolean = true
        try {
            dataBase.execSQL("DElETE FROM save_publication")
        }catch(e:Exception){
            boolResult=false
            print(e)
        }
        return boolResult
    }

    public fun insertNotification(notification:NotifyPreview):Boolean{
        val dataBase: SQLiteDatabase = this.writableDatabase
        val values: ContentValues = ContentValues()
        var boolResult: Boolean = true

        val baos = ByteArrayOutputStream()
        notification.imageUrl?.compress(Bitmap.CompressFormat.JPEG, 25, baos)

        values.put("userName", notification.userName)
        values.put("imageUrl", baos.toByteArray())
        values.put("createdAt", notification.createdAt)


        try {
            val result = dataBase.insert("notification_preview", null, values)

            if (result == (0).toLong()) {
                //Toast.makeText(this.context, "Failed", Toast.LENGTH_SHORT).show()
            } else {
                //Toast.makeText(this.context, "Success", Toast.LENGTH_SHORT).show()
            }

        } catch (e: Exception) {
            Log.e("Execption", e.toString())
            boolResult = false
        }

        dataBase.close()

        return boolResult
    }

    public fun getNotification(id:Int):MutableList<NotifyPreview>{
        val List: MutableList<NotifyPreview> = ArrayList()

        val dataBase: SQLiteDatabase = this.writableDatabase

        val columns: Array<String> = arrayOf("id","userName", "imageUrl", "createdAt")

        val data = dataBase.query("notification_preview",
            columns,
            null,
            null,
            null,
            null,
            "id DESC","10"
        )

        if (data.moveToFirst()) {

            do {
                val not: NotifyPreview = NotifyPreview()
                not?.id = data.getInt(data.getColumnIndex("id"))
                not?.userName = data.getString(data.getColumnIndex("userName"))
                not?.createdAt = data.getString(data.getColumnIndex("createdAt"))
                not?.imageUrl = BitmapFactory.decodeByteArray(data.getBlob(data.getColumnIndex("imageUrl")), 0, data.getBlob(data.getColumnIndex("imageUrl")).size)


                List.add(not)
            } while (data.moveToNext())

            data.close()

        }
        return List
    }

    public fun truncateNotification():Boolean{
        val dataBase: SQLiteDatabase = this.writableDatabase
        var boolResult: Boolean = true
        try {
            dataBase.execSQL("DElETE FROM notification_preview")
        }catch(e:Exception){
            boolResult=false
            print(e)
        }
        return boolResult
    }

    //SAVE PUBLICATIONS

    //Funciones para las fotos de las imagenes guardadas
    public fun getLastIdPublicationPreview(): Int? {
        val dataBase: SQLiteDatabase = this.writableDatabase

        val columns: Array<String> = arrayOf("id")

        var id: Int? = null

        val data = dataBase.query("publication_preview ",
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

    public fun getPublicationPreviewPhotoById(id:Int):MutableList<Bitmap>{

        var List: MutableList<Bitmap> = ArrayList()
        val dataBase: SQLiteDatabase = this.writableDatabase

        val columns: Array<String> = arrayOf( "image")


        val data = dataBase.query("publication_photo_preview",
                columns,
                "publicationId = $id",
                null,
                null,
                null,
                null
        )

        if (data.moveToFirst()) {

            do {
                var image: Bitmap? = null

                image = BitmapFactory.decodeByteArray(data.getBlob(data.getColumnIndex("image")), 0, data.getBlob(data.getColumnIndex("image")).size)



                List.add(image)
            } while (data.moveToNext())

        }

        return List
    }

    public fun insertPublicationPreviewPhoto(publicationId: Int, image:Bitmap):Boolean{
        val dataBase: SQLiteDatabase = this.writableDatabase
        val values: ContentValues = ContentValues()
        var boolResult: Boolean = true

        val baos = ByteArrayOutputStream()
        image?.compress(Bitmap.CompressFormat.JPEG, 25, baos)


        values.put("publicationId", publicationId)
        values.put("image", baos.toByteArray())


        try {
            val result = dataBase.insert("publication_photo_preview", null, values)

            if (result == (0).toLong()) {
                print("error")
            } else {
                print("success")
            }

        } catch (e: Exception) {
            Log.e("Execption", e.toString())
            boolResult = false
        }

        dataBase.close()

        return boolResult
    }

    public fun truncatePublicationPhotoPreview():Boolean{
        val dataBase: SQLiteDatabase = this.writableDatabase
        var boolResult: Boolean = true
        try {
            dataBase.execSQL("DElETE FROM publication_photo_preview")
        }catch(e:Exception){
            boolResult=false
            print(e)
        }
        return boolResult
    }


}