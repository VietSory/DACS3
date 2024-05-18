package com.example.dacs3

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.example.dacs3.databinding.FragmentCartBinding
import com.example.dacs3.databinding.PayOutActivityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class payOutActivity : AppCompatActivity(){
        private lateinit var binding: PayOutActivityBinding
        private lateinit var auth: FirebaseAuth
    private lateinit var name:String
    private lateinit var address:String
    private lateinit var phone:String
    private lateinit var totalAmount:String
    private lateinit var DrinkItemName:ArrayList<String>
    private lateinit var DrinkItemPrice:ArrayList<String>
    private lateinit var DrinkItemImage:ArrayList<String>
    private lateinit var DrinkItemDescription:ArrayList<String>
    private lateinit var DrinkItemQuantities:ArrayList<Int>
    private lateinit var database:DatabaseReference
    private lateinit var userId:String


    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = PayOutActivityBinding.inflate(layoutInflater)
            setContentView(binding.root)

            auth=FirebaseAuth.getInstance()
            database=FirebaseDatabase.getInstance().getReference()
            setUserData()
            //get user details from firebase
        val intent=intent
        DrinkItemName=intent.getStringArrayListExtra("DrinkItemName") as ArrayList<String>
        DrinkItemPrice=intent.getStringArrayListExtra("DrinkItemPrice") as ArrayList<String>
        DrinkItemImage=intent.getStringArrayListExtra("DrinkItemImage") as ArrayList<String>
        DrinkItemDescription=intent.getStringArrayListExtra("DrinkItemDescription") as ArrayList<String>
        DrinkItemQuantities=intent.getIntegerArrayListExtra("DrinkItemQuantities") as ArrayList<Int>
        totalAmount=calculateAmount().toString()+".000"
        Log.d("ggg",DrinkItemName.toString())
        //binding.edtTotal.isEnabled=false
        binding.edtTotal.setText(totalAmount)
        binding.button3.setOnClickListener{
                finish()
            }
            binding.btnOrder.setOnClickListener{
                    val bottomSheetDialog = CongratBottomSheet()
                    bottomSheetDialog.show(supportFragmentManager,"test")
            }
        }

    private fun calculateAmount(): Int {
        var totalAmount=0
        for (i in 0 until DrinkItemPrice.size){
            var price =DrinkItemPrice[i]
            val priceIntValue= price.toInt()
            var quantity=DrinkItemQuantities[i]
            totalAmount += priceIntValue*quantity
        }
        return totalAmount
    }

    private fun setUserData() {
        val user=auth.currentUser
        if(user!=null){
            userId=user.uid
            val userReference=database.child("user").child(userId)
            userReference.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        name=snapshot.child("name").getValue(String::class.java)?:""
                        address=snapshot.child("address").getValue(String::class.java)?:""
                        phone=snapshot.child("phone").getValue(String::class.java)?:""
                        binding.apply {
                            edtName.setText(name)
                            edtAddress.setText(address)
                            edtPhone.setText(phone)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
        }
    }
}