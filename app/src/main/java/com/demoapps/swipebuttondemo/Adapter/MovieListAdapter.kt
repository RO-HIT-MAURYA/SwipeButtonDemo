package com.demoapps.swipebuttondemo.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.demoapps.swipebuttondemo.Models.Result
import com.demoapps.swipebuttondemo.R

class MovieListAdapter(val context: Context, val movieList: List<Result>):
    RecyclerView.Adapter<MovieListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_movie_list, parent, false))
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    override fun onBindViewHolder(holder: MovieListAdapter.ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tv_title = itemView.findViewById<TextView>(R.id.tv_title)
        var tv_overview = itemView.findViewById<TextView>(R.id.tv_overview)
        var tv_release_date = itemView.findViewById<TextView>(R.id.tv_release_date)
        fun bind(position: Int){
            val list = movieList[position]
            tv_title.text = list.title
            tv_overview.text = list.overview
            tv_release_date.text = list.release_date
        }
    }
}