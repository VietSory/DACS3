package com.example.dacs3

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.dacs3.databinding.LoginActivityBinding
import com.example.dacs3.databinding.SignActivityBinding
import com.example.dacs3.model.UserModel
import com.example.dacs3.ui.theme.DACS3Theme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

@Suppress("DEPRECATION")
class signActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var userName: String
    private lateinit var database: DatabaseReference
    private val binding: SignActivityBinding by lazy {
        SignActivityBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // initialize Firebase Auth
        auth = Firebase.auth
        //initialize Firebase database
        database= Firebase.database.reference

        binding.btnSign.setOnClickListener {
            email = binding.edtmail.text.toString().trim()
            userName = binding.edtname.text.toString().trim()
            password = binding.edtpass.text.toString().trim()

            if(userName.isBlank() || email.isBlank() || password.isBlank()){
                Toast.makeText(this,"Hãy Điền Đầy Đủ Thông Tin", Toast.LENGTH_SHORT).show()
            } else {
                createAccount(email,password)
            }
        }
        binding.tvHaveAcc.setOnClickListener {
            val intent=Intent(this,loginActivity::class.java)
            startActivity(intent)
        }
    }
    private fun createAccount(email: String, passworld: String) {
        auth.createUserWithEmailAndPassword(email,passworld).addOnCompleteListener { task->
            if (task.isSuccessful){
                Toast.makeText(this,"Tạo Tài khoản thành công",Toast.LENGTH_SHORT).show()
                saveUserData()
                val intent=Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this,"Tạo Tài khoản thất bại",Toast.LENGTH_SHORT).show()
                Log.d("Tài khoản","Tạo Thất bại",task.exception)
            }
        }

    }
    //save data into database
    private fun saveUserData(){
        email = binding.edtmail.text.toString().trim()
        userName = binding.edtname.text.toString().trim()
        password = binding.edtpass.text.toString().trim()
        val user= UserModel(userName,email,password)
        val userId=FirebaseAuth.getInstance().currentUser!!.uid
        //save user date
        database.child("user").child(userId).setValue(user)
    }
}

