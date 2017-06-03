package com.goyourfly.vincent.app

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.goyourfly.vincent.Vincent

class SingleImageActivity : AppCompatActivity() {
    val image:ImageView by lazy {
        findViewById(R.id.image) as ImageView
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_image)


        findViewById(R.id.reload).setOnClickListener {
            Vincent.with(this)
                    .load("http://i.imgur.com/CqmBjo5.jpg")
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.loading_error)
                    .into(image)
        }
    }
}
