package com.example.dacs3

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dacs3.adaptar.RecentBuyAdapter
import com.example.dacs3.databinding.ActivityRecentOrderItemsBinding
import com.example.dacs3.model.OrderDetails

class recentOrderItems : AppCompatActivity() {
    private val binding:ActivityRecentOrderItemsBinding by lazy{
        ActivityRecentOrderItemsBinding.inflate(layoutInflater)
    }
    private lateinit var drinkNames:ArrayList<String>
    private lateinit var drinkImages:ArrayList<String>
    private lateinit var drinkPrices:ArrayList<String>
    private lateinit var drinkQuantities:ArrayList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnBack.setOnClickListener {
            finish()
        }
        setContentView(binding.root)
        val recentOrderItems=intent.getSerializableExtra("RecentBuyOrderItem") as ArrayList<OrderDetails>
        recentOrderItems ?.let { orderDetails ->
            for (i in orderDetails.indices) {
                val recentOrderItem = orderDetails[i]
                if (orderDetails.isNotEmpty()) {
                    drinkNames = recentOrderItem.drinkNames as ArrayList<String>
                    drinkImages = recentOrderItem.drinkImages as ArrayList<String>
                    drinkPrices = recentOrderItem.drinkPrices as ArrayList<String>
                    drinkQuantities = recentOrderItem.drinkQuantities as ArrayList<Int>
                }
            }
        }
        setAdapter()
    }

    private fun setAdapter() {
        val recycle=binding.menuRecycleView
        recycle.layoutManager=LinearLayoutManager(this)
        val adapter=RecentBuyAdapter(this,drinkNames,drinkImages,drinkPrices,drinkQuantities)
        recycle.adapter=adapter
    }
}