package com.example.linehome.utilities


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toBitmap
import java.io.ByteArrayOutputStream
//Esta clase permite obtener un ByteArray de diferentes origenes o
//convertir un byArray a un Bitmap
object  ImageUtilities {
    init {

    }

    //Obtener una imagen desde la carpeta res y convertirla a byte array
    fun getByteArrayFromResourse(idImage: Int, content: Context): ByteArray {
        var bitmap = BitmapFactory.decodeResource(content.resources, idImage)
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        return stream.toByteArray()
    }

    //Obtiene un byte array desde un bitmap
    fun getByteArrayFromBitmap(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        return stream.toByteArray()
    }

    //Obtiene un byte arrya desde un drawable
    fun getByteArrayFromDrawable(drawable: Drawable, content: Context): ByteArray {
        var bitMap = drawable.toBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, null)
        val stream = ByteArrayOutputStream()
        bitMap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        return stream.toByteArray()
    }

    //Obtiene un bitamap desde byteArray
    fun getBitMapFromByteArray(data: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(data, 0, data.size)
    }

}
