package com.goyourfly.vincent.app

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import java.io.IOException


class LoadFromNetActivity : AppCompatActivity() {
    val recycler: RecyclerView by lazy {
        findViewById(R.id.recycler) as RecyclerView
    }
    val adapter = MyAdapter()
    val mLayoutManager = GridLayoutManager(this, 4);
    var loading = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_from_net)

        recycler.adapter = adapter
        recycler.layoutManager = mLayoutManager

        getImage()
    }

    fun getImage() {
        loading = false
        DataProvider.fetchImages("Cat Wallpaper",object : DataProvider.ImageCallback {
            override fun onFailed(e: IOException) {
                e.printStackTrace()
                loading = true
            }

            override fun onSuccess(data: List<String>) {
                loading = true
                runOnUiThread{
                    adapter.addItems(data)
                    adapter.notifyDataSetChanged()
                }
            }
        })
    }
}