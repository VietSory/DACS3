package com.example.dacs3.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.dacs3.R
import com.example.dacs3.databinding.FragmentProfileBinding
import com.example.dacs3.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileFragment : Fragment() {
    private lateinit var binding:FragmentProfileBinding
    private val auth= FirebaseAuth.getInstance()
    private val database=FirebaseDatabase.getInstance()
    val userId=auth.currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentProfileBinding.inflate(inflater,container,false)
        setUserData()
        binding.btnSave.setOnClickListener {
            val name = binding.edtName.text.toString()
            val email = binding.edtEmail.text.toString()
            val address=binding.edtAddress.text.toString()
            val phone=binding.edtPhone.text.toString()
            updateUser(name,email,address,phone)
        }
        return binding.root
    }

    private fun updateUser(name: String, email: String, address: String, phone: String) {
        if(userId!=null){
            val userReference=database.getReference("user").child(userId)
            val userData= hashMapOf(
                "name" to name,
                "address" to address,
                "email" to email,
                "phone" to phone
            )
            userReference.setValue(userData).addOnSuccessListener {
                Toast.makeText(requireContext(),"Hồ sơ đã được cập nhật thành công",Toast.LENGTH_SHORT).show()
            }
                .addOnFailureListener {
                    Toast.makeText(requireContext(),"Hồ sơ cập nhật thất bại",Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun setUserData() {
        if(userId!=null){
            val userReference=database.getReference("user").child(userId)
            userReference.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        val userProfile=snapshot.getValue(UserModel::class.java)
                        if (userProfile!=null){
                            binding.edtName.setText(userProfile.name)
                            binding.edtAddress.setText(userProfile.address)
                            binding.edtEmail.setText(userProfile.email)
                            binding.edtPhone.setText(userProfile.phone)

                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
        }
    }

    companion object {

    }
}