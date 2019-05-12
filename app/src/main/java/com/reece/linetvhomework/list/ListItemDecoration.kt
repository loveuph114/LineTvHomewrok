package com.reece.linetvhomework.list

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import com.reece.linetvhomework.dipToPixels

class ListItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val space = view.dipToPixels(8f).toInt()
        val position = parent.getChildAdapterPosition(view)

        if (position == 0 || position == 1) {
            outRect.top = space
        }

        if (position % 2 == 0) {
            outRect.left = space
            outRect.right = space / 2
        } else {
            outRect.left = space / 2
            outRect.right = space
        }

        outRect.bottom = space
    }
}