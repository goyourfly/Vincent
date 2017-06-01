package com.goyourfly.vincent.app

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.goyourfly.vincent.Vincent

class MainActivity : AppCompatActivity() {
    val url_0 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1496288386494&di=8c4255357a9fa8b98a51d4b1e1139843&imgtype=0&src=http%3A%2F%2Fpic45.huitu.com%2Fres%2F20151223%2F203378_20151223140315269200_1.jpg"
    val url_1 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1496288435089&di=11134482590e7e5fb457cb495c8b1d25&imgtype=0&src=http%3A%2F%2Fimg3.duitang.com%2Fuploads%2Fitem%2F201505%2F09%2F20150509235715_Z42ri.jpeg"


    val recycler:RecyclerView by lazy {
        findViewById(R.id.recycler) as RecyclerView
    }
    val adapter = MyAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler.adapter = adapter
        recycler.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)

        for (i in 1..100){
            adapter.addItem(if(i % 2 == 0) url_0 else url_1)
        }
        adapter.notifyItemRangeInserted(0,adapter.itemCount)
    }

    class MyAdapter : RecyclerView.Adapter<MyAdapter.ViewHolder>() {
        val list = mutableListOf<String>()

        fun addItem(item:String){
            list.add(item)
        }


        override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
            Vincent.with(p0.image.context)
                    .load(list.get(p1))
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher)
                    .into(p0.image)

        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onCreateViewHolder(p0: ViewGroup?, p1: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(p0?.context).inflate(R.layout.item_img,p0,false))
        }

        class ViewHolder(view:View): RecyclerView.ViewHolder(view) {
            val image = view.findViewById(R.id.image) as ImageView
        }
    }
}
