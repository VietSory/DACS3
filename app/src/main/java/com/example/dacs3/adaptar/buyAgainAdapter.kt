package com.example.dacs3.adaptar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dacs3.databinding.BuyAgainItemBinding

class buyAgainAdapter(private val buyAgainDrinkName:ArrayList<String>,
                      private val buyAgainDrinkPrice:ArrayList<String>,
                      private val buyAgainDrinkImage:ArrayList<Int>):RecyclerView.Adapter<buyAgainAdapter.buyAgainViewHolder>() {

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
    class buyAgainViewHolder(private val binding: BuyAgainItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(drinkName: String, drinkPrice: String, drinkImage: Int) {
            binding.apply {
                buyAgainName.text=drinkName
                buyAgainPrice.text=drinkPrice
                buyAgainImage.setImageResource(drinkImage)
            }
        }

    }
}