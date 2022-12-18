package com.example.snsproject.navigation

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.app.NotificationCompat.getColor
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.snsproject.LoginActivity
import com.example.snsproject.MainActivity
import com.example.snsproject.R
import com.example.snsproject.navigation.model.ContentDTO
import com.example.snsproject.navigation.model.FollowDTO
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage

class ProfilFragment : Fragment() {
    var fragmentView : View? = null
    var firestore : FirebaseFirestore? = null
    var uid : String? = null
    var auth : FirebaseAuth? = null
    var currentUserid : String? = null

    lateinit var reyclerview : RecyclerView
    lateinit var account_tv_post_count : TextView
    lateinit var account_btn_follow_signout : Button
    lateinit var account_iv_profile : ImageView
    lateinit var account_tv_following_count : TextView
    lateinit var account_tv_follower_count : TextView
    lateinit var toolbar_username : TextView
    lateinit var toolbar_btn_back : ImageView
    lateinit var toolbar_title : TextView
    lateinit var bnv_main : BottomNavigationView
    companion object{
        var PICK_PROFILE_FROM_ALBUM = 10
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        fragmentView = LayoutInflater.from(activity).inflate(com.example.snsproject.R.layout.fragment_profil,container,false)
        val view = LayoutInflater.from(activity).inflate(com.example.snsproject.R.layout.fragment_profil,container,false)
        val view2 = LayoutInflater.from(activity).inflate(com.example.snsproject.R.layout.post_list,container,false)

        uid = arguments?.getString("destinationUid")
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        currentUserid = auth?.currentUser?.uid

        reyclerview = view.findViewById<RecyclerView>(com.example.snsproject.R.id.account_reyclerview)
        account_tv_post_count = view.findViewById<TextView>(com.example.snsproject.R.id.account_tv_post_count)
        account_btn_follow_signout = view.findViewById<Button>(com.example.snsproject.R.id.account_btn_follow_signout)
        account_iv_profile = view.findViewById(com.example.snsproject.R.id.account_iv_profile)
        account_tv_following_count = view.findViewById(com.example.snsproject.R.id.account_tv_following_count)
        account_tv_follower_count = view.findViewById(com.example.snsproject.R.id.account_tv_follower_count)
        toolbar_username = view2.findViewById<TextView>(com.example.snsproject.R.id.toolbar_username)
        toolbar_btn_back = view2.findViewById<ImageView>(com.example.snsproject.R.id.toolbar_btn_back)
        toolbar_title = view2.findViewById<TextView>(com.example.snsproject.R.id.toolbar_title)
        bnv_main = view2.findViewById<BottomNavigationView>(com.example.snsproject.R.id.bnv_main)

        if(uid == currentUserid){
            //MyPage
            account_btn_follow_signout.text = getString(R.string.signout)
            account_btn_follow_signout.setOnClickListener {
                activity?.finish()
                startActivity(Intent(activity,LoginActivity::class.java))
                auth?.signOut()
            }
        }else{
            //OtherUserPage
            account_btn_follow_signout.text = "follow"
            Log.i(account_btn_follow_signout.text.toString(),"button")
            toolbar_username.text = arguments?.getString("userId")
            toolbar_btn_back.setOnClickListener{
                bnv_main.selectedItemId = R.id.home
            }
            toolbar_title?.visibility = View.GONE
            toolbar_username?.visibility = View.VISIBLE
            toolbar_btn_back?.visibility = View.VISIBLE
            account_btn_follow_signout?.setOnClickListener {
                requestFollow()
            }
        }

        reyclerview.adapter = UserFragmentRecyclerViewAdapter()
        reyclerview.layoutManager = GridLayoutManager(requireActivity(),3)

        account_iv_profile.setOnClickListener {
            var photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            activity?.startActivityForResult(photoPickerIntent,PICK_PROFILE_FROM_ALBUM)
        }
        getFolloweAndFollowing()
        getProfileImage()
        return view
    }

    fun getFolloweAndFollowing(){
        firestore?.collection("users")?.document(uid!!)?.addSnapshotListener { value, error ->
            if(value == null) return@addSnapshotListener
            var followDTO = value.toObject(FollowDTO::class.java)
            if(followDTO?.followingCount != null){
                account_tv_following_count?.text = followDTO?.followingCount?.toString()
            }
            if(followDTO?.followerCount != null){
                account_tv_follower_count?.text = followDTO.followerCount?.toString()
                if(followDTO?.followers?.containsKey(currentUserid!!)!!){
                    account_btn_follow_signout?.text = "follow cancel"
                }else{
                    account_btn_follow_signout?.text = "follow"
                    if(uid == currentUserid) account_btn_follow_signout.text = "singout"
                }
            }
        }
    }
    fun requestFollow(){
        //save data to my account
        var tsDocFollowing = firestore?.collection("users")?.document(currentUserid!!)
        firestore?.runTransaction { transaction ->
            var followDTO = transaction.get(tsDocFollowing!!).toObject(FollowDTO::class.java)
            if(followDTO == null){
                followDTO = FollowDTO()
                followDTO!!.followingCount = 1
                followDTO!!.followers[uid!!] = true
                transaction.set(tsDocFollowing,followDTO)
                return@runTransaction
            }
            if(followDTO.followings.containsKey(uid)){
                followDTO?.followingCount = followDTO?.followingCount!! - 1
                followDTO?.followings?.remove(uid)
            }else{
                followDTO?.followingCount = followDTO?.followingCount!! + 1
                followDTO?.followings!![uid!!] = true
            }
            transaction.set(tsDocFollowing,followDTO)
            return@runTransaction
        }
        //Save data to third person
        var tsDocFollower = firestore?.collection("users")?.document(uid!!)
        firestore?.runTransaction { transaction ->
            var followDTO = transaction.get(tsDocFollower!!).toObject(FollowDTO::class.java)
            if(followDTO == null){
                followDTO = FollowDTO()
                followDTO!!.followerCount = 1
                followDTO!!.followers!![currentUserid!!] = true

                transaction.set(tsDocFollower,followDTO!!)
                return@runTransaction
            }
            if(followDTO!!.followers.containsKey(currentUserid)){
                followDTO!!.followerCount = followDTO!!.followerCount - 1
                followDTO!!.followers.remove(currentUserid!!)
            }else{
                followDTO!!.followerCount = followDTO!!.followerCount + 1
                followDTO!!.followers[currentUserid!!] = true
            }
            transaction.set(tsDocFollower,followDTO!!)
            return@runTransaction
        }
    }
    @SuppressLint("UseRequireInsteadOfGet")
    fun getProfileImage(){
        firestore?.collection("profileImages")?.document(uid!!)?.addSnapshotListener { value, error ->
            if(value == null) return@addSnapshotListener
            if(value.data != null){
                var uri = value?.data!!["image"]
                Glide.with(activity!!).load(uri).apply(RequestOptions().circleCrop()).into(account_iv_profile)
            }
        }
    }
    inner class UserFragmentRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        var contentDTOs : ArrayList<ContentDTO> = arrayListOf()

        init{
            firestore?.collection("images")?.whereEqualTo("uid",uid)?.addSnapshotListener { querySnapshot, error ->
                if(querySnapshot == null) return@addSnapshotListener

                for(snapshot in querySnapshot.documents){
                    contentDTOs.add(snapshot.toObject(ContentDTO::class.java)!!)
                }
                account_tv_post_count.text = contentDTOs.size.toString()

                notifyDataSetChanged()
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var width = resources.displayMetrics.widthPixels / 3

            var imageview = ImageView(parent.context)
            imageview.layoutParams = LinearLayoutCompat.LayoutParams(width,width)
            return CustomViewHolder(imageview)
        }

        inner class CustomViewHolder(var imageview: ImageView) : RecyclerView.ViewHolder(imageview)

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var imgview = (holder as CustomViewHolder).imageview

            Glide.with(holder.itemView.context).load(contentDTOs[position].imageUri).apply(RequestOptions().centerCrop()).into(imgview)
        }

        override fun getItemCount(): Int {
            return contentDTOs.size
        }
    }

}