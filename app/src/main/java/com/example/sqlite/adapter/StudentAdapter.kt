package com.example.sqlite.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sqlite.R
import com.example.sqlite.sqlite.model.Student

class StudentAdapter : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    private var studentList : ArrayList<Student> = ArrayList()
    private var onClickItem : ((Student)-> Unit)? = null
    private var onClickDeleteItem : ((Student)-> Unit)? = null

    fun setOnClickDeleteItem(callback: (Student)->Unit){
        this.onClickDeleteItem = callback
    }

    fun setOnClickItem(callback: (Student)->Unit){
        this.onClickItem = callback
    }

    fun addItems(items : ArrayList<Student>){
        this.studentList = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = StudentViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.items_student, parent, false)
    )

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = studentList[position]
        holder.bindView(student)
        holder.itemView.setOnClickListener {onClickItem!!.invoke(student)}
        holder.deleteBtn.setOnClickListener { onClickDeleteItem?.invoke(student) }
    }

    override fun getItemCount(): Int  = studentList.size

    class StudentViewHolder(var view: View) : RecyclerView.ViewHolder(view){
        private var id = view.findViewById<TextView>(R.id.tv_id)
        private var name = view.findViewById<TextView>(R.id.tv_Name)
        private var email = view.findViewById<TextView>(R.id.tv_Email)
        var deleteBtn = view.findViewById<Button>(R.id.btn_delete)

        fun bindView(student : Student){
            id.text = student.id.toString()
            name.text = student.name
            email.text = student.email
        }
    }
}