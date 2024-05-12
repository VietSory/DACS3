package com.example.dacs3.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dacs3.R
import com.example.dacs3.adaptar.buyAgainAdapter
import com.example.dacs3.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var buyAgainAdapter: buyAgainAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(layoutInflater,container,false)
        setupRecycleView()
        return binding.root
    }
    private fun setupRecycleView(){
        val buyAgainDrinkName = arrayListOf("kk","ll","mg")
        val buyAgainDrinkPrice = arrayListOf("24.000","33.000","44.000")
        val buyAgainDrinkImage = arrayListOf(R.drawable.banner1,R.drawable.banner2,R.drawable.banner3)
        buyAgainAdapter = buyAgainAdapter(buyAgainDrinkName,buyAgainDrinkPrice,buyAgainDrinkImage)
        binding.BuyAgainRecycleView.adapter=buyAgainAdapter
        binding.BuyAgainRecycleView.layoutManager=LinearLayoutManager(requireContext())

    }
    companion object {

    }
}