package com.example.snsproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PostListAdapter(private val context: HomeFragment) : RecyclerView.Adapter<PostListAdapter.ViewHolder>(){

    var datas = mutableListOf<PostListfileData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycle_post_list, parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val imgProfil : ImageView = itemView.findViewById(R.id.detailViewItem_profile_image)
        private val txtProfil : TextView = itemView.findViewById(R.id.detailViewItem_profile_textView)
        private val imgPost : ImageView = itemView.findViewById(R.id.detailViewItem_imageView_content)
        private val txtLike : TextView = itemView.findViewById(R.id.detailViewItem_favoriteCounter_textView)
        private val txtExplain : TextView = itemView.findViewById(R.id.detailViewItem_explain_textView)

        fun bind(item: PostListfileData) {

            Glide.with(itemView).load(item.profil_img).into(imgProfil)
            txtProfil.text = item.profil_text.toString()
            Glide.with(itemView).load(item.img).into(imgPost)
            txtLike.text = item.like_text.toString()
            txtExplain.text = item.explain_text.toString()
        }
    }
    data class PostListfileData(
        val profil_img: Int,
        val profil_text: String,
        val img: Int,
        val like_text: String,
        val explain_text: String
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val datas = datas[position]
        holder.bind(datas)
    }
}

