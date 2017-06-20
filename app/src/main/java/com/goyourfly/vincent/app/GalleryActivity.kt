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
import com.goyourfly.vincent.Vincent
import com.goyourfly.vincent.common.logD
import com.goyourfly.vincent.transform.RoundRectTransform
import android.provider.MediaStore
import android.provider.MediaStore.MediaColumns
import android.app.Activity
import android.database.Cursor
import android.net.Uri
import com.goyourfly.multiple.adapter.viewholder.view.CheckBoxFactory


class GalleryActivity : AppCompatActivity() {
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


        for (str in getAllShownImagesPath(this)){
            adapter.addItem(str)
        }
        adapter.notifyItemRangeInserted(0, adapter.itemCount)
    }

    /**
     * Getting All Images Path.

     * @param activity
     * *            the activity
     * *
     * @return ArrayList with images Path
     */
    private fun getAllShownImagesPath(activity: Activity): ArrayList<String> {
        val uri: Uri
        val cursor: Cursor
        val column_index_data: Int
        val column_index_folder_name: Int
        val listOfAllImages = ArrayList<String>()
        var absolutePathOfImage: String? = null
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

        cursor = activity.contentResolver.query(uri, projection, null, null, null)

        column_index_data = cursor.getColumnIndexOrThrow(MediaColumns.DATA)
        column_index_folder_name = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data)

            listOfAllImages.add(absolutePathOfImage)
        }
        return listOfAllImages
    }

    class MyAdapter : RecyclerView.Adapter<MyAdapter.ViewHolder>() {
        val list = mutableListOf<String>()

        fun addItem(item: String) {
            list.add(item)
        }


        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            "onBindViewHolder:$position,${holder.image.hashCode()}".logD()
            Vincent.with(holder.image.context)
                    .load(list.get(position))
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.loading_error)
                    .fit()
                    .transform(RoundRectTransform(20F))
                    .into(holder.image)

//            Picasso.with(holder.image.context)
//                    .load(list.get(position))
//                    .placeholder(R.drawable.loading)
//                    .error(R.drawable.loading_error)
//                    .fit()
//                    .into(holder.image)
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
