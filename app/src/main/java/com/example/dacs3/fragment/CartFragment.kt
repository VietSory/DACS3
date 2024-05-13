package com.example.dacs3.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dacs3.CongratBottomSheet
import com.example.dacs3.R
import com.example.dacs3.adaptar.CartAdapter
import com.example.dacs3.databinding.CartItemBinding
import com.example.dacs3.databinding.FragmentCartBinding
import com.example.dacs3.model.cartItems
import com.example.dacs3.payOutActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue


class CartFragment : Fragment() {
    private lateinit var binding:FragmentCartBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var drinkNames:MutableList<String>
    private lateinit var drinkPrices:MutableList<String>
    private lateinit var drinkDescriptions:MutableList<String>
    private lateinit var drinkImagesUri:MutableList<String>
    private lateinit var quantity:MutableList<Int>
    private lateinit var cartAdapter: CartAdapter
    private lateinit var userId:String




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentCartBinding.inflate(inflater,container,false)
        auth=FirebaseAuth.getInstance()
        retrieveCartItems()

        binding.btnCart.setOnClickListener{
            getOrderItemsDetail()
            val intent= Intent(requireContext(),payOutActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }

    private fun getOrderItemsDetail() {
        val orderIdReference:DatabaseReference=database.reference.child("user").child(userId).child("CartItems")
        var drinkName= mutableListOf<String>()
        var drinkPrice= mutableListOf<String>()
        var drinkDescription= mutableListOf<String>()
        var drinkImage= mutableListOf<String>()
        val drinkQuantity=cartAdapter.getUpdateItemsQuantities()
        orderIdReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (drinkSnapshot in snapshot.children){
                    val orderItems=drinkSnapshot.getValue(cartItems::class.java)
                    orderItems?.drinkName?.let { drinkNames.add(it) }
                    orderItems?.drinkPrice?.let { drinkPrices.add(it) }
                    orderItems?.drinkDescription?.let { drinkDescriptions.add(it) }
                    orderItems?.drinkImage?.let { drinkImagesUri.add(it) }
                    orderNow(drinkName,drinkPrice,drinkDescription,drinkImage,drinkQuantity)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(),"Đặt hàng thất bại.Hãy thử lại lần nữa",Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun orderNow(drinkName: MutableList<String>, drinkPrice: MutableList<String>, drinkDescription: MutableList<String>, drinkImage: MutableList<String>, drinkQuantity: MutableList<Int>) {
        if (isAdded && context !=null){
            val intent=Intent(requireContext(),payOutActivity::class.java)
            intent.putExtra("DrinkItemName",drinkName as ArrayList<String>)
            intent.putExtra("DrinkItemPrice",drinkPrice as ArrayList<String>)
            intent.putExtra("DrinkItemImage",drinkImage as ArrayList<String>)
            intent.putExtra("DrinkItemDescription",drinkDescription as ArrayList<String>)
            intent.putExtra("DrinkItemQuantities",drinkQuantity as ArrayList<Int>)
            startActivity(intent)
        }
    }

    private fun retrieveCartItems() {
        database = FirebaseDatabase.getInstance()
        userId = auth.currentUser?.uid?:""
        val drinkReference =database.reference.child("user").child(userId).child("CartItems")
        //list to store cart Items
        drinkNames= mutableListOf()
        drinkPrices= mutableListOf()
        drinkDescriptions= mutableListOf()
        drinkImagesUri= mutableListOf()
        quantity= mutableListOf()

        //fetch data from database
        drinkReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (drinkSnapshot in snapshot.children){
                    //get the cartItems object from child node
                    val cartItems=drinkSnapshot.getValue(cartItems::class.java)
                    //add cartItems detail to the list
                    cartItems?.drinkName?.let { drinkNames.add(it) }
                    cartItems?.drinkPrice?.let { drinkPrices.add(it) }
                    cartItems?.drinkDescription?.let { drinkDescriptions.add(it) }
                    cartItems?.drinkImage?.let { drinkImagesUri.add(it) }
                    cartItems?.drinkQuantity?.let { quantity.add(it) }

                }
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    private fun setAdapter() {
        cartAdapter=CartAdapter(drinkNames,drinkPrices,drinkImagesUri,drinkDescriptions,quantity,requireContext())
        binding.cartRecycleView.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.cartRecycleView.adapter=cartAdapter
    }

    companion object {

    }
}