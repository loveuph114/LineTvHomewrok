package com.reece.linetvhomework.list

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.reece.linetvhomework.R
import com.reece.linetvhomework.hide
import com.reece.linetvhomework.model.Repository
import com.reece.linetvhomework.show
import kotlinx.android.synthetic.main.fragment_list.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.jetbrains.anko.support.v4.toast

class ListFragment : Fragment() {

    private val mainScope: CoroutineScope by lazy {
        CoroutineScope(Dispatchers.Main + Job())
    }

    private val listAdapter = ListAdapter()
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var messageView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        recyclerView = view.list_recyclerview
        recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = listAdapter
            addItemDecoration(ListItemDecoration())
        }

        progressBar = view.list_progressbar
        progressBar.show()

        messageView = view.list_message
        messageView.hide()

        mainScope.launch {
            val result = Repository.getDramas(view.context)
            val dramas = result.first
            val isNetworkResult = result.second

            progressBar.hide()

            if (dramas != null) {
                listAdapter.data.addAll(dramas.data)
                listAdapter.notifyDataSetChanged()

                if(!isNetworkResult) {
                    toast("No network, showing offline data.")
                }
            } else {
                val message = if(isNetworkResult) {
                    "Oops! Something wrong :(\nPlease check your network status or try again later."
                } else {
                    "Oops! You are Offline!\nThere is no offline data can show,\nPlease check your network status."
                }

                messageView.text = message
                messageView.show()
            }
        }

        return view
    }
}