package com.example.dacs3.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dacs3.R
import com.example.dacs3.adaptar.MenuAdapter
import com.example.dacs3.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: MenuAdapter
    val originalMenuDrinkName = listOf("kk","ll","mg")
    val originalMenuItemPrice = listOf("24.000","33.000","44.000")
    val originalMenuImage = listOf(R.drawable.banner1,R.drawable.banner2,R.drawable.banner3)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    private val filterMenuDrinkName = mutableListOf<String>()
    private val filterMenuItemPrice = mutableListOf<String>()
    private val filterMenuImage = mutableListOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentSearchBinding.inflate(inflater,container,false)
       // adapter = MenuAdapter(
         //  filterMenuDrinkName, filterMenuItemPrice,filterMenuImage,requireContext())
        binding.menuRecycleView.layoutManager= LinearLayoutManager(requireContext())
        binding.menuRecycleView.adapter=adapter

        setupSearchView()
        showAllMenu()
        return binding.root
    }

    private fun showAllMenu() {
        filterMenuDrinkName.clear()
        filterMenuItemPrice.clear()
        filterMenuImage.clear()

        filterMenuDrinkName.addAll(originalMenuDrinkName)
        filterMenuItemPrice.addAll(originalMenuItemPrice)
        filterMenuImage.addAll(originalMenuImage)
        adapter.notifyDataSetChanged()
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(/* listener = */ object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                filterMenuItems(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterMenuItems(newText)
                return true
            }
        })
    }
    private fun filterMenuItems(query:String?){
        filterMenuDrinkName.clear()
        filterMenuItemPrice.clear()
        filterMenuImage.clear()
        originalMenuDrinkName.forEachIndexed { index, foodName ->
            if (foodName.contains(query.toString(),ignoreCase = true)){
                filterMenuDrinkName.add(foodName)
                filterMenuItemPrice.add(originalMenuItemPrice[index])
                filterMenuImage.add(originalMenuImage[index])
            }
        }
        adapter.notifyDataSetChanged()
    }

    companion object {


    }
}