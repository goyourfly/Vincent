package com.goyourfly.vincent.app

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById(R.id.btn_net).setOnClickListener {
            startActivity(Intent(this@MainActivity, LoadFromNetActivity::class.java))
        }

        findViewById(R.id.btn_net_2).setOnClickListener {
            startActivity(Intent(this@MainActivity, LoadFromNet2Activity::class.java))
        }

        findViewById(R.id.btn_gallery).setOnClickListener {
            startActivity(Intent(this@MainActivity, GalleryActivity::class.java))
        }

        findViewById(R.id.btn_single).setOnClickListener {
            startActivity(Intent(this@MainActivity, SingleImageActivity::class.java))
        }

        findViewById(R.id.btn_gif).setOnClickListener {
            startActivity(Intent(this@MainActivity,GifActivity::class.java))
        }
    }
}
