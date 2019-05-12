package com.reece.linetvhomework.list

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.findNavController
import com.reece.linetvhomework.R
import com.reece.linetvhomework.hide
import com.reece.linetvhomework.model.Model
import com.reece.linetvhomework.model.Repository
import com.reece.linetvhomework.show
import kotlinx.android.synthetic.main.fragment_list.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.jetbrains.anko.support.v4.toast

class ListFragment : Fragment() {

    companion object {
        private const val SEARCH_PREF = "searchPref"
        private const val SEARCH_KEYWORD = "searchKeyword"
    }

    private val mainScope: CoroutineScope by lazy {
        CoroutineScope(Dispatchers.Main + Job())
    }

    data class MessageStored(val message: String, val visibility: Int)

    private lateinit var storedMessage: MessageStored
    private var storedSearch = ""

    private var data = arrayListOf<Model.Drama>()
    private var searchResult = arrayListOf<Model.Drama>()

    private val listAdapter = ListAdapter()
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    private lateinit var messageView: TextView
    private lateinit var searchInput: EditText
    private lateinit var searchClearBtn: ImageButton


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        recyclerView = view.list_recyclerview
        recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = listAdapter
            addItemDecoration(ListItemDecoration())
        }

        listAdapter.listener = object : ListAdapter.OnListItemClickListener {
            override fun onClick(drama: Model.Drama) {
                val bundle = Bundle()
                bundle.putParcelable("drama", drama)

                view.findNavController().navigate(R.id.list_to_info, bundle)
            }
        }

        progressBar = view.list_progressbar

        messageView = view.list_message
        messageView.hide()

        searchInput = view.list_search_input
        searchClearBtn = view.list_search_clear
        setupSearch()

        return view
    }

    override fun onResume() {
        super.onResume()
        if (data.isNotEmpty()) {
            return
        }

        mainScope.launch {
            progressBar.show()
            val result = Repository.getDramas(requireContext())
            val dramas = result.first
            val isNetworkResult = result.second

            progressBar.hide()
            if (dramas != null) {
                data = dramas.data

                listAdapter.data.addAll(data)
                listAdapter.notifyDataSetChanged()

                if (!isNetworkResult) {
                    toast("No network, showing offline data.")
                }
            } else {
                val message = if (isNetworkResult) {
                    "Oops! Something wrong :(\nPlease check your network status or try again later."
                } else {
                    "Oops! You are Offline!\nThere is no offline data can show,\nPlease check your network status."
                }

                messageView.text = message
                messageView.show()
            }

            storeMessage()
            restoreSearch()
        }
    }

    private fun setupSearch() {
        searchInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                // do nothing
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.apply {
                    if (isNotEmpty() && isNotBlank()) {
                        storeSearch(s)

                        searchResult.clear()
                        searchResult.addAll(
                            data.filter {
                                it.name.contains(s)
                            }
                        )

                        listAdapter.data.clear()
                        listAdapter.data.addAll(searchResult)
                        listAdapter.notifyDataSetChanged()

                        if (searchResult.isEmpty()) {
                            messageView.text = "No match drama :("
                            messageView.show()
                        } else {
                            messageView.text = ""
                            messageView.hide()
                        }

                    } else {
                        storeSearch("")
                        restoreMessage()

                        listAdapter.data.clear()
                        listAdapter.data.addAll(data)
                        listAdapter.notifyDataSetChanged()
                    }
                }
            }
        })

        searchClearBtn.setOnClickListener {
            searchInput.setText("")
            storeSearch("")
        }
    }

    private fun storeMessage() {
        storedMessage = MessageStored(messageView.text.toString(), messageView.visibility)
    }

    private fun restoreMessage() {
        messageView.text = storedMessage.message
        messageView.visibility = storedMessage.visibility
    }

    private fun storeSearch(s: CharSequence) {
        val pref = requireContext().getSharedPreferences(SEARCH_PREF, Context.MODE_PRIVATE)
        pref.edit()
            .putString(SEARCH_KEYWORD, s.toString())
            .apply()
    }

    private fun restoreSearch() {
        val pref = requireContext().getSharedPreferences(SEARCH_PREF, Context.MODE_PRIVATE)
        storedSearch = pref.getString(SEARCH_KEYWORD, "") ?: ""

        if(storedSearch.isNotEmpty() && storedSearch.isNotBlank()) {
            searchInput.setText(storedSearch)
        }
    }
}