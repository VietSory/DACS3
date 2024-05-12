package com.example.dacs3.adaptar

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dacs3.DetailsActivity
import com.example.dacs3.databinding.MenuItemBinding
import com.example.dacs3.model.menuItem

class MenuAdapter (private val menuItems:List<menuItem>,private val requireContext: Context) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = MenuItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return  MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(position)
    }
    override fun getItemCount(): Int = menuItems.size

    inner class MenuViewHolder(private val binding: MenuItemBinding) :RecyclerView.ViewHolder(binding.root) {
        init{
            binding.root.setOnClickListener{
                val position = adapterPosition
                if (position!= RecyclerView.NO_POSITION){
                      openDetailsActivity(position)
                }
            }
        }
        //set data in to recycleview
        fun bind(position: Int) {
            val menuItem=menuItems[position]
            binding.apply {
                menuDrinkName.text =menuItem.drinkName
                menuPrice.text = menuItem.drinkPrice
                val uri = Uri.parse(menuItem.drinkImage)
                Glide.with(requireContext).load(uri).into(menuImage)
            }
        }
    }

    private fun openDetailsActivity(position: Int) {
        val menuItem = menuItems[position]
        val intent = Intent(requireContext,DetailsActivity::class.java).apply {
            putExtra("menuName",menuItem.drinkName)
            putExtra("menuPrice",menuItem.drinkPrice)
            putExtra("menuDescription",menuItem.drinkDescription)
            putExtra("menuImage",menuItem.drinkImage)
        }
        requireContext.startActivity(intent)
    }

}
