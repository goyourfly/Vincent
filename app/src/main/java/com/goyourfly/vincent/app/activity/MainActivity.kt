package com.goyourfly.vincent.app.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.goyourfly.vincent.app.R


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById(R.id.btn_net).setOnClickListener {
            startActivity(Intent(this@MainActivity, RecyclerViewSampleActivity::class.java))
        }

        findViewById(R.id.btn_net_2).setOnClickListener {
            startActivity(Intent(this@MainActivity, EndlessRecyclerViewSampleActivity::class.java))
        }

        findViewById(R.id.btn_staggered).setOnClickListener {
            startActivity(Intent(this@MainActivity, StaggeredRecyclerViewSampleActivity::class.java))
        }

        findViewById(R.id.btn_gallery).setOnClickListener {
            startActivity(Intent(this@MainActivity, GallerySampleActivity::class.java))
        }

        findViewById(R.id.btn_single).setOnClickListener {
            startActivity(Intent(this@MainActivity, SingleImageSampleActivity::class.java))
        }

        findViewById(R.id.btn_gif).setOnClickListener {
            startActivity(Intent(this@MainActivity, GifSampleActivity::class.java))
        }
    }
}
