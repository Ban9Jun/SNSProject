package com.example.snsproject.navigation

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.snsproject.R
import com.example.snsproject.navigation.model.ContentDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat

class PostFragment : Fragment() {

    lateinit var call_img_btn : Button
    lateinit var imgview : ImageView
    lateinit var post_text : EditText
    private var viewProfile : View? = null
    var pickImageFromAlbum = 0
    var fbStorage : FirebaseStorage? =null
    var uriPhoto : Uri? = null
    var auth : FirebaseAuth? =null
    var firestore : FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewProfile = inflater.inflate(R.layout.fragment_post, container, false)
        val view = inflater.inflate(R.layout.fragment_post, container, false)

        fbStorage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        imgview = view.findViewById<ImageView>(R.id.imgview)
        call_img_btn = view.findViewById<Button>(R.id.call_img_btn)
        post_text = view.findViewById<EditText>(R.id.post_text)

        call_img_btn.setOnClickListener {
            var photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, pickImageFromAlbum)
        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == pickImageFromAlbum){
            if(resultCode == Activity.RESULT_OK){
                uriPhoto = data?.data
                imgview.setImageURI(uriPhoto)

                if(ContextCompat.checkSelfPermission(viewProfile!!.context,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    funImageUpload(viewProfile!!)
                }
            }
            else{

            }
        }
    }
    private fun funImageUpload(view:View){

        var timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(java.util.Date())
        var imgFileName = "IMAGE_" + timeStamp + "_.png"
        var storageRef = fbStorage?.reference?.child("images")?.child(imgFileName)

        storageRef?.putFile(uriPhoto!!)?.addOnSuccessListener {
            storageRef?.downloadUrl?.addOnSuccessListener { uri ->
                var contentDTO = ContentDTO()

                contentDTO.imageUri = uri.toString()

                contentDTO.uid = auth?.currentUser?.uid

                contentDTO.userId = auth?.currentUser?.email

                contentDTO.explain = post_text.text.toString()

                contentDTO.timestamp = System.currentTimeMillis()

                firestore?.collection("images")?.document()?.set(contentDTO)

            }
        }
    }
}