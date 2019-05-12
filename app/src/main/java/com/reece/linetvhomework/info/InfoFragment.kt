package com.reece.linetvhomework.info

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.reece.linetvhomework.R
import com.reece.linetvhomework.hide
import com.reece.linetvhomework.model.Model
import com.reece.linetvhomework.model.Repository
import com.reece.linetvhomework.show
import kotlinx.android.synthetic.main.activity_info.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class InfoFragment : Fragment() {

    private val mainScope: CoroutineScope by lazy {
        CoroutineScope(Dispatchers.Main + Job())
    }

    private lateinit var progressBar: ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.activity_info, container, false)

        progressBar = view.info_progressBar

        val dramaId = arguments?.getString("dramaId")

        // from deep link
        if (dramaId != null && dramaId.isNotEmpty() && dramaId.isNotBlank()) {
            view.info_scrollview.hide()
            progressBar.show()

            mainScope.launch {
                val drama = Repository.getDramaWithId(requireContext(), dramaId.toInt())

                progressBar.hide()
                if (drama != null) {
                    showDramaInfo(view, drama)
                } else {
                    view.info_message.show()
                    view.info_message.text = "Oops! Drama $dramaId is not exist :("
                }
            }
        } else {
            val drama = arguments?.getParcelable<Model.Drama>("drama")
            showDramaInfo(view, drama)
        }

        return view
    }

    private fun showDramaInfo(view: View, drama: Model.Drama?) {
        drama?.apply {
            view.info_scrollview.show()

            view.info_name.text = drama.name
            view.info_rating.rating = drama.rating
            view.info_rating_number.text = String.format("%.02f", drama.rating)

            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.TAIWAN)
            view.info_create_at.text = String.format("出版日期：%s", sdf.format(drama.created_at))

            view.info_views.text = String.format("觀看次數：%d", drama.total_views)

            Glide.with(this@InfoFragment)
                .load(drama.thumb)
                .centerCrop()
                .into(view.info_photo)
        }
    }

}