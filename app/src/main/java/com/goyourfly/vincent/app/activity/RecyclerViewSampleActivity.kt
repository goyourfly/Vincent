package com.goyourfly.vincent.app.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.goyourfly.multiple.adapter.MultipleSelect
import com.goyourfly.multiple.adapter.viewholder.view.CheckBoxFactory
import com.goyourfly.multiple.adapter.viewholder.view.RadioBtnFactory
import com.goyourfly.vincent.app.DataProvider
import com.goyourfly.vincent.app.adapter.MyAdapter
import com.goyourfly.vincent.app.R
import java.io.IOException


class RecyclerViewSampleActivity : AppCompatActivity() {
    val recycler: RecyclerView by lazy {
        findViewById(R.id.recycler) as RecyclerView
    }
    val adapter = MyAdapter()
    val mLayoutManager = GridLayoutManager(this, 4);
    var loading = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_from_net)

        recycler.adapter = MultipleSelect.with(this)
                .adapter(adapter)
                .decorateFactory(CheckBoxFactory())
                .build()
        recycler.layoutManager = mLayoutManager

        getImage()
    }

    fun getImage() {
        loading = false
        DataProvider.fetchImages("凡高", 0, 150, object : DataProvider.ImageCallback {
            override fun onFailed(e: IOException) {
                e.printStackTrace()
                loading = true
            }

            override fun onSuccess(data: List<String>) {
                loading = true
                runOnUiThread {
                    adapter.addItems(data)
                    recycler.adapter.notifyDataSetChanged()
                }
            }
        })
    }
}
