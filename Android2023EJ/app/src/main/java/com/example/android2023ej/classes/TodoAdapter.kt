package com.example.android2023ej.classes

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.android2023ej.JsonFragmentDirections
import com.example.android2023ej.databinding.TodoItemBinding


class TodoAdapter(private val todos: List<todoDataClass>) : RecyclerView.Adapter<TodoViewHolder>() {

    private var _binding: TodoItemBinding? = null

    private val binding get() = _binding!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        _binding = TodoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(todos[position])
    }



    override fun getItemCount() = todos.size
}
class TodoViewHolder(itemView: TodoItemBinding) : RecyclerView.ViewHolder(itemView.root), View.OnClickListener {
private var view: TodoItemBinding = itemView
    private var todos: todoDataClass? = null

    // Initializes on click listener
    init {
        itemView.root.setOnClickListener(this)
    }

    fun bind(todo: todoDataClass) {
        todos = todo
        view.todoTitle.text = todo.title

        // Adds a checkmark or X whether the task is completed or not
        if (todo.completed == true) {
            view.todoStatus.text = "✔"
            view.todoStatus.setTextColor(Color.parseColor("#00ff00"))
        }
        else{
            view.todoStatus.text = "X"
            view.todoStatus.setTextColor(Color.parseColor("#FF0000"))

        }}

    // Psses these values and goes to TodoDetailFragment on click
    override fun onClick(itemView: View) {
        val sentuid = todos!!.userId!!.toInt()
        val sentid = todos!!.id!!.toInt()
        val senttitle= todos!!.title!!.toString()
        val sentcomplete = todos!!.completed as Boolean
        val action = JsonFragmentDirections.actionJsonFragmentToTodoDetailFragment(sentuid, sentid, senttitle, sentcomplete)
        itemView.findNavController().navigate(action)
    }
}

