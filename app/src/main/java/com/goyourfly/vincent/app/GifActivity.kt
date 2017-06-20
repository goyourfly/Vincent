package com.goyourfly.vincent.app

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.goyourfly.vincent.Vincent
import com.goyourfly.vincent.transform.RoundRectTransform

class GifActivity : AppCompatActivity() {
    val image: ImageView by lazy {
        findViewById(R.id.image) as ImageView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_image)


//        Vincent.with(this)
//                .load(R.drawable.test_img)
//                .placeholder(R.drawable.loading)
//                .fit()
//                .transform(RoundRectTransform(20F))
//                .error(R.drawable.loading_error)
//                .into(image)
    }
}
