package com.goyourfly.vincent.app.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.goyourfly.vincent.Vincent
import com.goyourfly.vincent.app.R
import com.goyourfly.vincent.scale.CircleCenterCrop
import com.goyourfly.vincent.scale.RoundRectCenterCrop
import com.goyourfly.vincent.transform.RoundRectTransform

/**
 * Created by Administrator on 2017/6/18 0018.
 */
class MyStaggeredAdapter:RecyclerView.Adapter<MyStaggeredAdapter.ViewHolder>() {

    val list = mutableListOf<String>()

    fun addItem(item: String) {
        list.add(item)
    }

    fun addItems(items: List<String>) {
        list.addAll(items)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.image.tag = position
        Vincent.with(holder.image.context)
                .load(list.get(position))
                .placeholder(R.drawable.loading)
                .error(R.drawable.loading_error)
                .scaleType(RoundRectCenterCrop(20))
                .notFit()
                .into(holder.image)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int)
            = ViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.item_img2, p0, false))

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image = view.findViewById(R.id.image) as ImageView
    }
}