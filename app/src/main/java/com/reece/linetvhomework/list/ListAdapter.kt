package com.reece.linetvhomework.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.reece.linetvhomework.R
import com.reece.linetvhomework.model.Model

class ListAdapter : RecyclerView.Adapter<ListViewHolder>() {

    val data = arrayListOf<Model.Drama>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_drama_grid, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(viewHolder: ListViewHolder, position: Int) {
        viewHolder.bind(data[position])
    }
}