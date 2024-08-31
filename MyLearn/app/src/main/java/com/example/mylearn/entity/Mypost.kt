package com.example.mylearn.entity  // entity package calling
import androidx.room.Entity// room Entity calling
import androidx.room.PrimaryKey// room PrimaryKey calling
//(Stevdza-San, 2020)
@Entity(tableName = "mypost_table")//creating table
data class Mypost (
    @PrimaryKey(autoGenerate = true)
    val id_post: Int = 0,
    val postby_name: String,
    val postby_email: String,
    val post_content: String,
    val post_like: Int,
    val post_dislike: Int
)
//Stevdza-San (2020) ROOM Database - #2 Insert Data | Android Studio Tutorial. [Online]. Available from: https://www.youtube.com/watch?v=UBCAWfztTrQ&list=PLSrm9z4zp4mEPOfZNV9O-crOhoMa0G2-o&index=3 [Accessed 10 August 2024].