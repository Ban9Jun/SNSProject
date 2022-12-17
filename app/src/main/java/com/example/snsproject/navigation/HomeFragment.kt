package com.example.snsproject.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class HomeFragment : Fragment() {

    lateinit var recyclerView : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(com.example.snsproject.R.layout.fragment_home, container, false)
        recyclerView = view.findViewById(com.example.snsproject.R.id.rcpostlist)

        val adapter = CustomAdapter(loadData())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        return view
    }

    fun loadData() : List<PostListData>{
        val list = mutableListOf<PostListData>()

        for(i in 0..100){
            val currentTime = System.currentTimeMillis()
            val datas = PostListData(com.example.snsproject.R.drawable.home,"${i}",
                com.example.snsproject.R.drawable.home,"${i}","${i}")
            list.add(datas)

        }

        return list
    }
}



