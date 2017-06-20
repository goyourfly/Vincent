package com.goyourfly.vincent.app.activity

import com.goyourfly.vincent.app.DataProvider
import com.goyourfly.vincent.app.adapter.MyAdapter
import java.io.IOException


class EndlessRecyclerViewSampleActivity : android.support.v7.app.AppCompatActivity() {
    val recycler: android.support.v7.widget.RecyclerView by lazy {
        findViewById(com.goyourfly.vincent.app.R.id.recycler) as android.support.v7.widget.RecyclerView
    }
    val adapter = MyAdapter()
    val mLayoutManager = android.support.v7.widget.GridLayoutManager(this, 3)
    var loading = true
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.goyourfly.vincent.app.R.layout.activity_load_from_net)

        recycler.adapter = adapter
        recycler.layoutManager = mLayoutManager

        getImage()
        recycler.addOnScrollListener(object : android.support.v7.widget.RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: android.support.v7.widget.RecyclerView, dx: Int, dy: Int) {
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
        com.goyourfly.vincent.app.DataProvider.fetchImages("Material Design", adapter.itemCount, 50, object : DataProvider.ImageCallback {
            override fun onFailed(e: IOException) {
                e.printStackTrace()
                loading = true
            }

            override fun onSuccess(data: List<String>) {
                loading = true
                runOnUiThread {
                    val start = adapter.itemCount
                    adapter.addItems(data)
                    adapter.notifyItemRangeInserted(start, data.size)
                }
            }
        })
    }
}
