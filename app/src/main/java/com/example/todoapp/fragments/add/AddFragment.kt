package com.example.todoapp.fragments.add

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.data.viewmodels.ToDoViewModel
import com.example.todoapp.data.models.ToDoData
import com.example.todoapp.fragments.SharedViewModel
import com.example.todoapp.databinding.FragmentAddBinding


class AddFragment : Fragment() , MenuProvider {

    private var _binding : FragmentAddBinding? =null
    private val binding get() = _binding!!

    private val mTodoViewModel : ToDoViewModel by viewModels()
    private val mSharedViewModel : SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddBinding.inflate(inflater, container, false)

        activity?.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)


        binding.spinner.onItemSelectedListener = mSharedViewModel.listener

        return binding.root
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.add_fragment_menu , menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if(menuItem.itemId == R.id.menu_add){
            insertDataToDb()
            return true

        }
        return false
    }

    private fun insertDataToDb() {
        val mTitle = binding.titleEt.text.toString()
        val mPriority = binding.spinner.selectedItem.toString()
        val mDescription = binding.descriptionEt.text.toString()

        val validation = mSharedViewModel.verifyDataFromUser(mTitle , mDescription)
        if(validation){
            val todo = ToDoData(0 , mTitle ,mSharedViewModel.parsePriority(mPriority), mDescription)
            mTodoViewModel.insertData(todo)
            Toast.makeText(requireContext() , "Successfully Added" , Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        }else{
            Toast.makeText(requireContext() , "Please fill out all fields " , Toast.LENGTH_SHORT).show()

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




}