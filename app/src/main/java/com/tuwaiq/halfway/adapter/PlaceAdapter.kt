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
import com.tuwaiq.halfway.model.Result
import java.lang.Exception
import java.net.URL
import java.util.concurrent.ExecutionException

class PlaceAdapter
    (
    private val context: Context,
    private val userDetailModalArrayList: List<Result>,
    private  val onItemClicked:(Result)->Unit
    /*, PlaceClickInterface placeClickInterface*/
) : RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {
    private val courseClickInterface: PlaceClickInterface? = null
    var lastPos = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //inflating our layout file on below line.
        val view = LayoutInflater.from(context).inflate(R.layout.item_mid_location, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        //setting data to our recycler view item on below line.
        val (_, _, icon, _, _, name, _, photos, _, _, _, _, _, _, _, _, vicinity) = userDetailModalArrayList[position]
        val key = context.getText(R.string.google_maps_key).toString()
        var imagePath = ""
        imagePath =
            if (photos != null) "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + photos[0]!!.photo_reference + "&sensor=false&key=" + key else icon
        var photo: Bitmap? = null
        try {
            photo = ImageRequestAsk().execute(imagePath).get()
            holder.iv_place.setImageBitmap(photo)
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        val status=  userDetailModalArrayList[position].opening_hours?.let {
           if (it.open_now){
            context.getString(R.string.open_now)
        }else{
               context.getString(R.string.close)
        }
        }?:context.getString(R.string.close)

        holder.tvOpening_hours.text = status
        holder.tvStars.text = "${userDetailModalArrayList[position].rating}"

        holder.tv_placeName.text = name
        holder.tv_placeName.text = name
        holder.tv_address.text = vicinity
        setAnimation(holder.itemView, position)
    //    holder.cv_row.setOnClickListener { courseClickInterface!!.onClick(position) }
        holder.cv_row.setOnClickListener { onItemClicked!!(userDetailModalArrayList[position]) }
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
        val iv_place: ImageView
        val tv_placeName: TextView
        val tv_address: TextView
        val tvOpening_hours: TextView
        val tvStars: TextView

        init {
            tvOpening_hours = itemView.findViewById(R.id.tvOpening_hours)
            tvStars = itemView.findViewById(R.id.tvStars)
            cv_row = itemView.findViewById(R.id.cv_row)
            iv_place = itemView.findViewById(R.id.iv_place)
            tv_placeName = itemView.findViewById(R.id.tv_placeName)
            tv_address = itemView.findViewById(R.id.tv_address)
        }
    }

    //creating a interface for on click
    interface PlaceClickInterface {
        fun onClick(position: Int)
    }

    private inner class ImageRequestAsk : AsyncTask<String?, Void?, Bitmap?>() {
        override fun doInBackground(vararg params: String?): Bitmap? {
            return try {
                val inputStream = URL(params[0]).openStream()
                BitmapFactory.decodeStream(inputStream)
            } catch (e: Exception) {
                null
            }
        }
    }
}