package com.example.linehome.models

import android.graphics.Bitmap

class PostPreview(
    var id:Int? = null,
    var ownerName: String? = null,
    var ownerId: Int? = null,
    var imageOwner: Bitmap? = null,
    var imagePublication: Bitmap? = null,
    var titlePublication: String? = null,
    var description: String? = null,
    var evaluation: Int? = null,
    var location: String? = null,
    var price: Int? = null,
    var createdAt: String? = null
) {
}