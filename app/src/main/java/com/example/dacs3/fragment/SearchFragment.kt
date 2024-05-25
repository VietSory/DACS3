package com.example.dacs3.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dacs3.adaptar.MenuAdapter
import com.example.dacs3.databinding.FragmentSearchBinding
import com.example.dacs3.model.menuItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: MenuAdapter
    private lateinit var database:FirebaseDatabase
    private var orginalMenuItems= mutableListOf<menuItem>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentSearchBinding.inflate(inflater,container,false)
        retrieveMenuItem()
        setupSearchView()
        return binding.root
    }

    private fun retrieveMenuItem() {
        database=FirebaseDatabase.getInstance()
        val drinkReference:DatabaseReference=database.reference.child("menu")
        drinkReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (drinkSnapshot in snapshot.children){
                    val menuItem=drinkSnapshot.getValue(menuItem::class.java)
                    menuItem?.let {
                        orginalMenuItems.add(it)
                    }
                }
                showAllMenu()
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    private fun showAllMenu() {
        val filterMenuItem= ArrayList(orginalMenuItems)
        setAdapter(filterMenuItem)
    }

    private fun setAdapter(filterMenuItem: List<menuItem>) {
        adapter= MenuAdapter(filterMenuItem,requireContext())
        binding.menuRecycleView.layoutManager=LinearLayoutManager(requireContext())
         binding.menuRecycleView.adapter=adapter
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(/* listener = */ object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                filterMenuItems(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterMenuItems(newText)
                return true
            }
        })
    }
    private fun filterMenuItems(query:String){
        val filterMenuItems=orginalMenuItems.filter {
            it.drinkName?.contains(query,ignoreCase = true)==true
        }
        setAdapter(filterMenuItems)
    }
}