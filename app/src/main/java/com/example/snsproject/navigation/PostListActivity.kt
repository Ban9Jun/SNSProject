package com.example.snsproject.navigation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.snsproject.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class PostListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.post_list)

        var bnv_main = findViewById(R.id.bnv_main) as BottomNavigationView

        // OnNavigationItemSelectedListener를 통해 탭 아이템 선택 시 이벤트를 처리
        // navi_menu.xml 에서 설정했던 각 아이템들의 id를 통해 알맞은 프래그먼트로 변경하게 한다.
        bnv_main.run {
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.home -> {
                        // 다른 프래그먼트 화면으로 이동하는 기능
                        val homeFragment = HomeFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fl_container, homeFragment).commit()
                    }
                    R.id.user -> {
                        val profilFragment = ProfilFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fl_container, profilFragment).commit()
                    }
                    R.id.friend -> {
                        val friendFragment = FriendFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fl_container, friendFragment).commit()
                    }
                    R.id.post -> {
                        val postFragment = PostFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fl_container, postFragment).commit()
                    }
                }
                true
            }
            selectedItemId = R.id.home
        }

        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1)
    }
}