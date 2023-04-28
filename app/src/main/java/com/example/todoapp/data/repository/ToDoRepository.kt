package com.example.todoapp.data.repository

import androidx.lifecycle.LiveData
import com.example.todoapp.data.ToDoDao
import com.example.todoapp.data.models.ToDoData

class ToDoRepository(private val toDoDao: ToDoDao) {

    val getAllData = toDoDao.getAllData()
    val searchByHighPriority = toDoDao.sortByHighPriority()
    val searchByLowPriority = toDoDao.sortByLowPriority()


    suspend fun insertData(toDoData: ToDoData){
        toDoDao.insertData(toDoData)
    }

    suspend fun updateData(toDoData: ToDoData){
        toDoDao.updateTodo(toDoData)
    }

    suspend fun deleteToDo(toDoData: ToDoData){
        toDoDao.deleteToDo(toDoData)
    }

    suspend fun deleteAll(){
        toDoDao.deleteAll()
    }

    fun searchDatabase(query : String):LiveData<List<ToDoData>>{
        return toDoDao.searchDatabase(query)
    }

}