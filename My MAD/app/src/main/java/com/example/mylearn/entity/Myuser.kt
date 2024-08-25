package com.example.mylearn.entity  // entity package calling
import androidx.room.Entity// room Entity calling
import androidx.room.PrimaryKey// room PrimaryKey calling
//(Stevdza-San, 2020)
@Entity(tableName = "myuser_table")//creating table
data class Myuser (
    @PrimaryKey(autoGenerate = true)
    val userid: Int = 0,
    val u_name: String,
    val u_password: String,
    val u_email: String,
    val u_dob: String,
    val created_date: Long,
    val updated_date: Long
)
//Stevdza-San (2020) ROOM Database - #2 Insert Data | Android Studio Tutorial. [Online]. Available from: https://www.youtube.com/watch?v=UBCAWfztTrQ&list=PLSrm9z4zp4mEPOfZNV9O-crOhoMa0G2-o&index=3 [Accessed 10 August 2024].
