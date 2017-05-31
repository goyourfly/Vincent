package com.goyourfly.vincent.app

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.goyourfly.vincent.Vincent

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Vincent.with(this)
                .load("www.baidu.com")

    }
}
