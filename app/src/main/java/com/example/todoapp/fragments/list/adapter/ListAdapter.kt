package com.example.todoapp.fragments.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.data.models.ToDoData
import com.example.todoapp.databinding.RowLayoutBinding

class ListAdapter : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

     var dataList = emptyList<ToDoData>()

    class MyViewHolder( val binding: RowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ToDoData) {

            binding.todoData = data
            binding.executePendingBindings()

//            binding.titleTxt.text = data.title
//            binding.descriptionTxt.text = data.description
//            when (data.priority) {
//                Priority.HIGH -> binding.priorityIndicator.setBackgroundColor(
//                    ContextCompat.getColor(
//                        binding.root.context,
//                        R.color.red
//                    )
//                )
//                Priority.MEDIUM -> binding.priorityIndicator.setBackgroundColor(
//                    ContextCompat.getColor(
//                        binding.root.context,
//                        R.color.yellow
//                    )
//                )
//                Priority.LOW -> binding.priorityIndicator.setBackgroundColor(
//                    ContextCompat.getColor(
//                        binding.root.context,
//                        R.color.green
//                    )
//                )
//            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = dataList[position]
        holder.bind(data)



    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setData(toDoData:List<ToDoData> ){
        val todoDiffUtil = TodoDiffUtil(dataList , toDoData)
        val todoDiffResult = DiffUtil.calculateDiff(todoDiffUtil)
        this.dataList = toDoData
        todoDiffResult.dispatchUpdatesTo(this)
        //notifyDataSetChanged()
    }

}