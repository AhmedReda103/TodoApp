package com.example.todoapp.fragments.update

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todoapp.R
import com.example.todoapp.data.models.ToDoData
import com.example.todoapp.data.viewmodels.ToDoViewModel
import com.example.todoapp.databinding.FragmentUpdateBinding
import com.example.todoapp.fragments.SharedViewModel


class UpdateFragment : Fragment(), MenuProvider {

    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!
    private val mSharedViewModel: SharedViewModel by viewModels()
    private val mToDoViewModel: ToDoViewModel by viewModels()

    private val args  by navArgs<UpdateFragmentArgs>()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        // Inflate the layout for this fragment
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
         binding.args = args
        //setHasOptionsMenu(true)
        activity?.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)


//        binding.customDescriptionEt.setText(args.currentItem.description)
//        binding.customTitleEt.setText(args.currentItem.title)
//        binding.customSpinner.setSelection(mSharedViewModel.parsePriorityToInt(args.currentItem.priority))

        binding.customSpinner.onItemSelectedListener = mSharedViewModel.listener

        return binding.root
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.update_fragment_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == R.id.menu_save) {
            updateItem()
            return true
        } else if (menuItem.itemId == R.id.delete) {
            confirmItemRemoval()
            return true
        }
        return false
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.update_fragment_menu, menu)
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == R.id.menu_save) {
//            updateItem()
//        } else if (item.itemId == R.id.delete) {
//            confirmItemRemoval()
//        }
//        return super.onOptionsItemSelected(item)
//    }

    private fun confirmItemRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            mToDoViewModel.deleteToDo(args.currentItem)
            Toast.makeText(
                requireContext(),
                "Successfully Removed : ${args.currentItem.title}",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("No"){_,_ ->}
        builder.setTitle("Delete '${args.currentItem.title}'")
        builder.setMessage("Are you sure you want to remove '${args.currentItem.title}'")
        builder.create().show()
    }

    private fun updateItem() {
        val title = binding.customTitleEt.text.toString()
        val description = binding.customDescriptionEt.text.toString()
        val getPriority = binding.customSpinner.selectedItem.toString()

        val validation = mSharedViewModel.verifyDataFromUser(title, description)

        if (validation) {
            val updatedItem = ToDoData(
                args.currentItem.id,
                title,
                mSharedViewModel.parsePriority(getPriority),
                description
            )
            mToDoViewModel.updateTodo(updatedItem)
            Toast.makeText(requireContext(), "Updated Successfully ", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(), "Please fill out all fields", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}