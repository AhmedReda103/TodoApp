package com.example.todoapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.todoapp.data.models.ToDoData

@Dao
interface ToDoDao {

    @Query("SELECT * FROM todo_table order by id ASC"  )
    fun getAllData():LiveData<List<ToDoData>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(toDoData: ToDoData)

    @Delete
    suspend fun deleteToDo(toDoData: ToDoData)

    @Update
    suspend fun updateTodo(toDoData: ToDoData)

    @Query("delete from todo_table")
    suspend fun deleteAll()

    @Query("Select *from todo_table where title LIKE :searchQuery")
    fun searchDatabase(searchQuery:String):LiveData<List<ToDoData>>

    @Query("SELECT *FROM todo_table ORDER BY CASE WHEN priority Like 'H%' Then 1 when priority like 'M%' then 2 when priority like 'L%' then 3 end")
    fun sortByHighPriority():LiveData<List<ToDoData>>

    @Query("SELECT *FROM todo_table ORDER BY CASE WHEN priority Like 'L%' Then 1 when priority like 'M%' then 2 when priority like 'H%' then 3 end")
    fun sortByLowPriority():LiveData<List<ToDoData>>

}