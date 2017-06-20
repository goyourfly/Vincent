package com.goyourfly.vincent.app

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.LinearLayoutManager
import java.io.IOException


class LoadFromNet2Activity : AppCompatActivity() {
    val recycler: RecyclerView by lazy {
        findViewById(R.id.recycler) as RecyclerView
    }
    val adapter = MyAdapter()
    val mLayoutManager = GridLayoutManager(this,3)
    var loading = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_from_net)

        recycler.adapter = adapter
        recycler.layoutManager = mLayoutManager

        getImage()
        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) //check for scroll down
                {
                    val visibleItemCount = mLayoutManager.getChildCount();
                    val totalItemCount = mLayoutManager.getItemCount();
                    val pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            getImage()
                        }
                    }
                }
            }
        })
    }

    fun getImage() {
        loading = false
        DataProvider.fetchImages("Material Design",adapter.itemCount,50,object : DataProvider.ImageCallback {
            override fun onFailed(e: IOException) {
                e.printStackTrace()
                loading = true
            }

            override fun onSuccess(data: List<String>) {
                loading = true
                runOnUiThread {
                    val start = adapter.itemCount
                    adapter.addItems(data)
                    adapter.notifyItemRangeInserted(start,data.size)
                }
            }
        })
    }
}
