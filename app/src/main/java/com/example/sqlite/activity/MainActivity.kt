package com.example.sqlite.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sqlite.R
import com.example.sqlite.adapter.StudentAdapter
import com.example.sqlite.sqlite.helper.SQLiteHelper
import com.example.sqlite.sqlite.model.Student

class MainActivity : AppCompatActivity() {

    private lateinit var nameEt : EditText
    private lateinit var emailEt : EditText
    private lateinit var addBtn : Button
    private lateinit var viewBtn : Button
    private lateinit var updateBtn : Button


    private lateinit var sqliteHelper : SQLiteHelper
    private lateinit var recyclerView: RecyclerView
    private var adapter : StudentAdapter? = null
    private var student : Student ? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        initRecyclerView()

        sqliteHelper = SQLiteHelper(this)

        addBtn.setOnClickListener { addStudent() }
        viewBtn.setOnClickListener { getStudents() }
        updateBtn.setOnClickListener { updateStudent() }

        adapter?.setOnClickItem {
            Toast.makeText(this, it.name, Toast.LENGTH_SHORT).show()
            nameEt.setText(it.name)
            emailEt.setText(it.email)
            student = it
        }

        adapter?.setOnClickDeleteItem {
            deleteStudent(it.id)
        }
    }

    private fun getStudents(){
        val studentList = sqliteHelper.getAllStudents()
        Log.e("PPPP", "${studentList.size}")

        //ok we need to display data in recyclerview
        adapter?.addItems(studentList)
    }

    private fun addStudent(){
        val name = nameEt.text.toString()
        val email = emailEt.text.toString()

        if(name.isEmpty() || email.isEmpty()){
            Toast.makeText(this, "Please enter required field", Toast.LENGTH_SHORT).show()
        }else{
            val student = Student(name = name, email = email)
            val status = sqliteHelper.insertStudent(student)
            //check insert success or not success

            if(status > -1){
                Toast.makeText(this, "Student added...", Toast.LENGTH_SHORT).show()
                clearEditTexts()
                getStudents()
            }else{
                Toast.makeText(this, "Record not saved", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun updateStudent(){
        val name = nameEt.text.toString()
        val email = emailEt.text.toString()

        if(name == student!!.name && email == student!!.email){
            Toast.makeText(this, "Record not changed", Toast.LENGTH_SHORT).show()
            return
        }

        if(student == null) return

        val student = Student(id = student!!.id, name = name, email = email)
        val status = sqliteHelper.updateStudent(student)
        if(status > -1){
            clearEditTexts()
            getStudents()
        }else{
            Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteStudent(id : Int){
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to delete item")
        builder.setCancelable(true)
        builder.setPositiveButton("Yes"){ dialog, _ ->
            sqliteHelper.deleteStudentById(id)
            getStudents()
            dialog.dismiss()
        }
        builder.setNegativeButton("No"){ dialog,_->
            dialog.dismiss()
        }

        val alter = builder.create()
        alter.show()
    }

    private fun clearEditTexts(){
        nameEt.setText("")
        emailEt.setText("")
        nameEt.requestFocus()
    }

    private fun initRecyclerView(){
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = StudentAdapter()
        recyclerView.adapter = adapter
    }

    private fun initViews(){
        nameEt = findViewById(R.id.et_name)
        emailEt = findViewById(R.id.et_email)
        addBtn = findViewById(R.id.btn_add)
        viewBtn = findViewById(R.id.btn_view)
        updateBtn = findViewById(R.id.btn_update)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}