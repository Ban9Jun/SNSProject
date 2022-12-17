package com.example.snsproject.navigation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.snsproject.R

class CustomAdapter(val list:List<PostListData>) : RecyclerView.Adapter<Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycle_post_list,parent,false)

        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val data = list[position]
        holder.setItem(data)
    }

    override fun getItemCount(): Int {
        return list.size
    }

}

class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){

    private val imgProfil : ImageView = itemView.findViewById(R.id.detailViewItem_profile_image)
    private val txtProfil : TextView = itemView.findViewById(R.id.detailViewItem_profile_textView)
    private val imgPost : ImageView = itemView.findViewById(R.id.detailViewItem_imageView_content)
    private val txtLike : TextView = itemView.findViewById(R.id.detailViewItem_favoriteCounter_textView)
    private val txtExplain : TextView = itemView.findViewById(R.id.detailViewItem_explain_textView)

    fun setItem(data: PostListData){

        Glide.with(itemView).load(data.profil_img).into(imgProfil)
        txtProfil.text = data.profil_text.toString()
        Glide.with(itemView).load(data.img).into(imgPost)
        txtLike.text = data.like_text.toString()
        txtExplain.text = data.explain_text.toString()

    }
}