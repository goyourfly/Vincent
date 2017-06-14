package com.goyourfly.vincent.app

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.goyourfly.multiple.adapter.MultipleSelect
import com.goyourfly.multiple.adapter.binder.view.CheckBoxFactory
import com.goyourfly.vincent.Vincent
import com.goyourfly.vincent.common.logD
import com.goyourfly.vincent.transform.RoundRectTransform


class LoadFromNetActivity : AppCompatActivity() {
    val recycler: RecyclerView by lazy {
        findViewById(R.id.recycler) as RecyclerView
    }
    val adapter = MyAdapter()
    val mLayoutManager = GridLayoutManager(this, 3);
    var loading = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_from_net)

        recycler.adapter = MultipleSelect.with(this)
                .adapter(adapter)
                .decorateFactory(CheckBoxFactory())
                .build()
        recycler.layoutManager = mLayoutManager

        for (str in Data.URLS2) {
            adapter.addItem(str)
        }
        for (str in Data.URLS2) {
            adapter.addItem(str)
        }
        for (str in Data.URLS2) {
            adapter.addItem(str)
        }
        for (str in Data.URLS2) {
            adapter.addItem(str)
        }
        for (str in Data.URLS2) {
            adapter.addItem(str)
        }
        adapter.notifyItemRangeInserted(0, adapter.itemCount)
        findViewById(R.id.refresh).setOnClickListener {
            adapter.notifyDataSetChanged()
        }


    }

    class MyAdapter : RecyclerView.Adapter<MyAdapter.ViewHolder>() {
        val list = mutableListOf<String>()

        fun addItem(item: String) {
            list.add(item)
        }


        override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
            "onBindViewHolder:$p1,${p0.image.hashCode()}".logD()
            Vincent.with(p0.image.context)
                    .load(list.get(p1))
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.loading_error)
                    .fit()
                    .transform(RoundRectTransform(20F))
                    .into(p0.image)

//            Picasso.with(p0.image.context)
//                    .load(list.get(p1))
//                    .placeholder(R.drawable.loading)
//                    .error(R.drawable.loading_error)
//                    .fit()
//                    .into(p0.image)
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onCreateViewHolder(p0: ViewGroup?, p1: Int): ViewHolder {
            val view = ViewHolder(LayoutInflater.from(p0?.context).inflate(R.layout.item_img, p0, false))
            "onBindCreateViewHolder:${view.hashCode()}".logD()
            return view
        }

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val image = view.findViewById(R.id.image) as ImageView
        }
    }
}
