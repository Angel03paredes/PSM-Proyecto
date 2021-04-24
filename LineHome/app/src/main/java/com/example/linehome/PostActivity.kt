package com.example.linehome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.synnapps.carouselview.ImageListener
import kotlinx.android.synthetic.main.activity_post.*

class PostActivity : AppCompatActivity() {

    var sampleImages = intArrayOf(
        R.drawable.dubai,
        R.drawable.moscow,
        R.drawable.paris,
        R.drawable.uk
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        btnOnBack.setOnClickListener {
            finish()
        }

        carouselView.setPageCount(sampleImages.size);
        carouselView.setImageListener(imageListener);
    }

    var imageListener: ImageListener = object : ImageListener {
        override fun setImageForPosition(position: Int, imageView: ImageView) {
            // You can use Glide or Picasso here
            imageView.setImageResource(sampleImages[position])
        }
    }
}