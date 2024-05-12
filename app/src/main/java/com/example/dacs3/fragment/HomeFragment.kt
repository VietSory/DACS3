package com.example.dacs3.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.dacs3.MenuBotomFragment
import com.example.dacs3.R
import com.example.dacs3.adaptar.MenuAdapter
import com.example.dacs3.adaptar.PopularAdapter
import com.example.dacs3.databinding.FragmentHomeBinding
import com.example.dacs3.model.menuItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding:FragmentHomeBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var menuItems:MutableList<menuItem>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        binding.ViewAllMenu.setOnClickListener{
            val bottomSheetDialog = MenuBotomFragment()
            bottomSheetDialog.show(parentFragmentManager,"test")
        }
        retrieveAndDisplay()
        return binding.root
    }

    private fun retrieveAndDisplay() {
        //get reference to database
        database = FirebaseDatabase.getInstance()
        val drinkRef:DatabaseReference = database.reference.child("menu")
        menuItems= mutableListOf()
        //retrieve menu items from database
        drinkRef.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (drinkSnapshot in snapshot.children){
                    val menuItem = drinkSnapshot.getValue(menuItem::class.java)
                    menuItem?.let { menuItems.add(it) }
                }
                //display a random popular items
                randomPopularItems()
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun randomPopularItems() {
        //create shuffled list of menu items
        val index =menuItems.indices.toList().shuffled()
        val numItemToShow=6
        val subsetMenuItems=index.take(numItemToShow).map { menuItems[it] }
        setPopularItemsAdapter(subsetMenuItems)
    }

    private fun setPopularItemsAdapter(subsetMenuItems: List<menuItem>) {
        val adapter= MenuAdapter(subsetMenuItems,requireContext())
        binding.PPRecycle.layoutManager=LinearLayoutManager(requireContext())
        binding.PPRecycle.adapter=adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.banner1, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner2, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner3, ScaleTypes.FIT))

        val imageSlider=binding.imageSlider
        imageSlider.setImageList(imageList)
        imageSlider.setImageList(imageList, ScaleTypes.FIT)

        imageSlider.setItemClickListener(object : ItemClickListener{
            override fun doubleClick(position: Int) {
            }
            override fun onItemSelected(position: Int) {
                val itemPostion= imageList[position]
                val itemMessage= "Bạn vừa chọn ảnh $position"
                Toast.makeText(requireContext(),itemMessage,Toast.LENGTH_SHORT).show()
            }
        })
    }

}