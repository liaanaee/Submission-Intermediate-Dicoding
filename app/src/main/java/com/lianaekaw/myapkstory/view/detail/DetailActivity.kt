package com.lianaekaw.myapkstory.view.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.lianaekaw.myapkstory.R

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val title = intent.getStringExtra("STORY_TITLE")
        val content = intent.getStringExtra("STORY_CONTENT")
        val imageUrl = intent.getStringExtra("STORY_IMAGE_URL")

        findViewById<TextView>(R.id.detail_name).text = title
        findViewById<TextView>(R.id.detail_desc).text = content
        Glide.with(this)
            .load(imageUrl)
            .into(findViewById<ImageView>(R.id.detail_image))
    }
}