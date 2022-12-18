package com.example.snsproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.snsproject.navigation.PostListActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {
    private var firebaseAuth : FirebaseAuth? = null
    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var GoRegisterbtn: Button
    lateinit var Loginbtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        firebaseAuth = FirebaseAuth.getInstance()
        email = findViewById<EditText>(R.id.email)
        password = findViewById<EditText>(R.id.password)
        GoRegisterbtn = findViewById<Button>(R.id.GoRegisterbtn)
        Loginbtn = findViewById<Button>(R.id.Loginbtn)

        //로그인 버튼
        Loginbtn.setOnClickListener {
            signinEmail()
        }

        //회원가입 이동
        GoRegisterbtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        moveMainPage(firebaseAuth?.currentUser)
    }

    fun moveMainPage(user:FirebaseUser?){
        if(user != null){
            intent = Intent(this, PostListActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    //로그인 기능
    fun signinEmail() {
        firebaseAuth?.signInWithEmailAndPassword(email.text.toString(),password.text.toString())
            ?.addOnCompleteListener {
                    task ->
                if(task.isSuccessful) {
                    // Login, 아이디와 패스워드가 맞았을 때
                    moveMainPage(task.result?.user)
                } else {
                    // Show the error message, 아이디와 패스워드가 틀렸을 때
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                }
            }
    }
}