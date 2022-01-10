package com.tuwaiq.halfway.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.tuwaiq.halfway.R
import com.tuwaiq.halfway.model.UserDetailModal
import java.util.*

class UsersAdapter     //creating a constructor.
    (
    private val userDetailModalArrayList: ArrayList<UserDetailModal>,
    private val context: Context,
    private val courseClickInterface: UserClickInterface
) : RecyclerView.Adapter<UsersAdapter.ViewHolder>() {
    var lastPos = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //inflating our layout file on below line.
        val view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        //setting data to our recycler view item on below line.
        val userDetailModal = userDetailModalArrayList[position]
        holder.tv_name.text = userDetailModal.name
        holder.tv_gender.text = "Gender : " + userDetailModal.gender
        holder.tv_age.text = "Age : " + userDetailModal.age
        //adding animation to recycler view item on below line.
        setAnimation(holder.itemView, position)
        holder.cv_row.setOnClickListener { courseClickInterface.onClick(position) }
    }

    private fun setAnimation(itemView: View, position: Int) {
        if (position > lastPos) {
            //on below line we are setting animation.
            val animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left)
            itemView.animation = animation
            lastPos = position
        }
    }

    override fun getItemCount(): Int {
        return userDetailModalArrayList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cv_row: CardView
        val tv_name: TextView
        val tv_gender: TextView
        val tv_age: TextView

        init {
            cv_row = itemView.findViewById(R.id.cv_row)
            tv_name = itemView.findViewById(R.id.tv_name)
            tv_gender = itemView.findViewById(R.id.tv_gender)
            tv_age = itemView.findViewById(R.id.tv_age)
        }
    }

    //creating a interface for on click
    interface UserClickInterface {
        fun onClick(position: Int)
    }
}