package com.example.todo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class TodoAdapter(context: Context, private val todoList: List<Todo>) : ArrayAdapter<Todo>(context, 0, todoList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listItemView = convertView
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false)
        }
        val currentItem = getItem(position)
        val textView = listItemView!!.findViewById<TextView>(android.R.id.text1)
        textView.text = currentItem?.text
        return listItemView
    }
}