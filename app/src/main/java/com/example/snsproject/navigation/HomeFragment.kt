package com.example.snsproject.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.snsproject.R
import com.example.snsproject.R.layout
import com.example.snsproject.navigation.model.ContentDTO
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.NonDisposableHandle.parent


class HomeFragment : Fragment() {

    lateinit var recyclerView : RecyclerView

    var firestore : FirebaseFirestore? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(layout.fragment_home, container, false)
        firestore = FirebaseFirestore.getInstance()
        recyclerView = view.findViewById(com.example.snsproject.R.id.rcpostlist)
        recyclerView.adapter = DetailViewRecycleerViewAdapter()
        recyclerView.layoutManager = LinearLayoutManager(context)

        return view
    }

    inner class DetailViewRecycleerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var contentDTOs: ArrayList<ContentDTO> = arrayListOf()
        var contentUidList: ArrayList<String> = arrayListOf()

        init {
            firestore?.collection("images")?.orderBy("timestamp")
                ?.addSnapshotListener { querySnapshot, error ->
                    contentDTOs.clear()
                    contentUidList.clear()
                    for (snapshot in querySnapshot!!.documents) {
                        var item = snapshot.toObject(ContentDTO::class.java)
                        contentDTOs.add(item!!)
                        contentUidList.add(snapshot.id)
                    }
                    notifyDataSetChanged()
                }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(com.example.snsproject.R.layout.recycle_post_list,parent,false)

            return CustomViewHolder(view)
        }

        inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var viewholder = (holder as CustomViewHolder).itemView

            val imgProfil : ImageView = viewholder.findViewById(R.id.detailViewItem_profile_image)
            val txtProfil : TextView = viewholder.findViewById(R.id.detailViewItem_profile_textView)
            val imgPost : ImageView = viewholder.findViewById(R.id.detailViewItem_imageView_content)
            val txtLike : TextView = viewholder.findViewById(R.id.detailViewItem_favoriteCounter_textView)
            val txtExplain : TextView = viewholder.findViewById(R.id.detailViewItem_explain_textView)

            txtExplain.text = contentDTOs!![position].explain
            txtProfil.text = contentDTOs!![position].userId
            Glide.with(holder.itemView.context).load(contentDTOs!![position].imageUri).into(imgPost)
            txtLike.text = "Likes " + contentDTOs!![position].favoriteCount
            Glide.with(holder.itemView.context).load(contentDTOs!![position].imageUri).into(imgProfil)

            imgProfil.setOnClickListener {
                var fragment = ProfilFragment()
                var bundle = Bundle()
                bundle.putString("destinationUid",contentDTOs[position].uid)
                bundle.putString("userId",contentDTOs[position].userId)
                fragment.arguments = bundle
                activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fl_container,fragment)?.commit()
            }
        }

        override fun getItemCount(): Int {
            return contentDTOs.size
        }


    }

}



