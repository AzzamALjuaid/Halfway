package com.tuwaiq.halfway.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.tuwaiq.halfway.R
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import androidx.cardview.widget.CardView
import android.widget.TextView
import android.os.AsyncTask
import android.graphics.BitmapFactory
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.tuwaiq.halfway.databinding.ItemTabsBinding
import com.tuwaiq.halfway.model.Result
import java.lang.Exception
import java.net.URL
import java.util.concurrent.ExecutionException

class TabsAdapter
    (
    private val context: Context,
    private val tabs: List<String>,
    private val onItemClicked: (item:String)->Unit
) : RecyclerView.Adapter<TabsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_tabs, parent, false))
    }

    var selectedPostion=0;

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {

        holder.tvTab.apply {
            isSelected=selectedPostion==position
            text= tabs[position]

            if(selectedPostion==position){
                onItemClicked(tabs[position])
            }


            setOnClickListener {
                selectedPostion=position
                notifyDataSetChanged()
            }
        }
    }



    override fun getItemCount(): Int {
        return tabs.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTab: TextView = itemView.findViewById(R.id.tvTab)
    }


}