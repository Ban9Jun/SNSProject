package com.example.snsproject

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Date
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {
    private var firebaseAuth : FirebaseAuth? = null
    lateinit var email : EditText
    lateinit var password : EditText
    lateinit var passwordcheck : EditText
    lateinit var Registerbtn : Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        firebaseAuth = FirebaseAuth.getInstance()

        email = findViewById<EditText>(R.id.email)
        password = findViewById<EditText>(R.id.password)
        passwordcheck = findViewById<EditText>(R.id.passwordcheck)
        Registerbtn = findViewById<Button>(R.id.Registerbtn)

        // 회원가입 버튼 내용
        Registerbtn.setOnClickListener {
            createEmail()
        }
    }

    // 아이디 생성
    private fun createEmail(){

        //비밀번호 일치 확인
        if(password.text.toString() != passwordcheck.text.toString()){
            Toast.makeText(this,"비밀번호가 일치하지 않음",Toast.LENGTH_SHORT).show()
        }
        else {
            // 이메일,비밀번호 등록
            firebaseAuth!!.createUserWithEmailAndPassword(
                email.text.toString(),
                password.text.toString()
            )
                .addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        var user = firebaseAuth?.currentUser
                        Toast.makeText(this, "회원가입 완료", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "이미있는 아이디 입니다.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

}