package com.example.dacs3

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.dacs3.databinding.ActivityDetailsBinding
import com.example.dacs3.model.cartItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private var drinkName :String?=null
    private var drinkPrice :String?=null
    private var drinkDescription :String?=null
    private var drinkImage :String?=null
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
//initialize firebase
        auth=FirebaseAuth.getInstance()
        drinkName=intent.getStringExtra("menuName")
        drinkPrice=intent.getStringExtra("menuPrice")
        drinkDescription=intent.getStringExtra("menuDescription")
        drinkImage=intent.getStringExtra("menuImage")

        with(binding){
            tvDrinkName.text=drinkName
            tvDescription.text=drinkDescription
            Glide.with(this@DetailsActivity).load(Uri.parse(drinkImage)).into(imgDrink)
        }

        binding.imgButton.setOnClickListener{
            finish()
        }
        binding.btnAddcart.setOnClickListener{
            addItemToCart()
        }
    }

    private fun addItemToCart() {
        val database = FirebaseDatabase.getInstance().reference
        val userId=auth.currentUser?.uid?:""
        //create cartItems
        val cartItems=cartItems(drinkName.toString(),drinkPrice.toString(),drinkDescription.toString(),drinkImage.toString(),1 )
        //save cartItems to firebase
        database.child("user").child(userId).child("CartItems").push().setValue(cartItems).addOnSuccessListener {
            Toast.makeText(this,"Thêm vào giỏ hàng thành công",Toast.LENGTH_SHORT).show()
        }.addOnFailureListener(){
            Toast.makeText(this,"Thêm vào giỏ hàng Thất bại",Toast.LENGTH_SHORT).show()

        }
    }
}