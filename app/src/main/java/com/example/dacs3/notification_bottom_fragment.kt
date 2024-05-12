package com.example.dacs3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dacs3.adaptar.notificationAdapter
import com.example.dacs3.databinding.FragmentNotificationBottomFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class notification_bottom_fragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentNotificationBottomFragmentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotificationBottomFragmentBinding.inflate(layoutInflater,container,false)
        val notifications = listOf("Bạn đã hủy đơn hàng thành công","Đơn hàng của bạn đang được vận chuyển","Chúc mừng bạn đã đặt hàng thành công")
        val nofiticationImages = listOf(R.drawable.sademoji,R.drawable.truck,R.drawable.congratulation)
        val adapter = notificationAdapter(
            ArrayList(notifications),
            ArrayList(nofiticationImages)
        )
        binding.notifRecycleView.layoutManager= LinearLayoutManager(requireContext())
        binding.notifRecycleView.adapter=adapter
        return binding.root
    }

    companion object {

    }
}