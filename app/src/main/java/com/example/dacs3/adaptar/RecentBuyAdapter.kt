package com.example.dacs3.adaptar

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dacs3.databinding.RecentBuyItemBinding

class RecentBuyAdapter(private var context: Context,
                       private var drinkNameList:ArrayList<String>,
                       private var drinkImageList:ArrayList<String>,
                       private var drinkPriceList: ArrayList<String>,
                       private var drinkQuantityList:ArrayList<Int>,
    ) :RecyclerView.Adapter<RecentBuyAdapter.RecentViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecentBuyAdapter.RecentViewHolder {
        val binding=RecentBuyItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RecentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecentBuyAdapter.RecentViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int =drinkNameList.size
    inner class RecentViewHolder(private val binding: RecentBuyItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                drinkNamePP.text=drinkNameList[position]
                pricePP.text=drinkPriceList[position]
                tvQuantity.text=drinkQuantityList[position].toString()
                val uriString=drinkImageList[position]
                val uri=Uri.parse(uriString)
                Glide.with(context).load(uri).into(imageView3)
            }
        }

    }
}