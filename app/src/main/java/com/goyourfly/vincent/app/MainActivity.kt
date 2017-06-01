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
    val recycler:RecyclerView by lazy {
        findViewById(R.id.recycler) as RecyclerView
    }
    val adapter = MyAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler.adapter = adapter
        recycler.layoutManager = StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL)

        for (str in Data.URLS){
            adapter.addItem(str)
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
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.loading_error)
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
