package com.example.mylearn.entity// entity package calling
import androidx.room.Entity// room Entity calling
import androidx.room.PrimaryKey// room PrimaryKey calling
//(Stevdza-San, 2020)
@Entity(tableName = "mycmt_table")//creating table
data class Mycomment(
    @PrimaryKey(autoGenerate = true) val id_cmt: Int = 0,
    val cmt_content: String,
    val cmtby_name: String,
    val id_post: Int
)
//Stevdza-San (2020) ROOM Database - #2 Insert Data | Android Studio Tutorial. [Online]. Available from: https://www.youtube.com/watch?v=UBCAWfztTrQ&list=PLSrm9z4zp4mEPOfZNV9O-crOhoMa0G2-o&index=3 [Accessed 10 August 2024].