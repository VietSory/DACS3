package com.example.dacs3

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dacs3.R
import com.example.dacs3.adaptar.CartAdapter
import com.example.dacs3.adaptar.MenuAdapter
import com.example.dacs3.databinding.FragmentMenuBotomBinding
import com.example.dacs3.model.menuItem
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MenuBotomFragment : BottomSheetDialogFragment(){
    private lateinit var binding:FragmentMenuBotomBinding
    private lateinit var database : FirebaseDatabase
    private lateinit var menuItems: MutableList<menuItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentMenuBotomBinding.inflate(inflater,container,false)
        binding.btnBack.setOnClickListener{
            dismiss()
        }
        retrieveMenuItems()
        return binding.root
    }

    private fun retrieveMenuItems() {
        database=FirebaseDatabase.getInstance()
        val drinkRef: DatabaseReference =database.reference.child("menu")
        menuItems = mutableListOf()
        //fetch data
        drinkRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot){

                //loop each item
                for (drinkSnapshot in snapshot.children){
                    val menuItem=drinkSnapshot.getValue(menuItem::class.java)
                    menuItem?.let {
                        menuItems.add(it)
                    }
                }
                setAdapter()
            }


            override fun onCancelled(error: DatabaseError) {
                Log.d("DatabaseError","Error: ${error.message}")
            }
        })
    }
    private fun setAdapter() {
        val adapter = MenuAdapter(menuItems,requireContext())
        binding.menuRecycleView.layoutManager = LinearLayoutManager(requireContext())
        binding.menuRecycleView.adapter=adapter
    }

    companion object {

    }
}