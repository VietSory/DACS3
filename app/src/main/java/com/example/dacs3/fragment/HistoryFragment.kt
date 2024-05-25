package com.example.dacs3.fragment

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.dacs3.R
import com.example.dacs3.adaptar.buyAgainAdapter
import com.example.dacs3.databinding.FragmentHistoryBinding
import com.example.dacs3.model.OrderDetails
import com.example.dacs3.recentOrderItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var buyAgainAdapter: buyAgainAdapter
    private lateinit var database:FirebaseDatabase
    private lateinit var auth:FirebaseAuth
    private lateinit var userId:String
    private var listOrderItem:ArrayList<OrderDetails> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(layoutInflater,container,false)
        auth=FirebaseAuth.getInstance()
        database=FirebaseDatabase.getInstance()
        //retrieve and display the user order history
        retrieveBuyHistory()
        binding.recentBuyItem.setOnClickListener {
            setItemRecentBuy()
        }
        binding.btnReceive.setOnClickListener {
            updateOrderStatus()
        }
        return binding.root
    }

    private fun updateOrderStatus() {
        val itemPushKey=listOrderItem[0].itemPushKey
        val completeOrderReference=database.reference.child("CompletedOrder").child(itemPushKey!!)
        completeOrderReference.child("paymentReceived").setValue(true)
    }

    private fun setItemRecentBuy() {
        listOrderItem.firstOrNull()?.let { recentBuy->
            val intent= Intent(requireContext(),recentOrderItems::class.java)
            intent.putExtra("RecentBuyOrderItem",listOrderItem)
            startActivity(intent)
        }

    }

    private fun retrieveBuyHistory() {
        binding.recentBuyItem.visibility=View.INVISIBLE
        userId=auth.currentUser?.uid?:""
        val buyItemReference=database.reference.child("user").child(userId).child("BuyHistory")
        val shortingQuery=buyItemReference.orderByChild("currentTime")
        shortingQuery.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (buySnapshot in snapshot.children){
                    val buyHistoryItem=buySnapshot.getValue(OrderDetails::class.java)
                    buyHistoryItem?.let { listOrderItem.add(it) }
                }
                listOrderItem.reverse()
                if (listOrderItem.isNotEmpty()){
                    //display the most recent order details
                    setDataRecentBuyItem()
                    //setup recycle with order details
                    setPreviousBuyItem()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    private fun setDataRecentBuyItem() {
        binding.recentBuyItem.visibility=View.VISIBLE
        val recentOrderItem=listOrderItem.firstOrNull()
        recentOrderItem?.let {
            with(binding){
                tvName.text=it.drinkNames?.firstOrNull()?:""
                tvPrice.text=it.drinkPrices?.firstOrNull()?:""
                val image=it.drinkImages?.firstOrNull()?:""
                val uri=Uri.parse(image)
                Glide.with(requireContext()).load(uri).into(BuyAgainImage)

                val isOrderAccepted=listOrderItem[0].orderAccept
                if (isOrderAccepted){
                    orderStatus.background.setTint(Color.GREEN)
                    btnReceive.visibility=View.VISIBLE
                }
            }
        }
    }

    private fun setPreviousBuyItem() {
        val BuyAgainDrinkName= mutableListOf<String>()
        val BuyAgainDrinkPrice= mutableListOf<String>()
        val BuyAgainDrinkImage= mutableListOf<String>()
        for (i in 1 until listOrderItem.size){
            listOrderItem[i].drinkNames?.firstOrNull()?.let {
                BuyAgainDrinkName.add(it)
                listOrderItem[i].drinkPrices?.firstOrNull()?.let {
                    BuyAgainDrinkPrice.add(it)
                    listOrderItem[i].drinkImages?.firstOrNull()?.let {
                        BuyAgainDrinkImage.add(it)
                    }
                }
                val rv = binding.BuyAgainRecycleView
                rv.layoutManager = LinearLayoutManager(requireContext())
                buyAgainAdapter = buyAgainAdapter(
                    BuyAgainDrinkName,
                    BuyAgainDrinkPrice,
                    BuyAgainDrinkImage,
                    requireContext()
                )
                rv.adapter = buyAgainAdapter
            }
        }
    }
}