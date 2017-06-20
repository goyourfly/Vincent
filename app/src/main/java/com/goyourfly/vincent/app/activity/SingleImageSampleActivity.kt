package com.goyourfly.vincent.app.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.goyourfly.vincent.Vincent
import com.goyourfly.vincent.transform.RoundRectTransform

class SingleImageSampleActivity : android.support.v7.app.AppCompatActivity() {
    val image: android.widget.ImageView by lazy {
        findViewById(com.goyourfly.vincent.app.R.id.image) as android.widget.ImageView
    }

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.goyourfly.vincent.app.R.layout.activity_single_image)

        com.goyourfly.vincent.Vincent.with(this)
//                    .load("http://i.imgur.com/CqmBjo5.jpg")
//                    .load("http://wx4.sinaimg.cn/large/ab9754c5ly1fg70pc7pyqg20i00bnheb.gif")
//                    .load(R.drawable.test_img)
//                .load("http://img2.imgtn.bdimg.com/it/u=790136929,2931538723&fm=26&gp=0.jpg")
//                .load("https://www.nasa.gov/sites/default/files/styles/full_width_feature/public/images/115334main_image_feature_329_ys_full.jpg?itok=8BKbVoQH")
                .load("http://www.wallpaperandphoto.com/wp-content/uploads/2016/01/most_beautiful_city_in_the_world-wallpaper-2560x1600.jpg")
                .fit()
                .placeholder(com.goyourfly.vincent.app.R.drawable.loading)
                .error(com.goyourfly.vincent.app.R.drawable.loading_error)
                .into(image)
    }
}
