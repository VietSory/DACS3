package com.example.dacs3

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.dacs3.databinding.LoginActivityBinding
import com.example.dacs3.model.UserModel
import com.example.dacs3.ui.theme.DACS3Theme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

@Suppress("DEPRECATION")
class loginActivity : ComponentActivity() {
    private lateinit var email:String
    private lateinit var password:String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private val binding: LoginActivityBinding by lazy {
        LoginActivityBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = Firebase.auth
        database = Firebase.database.reference

        binding.btnLog.setOnClickListener {
            email = binding.edtmail.text.toString().trim()
            password=binding.edtpass.text.toString().trim()
            if (email.isBlank() || password.isBlank()){
                Toast.makeText(this,"Hãy điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            } else {
                loginUser(email,password)
            }

        }
        binding.tvNotAcc.setOnClickListener {
            val intent=Intent(this,signActivity::class.java)
            startActivity(intent)
        }
    }
    private fun loginUser(email :String,password:String){
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener{task->
            if (task.isSuccessful){
                val user=auth.currentUser
                updateUI(user)
            } else{
                Toast.makeText(this,"Thông tin Đăng nhập không đúng hoặc tài khoản không tồn tại", Toast.LENGTH_LONG).show()
            }
        }
    }
    //save data into database
    private fun saveUserData(){
        email = binding.edtmail.text.toString().trim()
        password=binding.edtpass.text.toString().trim()
        val user= UserModel(email,password)
        val userId=FirebaseAuth.getInstance().currentUser!!.uid
        //save user date
        database.child("user").child(userId).setValue(user)
    }
    private fun updateUI(user: FirebaseUser?){
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }
}

