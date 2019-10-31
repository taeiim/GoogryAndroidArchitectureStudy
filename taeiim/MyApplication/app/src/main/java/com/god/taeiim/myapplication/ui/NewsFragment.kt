package com.god.taeiim.myapplication.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.god.taeiim.myapplication.R
import com.god.taeiim.myapplication.api.SearchApi
import com.god.taeiim.myapplication.api.model.SearchResult
import com.god.taeiim.myapplication.api.provideAuthApi
import com.god.taeiim.myapplication.extensions.fromHtml
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.item_contents.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsFragment : Fragment() {
    private val api: SearchApi by lazy { provideAuthApi() }
    private val adapter = NewsAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        with(searchResultRecyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = this@NewsFragment.adapter
        }

        searchBtn.setOnClickListener {
            searchEditTv.text.toString()?.let { searchNews(it) }
        }
    }

    private fun searchNews(query: String) {
        val searchCall = api.searchContents("news", query)
        searchCall?.let {
            it.enqueue(object : Callback<SearchResult> {

                override fun onResponse(call: Call<SearchResult>, response: Response<SearchResult>) {
                    with(adapter) {
                        response.body()?.let { setItems(it.items as ArrayList<SearchResult.Item>) }
                            ?: clearItems()
                    }
                }

                override fun onFailure(call: Call<SearchResult>, t: Throwable) {

                }
            })
        }
    }

    private inner class NewsAdapter : RecyclerView.Adapter<NewsListHolder>() {
        private var resultList: ArrayList<SearchResult.Item> = ArrayList()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsListHolder =
            NewsListHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_contents, parent, false))

        override fun getItemCount(): Int {
            return resultList.size
        }

        fun setItems(items: ArrayList<SearchResult.Item>) {
            resultList = items
            notifyDataSetChanged()
        }

        fun clearItems(){
            resultList.clear()
            notifyDataSetChanged()
        }

        override fun onBindViewHolder(holder: NewsListHolder, position: Int) {
            resultList[position].let { news ->
                with(holder.itemView) {
                    titleTv.text = news.title!!.fromHtml()
                    subTitleTv.text = news.pubDate!!.fromHtml()
                    descTv.text = news.description!!.fromHtml()

                    setOnClickListener { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(news.link))) }
                }
            }
        }
    }

    private inner class NewsListHolder(view: View) : RecyclerView.ViewHolder(view)
}