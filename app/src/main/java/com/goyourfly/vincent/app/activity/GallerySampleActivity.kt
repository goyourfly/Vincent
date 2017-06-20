package com.goyourfly.vincent.app.activity

import com.goyourfly.vincent.common.logD
import com.goyourfly.vincent.app.R


class GallerySampleActivity : android.support.v7.app.AppCompatActivity() {
    val recycler: android.support.v7.widget.RecyclerView by lazy {
        findViewById(com.goyourfly.vincent.app.R.id.recycler) as android.support.v7.widget.RecyclerView
    }
    val adapter = com.goyourfly.vincent.app.activity.GallerySampleActivity.MyAdapter()
    val mLayoutManager = android.support.v7.widget.GridLayoutManager(this, 3);
    var loading = true
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.goyourfly.vincent.app.R.layout.activity_load_from_net)

        recycler.adapter = com.goyourfly.multiple.adapter.MultipleSelect.with(this)
                .adapter(adapter)
                .decorateFactory(com.goyourfly.multiple.adapter.viewholder.view.CheckBoxFactory())
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
    private fun getAllShownImagesPath(activity: android.app.Activity): ArrayList<String> {
        val uri: android.net.Uri
        val cursor: android.database.Cursor
        val column_index_data: Int
        val column_index_folder_name: Int
        val listOfAllImages = ArrayList<String>()
        var absolutePathOfImage: String? = null
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(android.provider.MediaStore.MediaColumns.DATA, android.provider.MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

        cursor = activity.contentResolver.query(uri, projection, null, null, null)

        column_index_data = cursor.getColumnIndexOrThrow(android.provider.MediaStore.MediaColumns.DATA)
        column_index_folder_name = cursor
                .getColumnIndexOrThrow(android.provider.MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data)

            listOfAllImages.add(absolutePathOfImage)
        }
        return listOfAllImages
    }

    class MyAdapter : android.support.v7.widget.RecyclerView.Adapter<MyAdapter.ViewHolder>() {
        val list = mutableListOf<String>()

        fun addItem(item: String) {
            list.add(item)
        }


        override fun onBindViewHolder(holder: com.goyourfly.vincent.app.activity.GallerySampleActivity.MyAdapter.ViewHolder, position: Int) {
            "onBindViewHolder:$position,${holder.image.hashCode()}".logD()
            com.goyourfly.vincent.Vincent.with(holder.image.context)
                    .load(list.get(position))
                    .placeholder(com.goyourfly.vincent.app.R.drawable.loading)
                    .error(com.goyourfly.vincent.app.R.drawable.loading_error)
                    .fit()
                    .transform(com.goyourfly.vincent.transform.RoundRectTransform(20F))
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

        override fun onCreateViewHolder(p0: android.view.ViewGroup?, p1: Int): com.goyourfly.vincent.app.activity.GallerySampleActivity.MyAdapter.ViewHolder {
            val view = com.goyourfly.vincent.app.activity.GallerySampleActivity.MyAdapter.ViewHolder(android.view.LayoutInflater.from(p0?.context).inflate(R.layout.item_img, p0, false))
            "onBindCreateViewHolder:${view.hashCode()}".logD()
            return view
        }

        class ViewHolder(view: android.view.View) : android.support.v7.widget.RecyclerView.ViewHolder(view) {
            val image = view.findViewById(com.goyourfly.vincent.app.R.id.image) as android.widget.ImageView
        }
    }
}
