package com.reece.linetvhomework.info

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.reece.linetvhomework.R
import com.reece.linetvhomework.model.Model
import kotlinx.android.synthetic.main.activity_info.*
import java.text.SimpleDateFormat
import java.util.*

class InfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        val drama = intent.getParcelableExtra<Model.Drama>("drama")
        drama?.apply {
            supportActionBar?.title = drama.name
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            info_name.text = drama.name
            info_rating.rating = drama.rating
            info_rating_number.text = String.format("%.02f", drama.rating)

            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.TAIWAN)
            info_create_at.text = String.format("出版日期：%s", sdf.format(drama.created_at))

            info_views.text = String.format("觀看次數：%d", drama.total_views )

            Glide.with(this@InfoActivity)
                .load(drama.thumb)
                .centerCrop()
                .into(info_photo)

        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}