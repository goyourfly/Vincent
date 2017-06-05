package com.goyourfly.vincent.app

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.goyourfly.vincent.Vincent
import com.goyourfly.vincent.transform.RoundRectTransform

class SingleImageActivity : AppCompatActivity() {
    val image:ImageView by lazy {
        findViewById(R.id.image) as ImageView
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_image)


        findViewById(R.id.reload).setOnClickListener {
            Vincent.with(this)
//                    .load("http://i.imgur.com/CqmBjo5.jpg")
//                    .load("http://wx4.sinaimg.cn/large/ab9754c5ly1fg70pc7pyqg20i00bnheb.gif")
//                    .load(R.drawable.test_img)
                    .load("http://img2.imgtn.bdimg.com/it/u=790136929,2931538723&fm=26&gp=0.jpg")
                    .placeholder(R.drawable.loading)
                    .fit()
                    .transform(RoundRectTransform(20F))
                    .error(R.drawable.loading_error)
                    .into(image)
        }
    }
}
