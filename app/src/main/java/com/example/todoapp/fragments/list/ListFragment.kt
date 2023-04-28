package com.example.todoapp.fragments.list

import android.os.Bundle
import android.view.*
import android.widget.SearchView

import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.todoapp.R
import com.example.todoapp.data.models.ToDoData
import com.example.todoapp.data.viewmodels.ToDoViewModel
import com.example.todoapp.databinding.FragmentListBinding
import com.example.todoapp.fragments.SharedViewModel
import com.example.todoapp.fragments.list.adapter.ListAdapter
import com.example.todoapp.fragments.list.adapter.SwipeToDelete
import com.example.todoapp.utils.hideKeyboard
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator


class ListFragment : Fragment(), SearchView.OnQueryTextListener , MenuProvider {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private val mTodoViewModel: ToDoViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()
    private val adapter: ListAdapter by lazy { ListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        binding.mSharedViewModel = mSharedViewModel

        setUpRecyclerView()

        mTodoViewModel.getAllData.observe(requireActivity()) {
            mSharedViewModel.checkIfDatabaseEmpty(it)
            adapter.setData(it)
        }

//        mSharedViewModel.emptyDatabase.observe(requireActivity()){
//            showEmptyDataBaseViews(it)
//        }

//        binding.floatingActionButton.setOnClickListener {
//            findNavController().navigate(R.id.addFragment)
//        }

//        binding.listLayout.setOnClickListener {
//            findNavController().navigate(R.id.updateFragment)
//        }

       // setHasOptionsMenu(true)

        activity?.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        hideKeyboard(requireActivity())

        return binding.root
    }

    private fun setUpRecyclerView() {
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.adapter = adapter
        recyclerView.itemAnimator = SlideInUpAnimator().apply {
            addDuration = 300

        }
        //Swipe to delete
        swipeToDelete(recyclerView)

    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallBack = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemToDelete = adapter.dataList[viewHolder.adapterPosition]
                mTodoViewModel.deleteToDo(itemToDelete)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)
                //restore deleted item
                restoreDeleteData(viewHolder.itemView, itemToDelete)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallBack)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    fun restoreDeleteData(view: View, deletedItem: ToDoData) {
        val snackBar = Snackbar.make(view, "Deleted ${deletedItem.title} ", Snackbar.LENGTH_SHORT)
        snackBar.setAction("Undo") {
            mTodoViewModel.insertData(deletedItem)
        }
        snackBar.show()

    }

//    private fun showEmptyDataBaseViews(emptyDatabase: Boolean) {
//        if(emptyDatabase){
//            binding.noDataTextView.visibility = View.VISIBLE
//        }else{
//            binding.noDataTextView.visibility = View.INVISIBLE
//        }
//    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.list_fragment_menu, menu)
        val search = menu.findItem(R.id.menu_search)
        /*
        An action view is an action that provides rich functionality within the app bar.
        For example, a search action view allows the user to type their search text in the app bar,
        without having to change activities or fragments.
         */
        //convert search menu item to an action view of SearchView
        val searchView = search.actionView as? SearchView
        //we can set listener to call method isSubmittedButtonEnabled
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.menu_delete_all-> confirmRemoval()
            R.id.menu_priority_high-> mTodoViewModel.searchByHighPriority.observe(this ){
                adapter.setData(it)

            }
            R.id.priority_low -> mTodoViewModel.searchByLowPriority.observe(this){
                adapter.setData(it)
            }
        }
        return true

    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.list_fragment_menu, menu)
//        val search = menu.findItem(R.id.menu_search)
//        /*
//        An action view is an action that provides rich functionality within the app bar.
//        For example, a search action view allows the user to type their search text in the app bar,
//        without having to change activities or fragments.
//         */
//        //convert search menu item to an action view of SearchView
//        val searchView = search.actionView as? SearchView
//        //we can set listener to call method isSubmittedButtonEnabled
//        searchView?.isSubmitButtonEnabled = true
//        searchView?.setOnQueryTextListener(this)
//
//    }

//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.menu_delete_all-> confirmRemoval()
//            R.id.menu_priority_high-> mTodoViewModel.searchByHighPriority.observe(this ){
//                adapter.setData(it)
//            }
//            R.id.priority_low -> mTodoViewModel.searchByLowPriority.observe(this){
//                adapter.setData(it)
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            searchThroughDatabase(newText)
        }
        return true
    }

    private fun searchThroughDatabase(query: String) {
        var searchQuery ="%$query%"
        mTodoViewModel.searchDatabase(searchQuery).observe(this) { list ->
            list?.let {
                adapter.setData(it)
            }

        }
    }


    private fun confirmRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            mTodoViewModel.deleteAll()
            Toast.makeText(
                requireContext(),
                "Successfully Removed EveryThing",
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete EveryThing ? ")
        builder.setMessage("Are you sure you want to remove everything ")
        builder.create().show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




}