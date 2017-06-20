package com.goyourfly.vincent.app

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.goyourfly.vincent.Vincent
import com.goyourfly.vincent.scale.CircleCenterCrop
import com.goyourfly.vincent.scale.RoundRectCenterCrop
import com.goyourfly.vincent.transform.RoundRectTransform

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


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        if (position % 2 == 0) {
        holder.image.tag = position
        holder.text.text = "$position"
        Vincent.with(holder.image.context)
                .load(list.get(position))
                .placeholder(R.drawable.loading)
                .error(R.drawable.loading_error)
                .scale(RoundRectCenterCrop(20))
//                .scale(CircleCenterCrop())
                .fit()
//                .transform(RoundRectTransform(20F))
//                .transform(CircleTransform())
                .into(holder.image)
//        } else {
//            Picasso.with(holder.image.context)
//                    .load(list.get(position))
//                    .placeholder(R.drawable.loading)
//                    .error(R.drawable.loading_error)
//                    .fit()
//                    .into(holder.image)
//        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int) = ViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.item_img, p0, false))

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image = view.findViewById(R.id.image) as ImageView
        val text = view.findViewById(R.id.text) as TextView
    }
}