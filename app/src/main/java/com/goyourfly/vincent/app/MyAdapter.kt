package com.goyourfly.vincent.app

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.goyourfly.vincent.Vincent
import com.goyourfly.vincent.common.logD
import com.goyourfly.vincent.transform.CircleTransform
import com.goyourfly.vincent.transform.RoundRectTransform
import com.squareup.picasso.Picasso

/**
 * Created by Administrator on 2017/6/18 0018.
 */
class MyAdapter : RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    val list = mutableListOf<String>()

    fun addItem(item: String) {
        list.add(item)
    }

    fun addItems(items: List<String>) {
        list.addAll(items)
    }


    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
//        if (p1 % 2 == 0) {
        Vincent.with(p0.image.context)
                .load(list.get(p1))
                .placeholder(R.drawable.loading)
                .error(R.drawable.loading_error)
                .fit()
                .transform(RoundRectTransform(20F))
//                .transform(CircleTransform())
                .into(p0.image)
//        } else {
//            Picasso.with(p0.image.context)
//                    .load(list.get(p1))
//                    .placeholder(R.drawable.loading)
//                    .error(R.drawable.loading_error)
//                    .fit()
//                    .into(p0.image)
//        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int) = ViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.item_img, p0, false))

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image = view.findViewById(R.id.image) as ImageView
    }
}