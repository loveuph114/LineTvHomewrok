package com.reece.linetvhomework.list

import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.reece.linetvhomework.model.Model
import kotlinx.android.synthetic.main.item_drama_grid.view.*
import java.text.SimpleDateFormat
import java.util.*

class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(drama: Model.Drama) {
        itemView.apply {
            drama_grid_name.text = drama.name
            drama_grid_rating.rating = drama.rating
            drama_grid_rating_number.text = String.format("%.02f", drama.rating)

            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.TAIWAN)
            drama_grid_create_at.text = String.format("出版日期：%s", sdf.format(drama.created_at))

            Glide.with(itemView)
                .load(drama.thumb)
                .centerCrop()
                .into(drama_grid_photo)
        }
    }
}