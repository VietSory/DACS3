package com.example.dacs3.adaptar

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dacs3.databinding.BuyAgainItemBinding

class buyAgainAdapter(private val buyAgainDrinkName:MutableList<String>,
                      private val buyAgainDrinkPrice:MutableList<String>,
                      private val buyAgainDrinkImage:MutableList<String>,
                      private var requireContext: Context):RecyclerView.Adapter<buyAgainAdapter.buyAgainViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): buyAgainViewHolder {
        val binding = BuyAgainItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return buyAgainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: buyAgainViewHolder, position: Int) {
        holder.bind(buyAgainDrinkName[position],buyAgainDrinkPrice[position],buyAgainDrinkImage[position])
    }

    override fun getItemCount(): Int = buyAgainDrinkName.size
    inner class buyAgainViewHolder(private val binding: BuyAgainItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(drinkName: String, drinkPrice: String, drinkImage: String) {
            binding.apply {
                buyAgainName.text=drinkName
                buyAgainPrice.text=drinkPrice
                val uriString=drinkImage
                val uri=Uri.parse(uriString)
                Glide.with(requireContext).load(uri).into(buyAgainImage)
            }
        }

    }
}