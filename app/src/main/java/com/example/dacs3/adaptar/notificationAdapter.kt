package com.example.dacs3.adaptar

import android.app.Notification
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dacs3.databinding.NotificationItemBinding

class notificationAdapter(private var notification: ArrayList<String>,private var notificationImage:ArrayList<Int>) :RecyclerView.Adapter<notificationAdapter.NotificationViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding=NotificationItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NotificationViewHolder(binding)
    }

    override fun getItemCount(): Int = notification.size

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(position)
    }
    inner class NotificationViewHolder(private val binding: NotificationItemBinding) :RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int) {
            binding.apply {
                tvNotification.text=notification[position]
                imgNotification.setImageResource(notificationImage[position])
            }
        }

    }
}