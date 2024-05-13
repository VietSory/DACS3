package com.example.dacs3.adaptar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.ContactsContract.Data
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.dacs3.databinding.CartItemBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CartAdapter(private val cartItems:MutableList<String>,private val cartPrices:MutableList<String>,private val cartImages:MutableList<String>,
    private val cartDescription:MutableList<String>,private val cartQuantity:MutableList<Int>,private val context:Context) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    private val auth=FirebaseAuth.getInstance()

    //initialize database
    init {
        val database=FirebaseDatabase.getInstance()
        val userId=auth.currentUser?.uid?:""
        val cartItemNumber  =cartItems.size
        itemQuantities=IntArray(cartItemNumber){1}
        cartItemReference = database.reference.child("user").child(userId).child("CartItems")
    }
    companion object{
        private var itemQuantities:IntArray = intArrayOf()
        private lateinit var cartItemReference:DatabaseReference
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding=CartItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(position)
    }
    override fun getItemCount(): Int =cartItems.size
    fun getUpdateItemsQuantities(): MutableList<Int> {
        val itemQuantity= mutableListOf<Int>()
        itemQuantity.addAll(cartQuantity)
        return itemQuantity
    }
    inner class CartViewHolder(private val binding: CartItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply{
                val quantity= itemQuantities[position]
                cartDrinkName.text=cartItems[position]
                cartPrice.text=cartPrices[position]
                cartItemQuantity.text=quantity.toString()
                //load image using glide
                val uriString =cartImages[position]
                val uri=Uri.parse(uriString)
                Glide.with(context).load(uri).into(cartImage)
                btnminus.setOnClickListener {
                    deceaseQuantity(position)
                }

                btnplus.setOnClickListener {
                    increaseQuantity(position)
                }
                btndelete.setOnClickListener {
                    val itemPosition = adapterPosition
                    if (itemPosition != RecyclerView.NO_POSITION) {
                        deleteItem(itemPosition)
                    }
                }
            }
        }

            private fun increaseQuantity(position: Int) {
                if (itemQuantities[position] < 10) {
                    itemQuantities[position]++
                    binding.cartItemQuantity.text = itemQuantities[position].toString()
                }
            }
            private fun deceaseQuantity(position: Int) {
                if (itemQuantities[position] > 1) {
                    itemQuantities[position]--
                    binding.cartItemQuantity.text = itemQuantities[position].toString()
                }
            }
            private fun deleteItem (position : Int){
                 val positionRetrieve = position
                 getUniqueKeyAtPosition(positionRetrieve){uniquekey->
                     if (uniquekey!=null){
                         removeItem(position,uniquekey)
                     }
                 }
            }
    }

    private fun removeItem(position: Int, uniquekey: String) {
        if (uniquekey!=null){
            cartItemReference.child(uniquekey).removeValue().addOnSuccessListener {
                cartItems.removeAt(position)
                cartPrices.removeAt(position)
                cartImages.removeAt(position)
                cartDescription.removeAt(position)
                cartQuantity.removeAt(position)

                //update itemQuantities
                itemQuantities = itemQuantities.filterIndexed { index, i -> index!=position  }.toIntArray()
                notifyItemRemoved(position)
                notifyItemRangeChanged(position,cartItems.size)

            }
        }
    }

    private fun getUniqueKeyAtPosition(positionRetrieve: Int, onComplete:(String?) ->Unit) {
        cartItemReference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var uniqueKey:String?=null
                //loop for snap children
                snapshot.children.forEachIndexed { index, dataSnapshot ->
                    if (index==positionRetrieve){
                        uniqueKey=dataSnapshot.key
                        return@forEachIndexed
                    }
                }
                onComplete(uniqueKey)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}