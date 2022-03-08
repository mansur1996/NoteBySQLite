package com.example.sqlite.sqlite.helper

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.sqlite.sqlite.model.Student
import java.lang.Exception

class SQLiteHelper(context: Context) :SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{

        private const val DATABASE_NAME = "student.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_STUDENT = "student"
        private const val ID = "id"
        private const val NAME = "name"
        private const val EMAIL = "email"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableStudent = ("CREATE TABLE " + TABLE_STUDENT + "("
                + ID + " INTEGER PRIMARY KEY," + NAME + " TEXT,"
                + EMAIL + " TEXT" + ")")
        db?.execSQL(createTableStudent)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_STUDENT")
        onCreate(db)
    }

    fun insertStudent(std: Student) : Long{
        val db = this.writableDatabase

        val contentValue = ContentValues()
        contentValue.put(ID, std.id)
        contentValue.put(NAME, std.name)
        contentValue.put(EMAIL, std.email)

        val success = db.insert(TABLE_STUDENT, null, contentValue)
        db.close()
        return success
    }

    @SuppressLint("Range", "Recycle")
    fun getAllStudents() : ArrayList<Student>{
        val studentList : ArrayList<Student> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_STUDENT"
        val db = this.readableDatabase

        val cursor : Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        }catch (e : Exception){
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id : Int
        var name : String
        var email : String

        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex("id"))
                name = cursor.getString(cursor.getColumnIndex("name"))
                email = cursor.getString(cursor.getColumnIndex("email"))


                val student = Student(id = id, name = name, email = email)
                studentList.add(student)
            }while (cursor.moveToNext())
        }
        return studentList
    }

    fun updateStudent(student : Student) : Int {
        val db = this.writableDatabase

        val contentValue = ContentValues()
        contentValue.put(ID, student.id)
        contentValue.put(NAME, student.name)
        contentValue.put(EMAIL, student.email)

        val success = db.update(TABLE_STUDENT, contentValue, "id=" + student.id, null)
        db.close()
        return success
    }

    fun deleteStudentById(id : Int) : Int{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID, id)

        val success = db.delete(TABLE_STUDENT, "id=$id", null)
        db.close()
        return success
    }
}