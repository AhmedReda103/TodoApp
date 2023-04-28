package com.example.todoapp.data.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.ToDoDatabase
import com.example.todoapp.data.models.ToDoData
import com.example.todoapp.data.repository.ToDoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ToDoViewModel(application: Application) : AndroidViewModel(application) {

    private val todoDao = ToDoDatabase.getDatabase(application).toDoDao()

    private val repository : ToDoRepository

    val getAllData : LiveData<List<ToDoData>>
    val searchByHighPriority : LiveData<List<ToDoData>>
    val searchByLowPriority : LiveData<List<ToDoData>>

    init {
        repository = ToDoRepository(todoDao)
        getAllData = repository.getAllData
        searchByHighPriority = repository.searchByHighPriority
        searchByLowPriority  = repository.searchByLowPriority
    }


     fun insertData(toDoData: ToDoData){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertData(toDoData)
        }
    }

    fun updateTodo(toDoData: ToDoData){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateData(toDoData)
        }
    }

    fun deleteToDo(toDoData: ToDoData){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteToDo(toDoData)
        }
    }


    fun deleteAll(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }

    fun searchDatabase(searchQuery: String): LiveData<List<ToDoData>>{
        return repository.searchDatabase(searchQuery)
    }

}