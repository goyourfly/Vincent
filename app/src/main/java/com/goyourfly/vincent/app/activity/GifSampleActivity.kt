package com.goyourfly.vincent.app.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.goyourfly.vincent.Vincent
import com.goyourfly.vincent.transform.RoundRectTransform

class GifSampleActivity : android.support.v7.app.AppCompatActivity() {
    val image: android.widget.ImageView by lazy {
        findViewById(com.goyourfly.vincent.app.R.id.image) as android.widget.ImageView
    }

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.goyourfly.vincent.app.R.layout.activity_single_image)


//        Vincent.with(this)
//                .load(R.drawable.test_img)
//                .placeholder(R.drawable.loading)
//                .fit()
//                .transform(RoundRectTransform(20F))
//                .error(R.drawable.loading_error)
//                .into(image)
    }
}
