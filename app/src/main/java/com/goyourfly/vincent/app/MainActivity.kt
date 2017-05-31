package com.goyourfly.vincent.app

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.goyourfly.vincent.Vincent

class MainActivity : AppCompatActivity() {
    val image:ImageView by lazy {
        findViewById(R.id.image) as ImageView
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Vincent.with(this)
                .load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1496234844815&di=e17062434e48bbef62a6e00493bca6e2&imgtype=0&src=http%3A%2F%2Fimg3.3lian.com%2F2013%2Fc4%2F99%2Fd%2F19.jpg")
//                .load("http://n4-q.mafengwo.net/s6/M00/84/96/wKgB4lMcd3yAGIDIABTcehHVX7A96.jpeg?imageView2%2F2%2Fw%2F700%2Fh%2F600%2Fq%2F90")
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher)
                .into(image)


    }
}
