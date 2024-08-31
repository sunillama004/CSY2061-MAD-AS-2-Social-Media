package com.example.mylearn.database  // database package calling
import android.content.Context// Context calling
import androidx.room.Database// Database calling
import androidx.room.Room// Room calling
import androidx.room.RoomDatabase// RoomDatabase calling
import com.example.mylearn.dao.MycommentDao// MycommentDao calling
import com.example.mylearn.dao.MypostDao// MypostDao calling
import com.example.mylearn.dao.MyuserDao// MyuserDao calling
import com.example.mylearn.entity.Mycomment// Mycomment calling
import com.example.mylearn.entity.Mypost// Mypost calling
import com.example.mylearn.entity.Myuser// Myuser calling
//(Stevdza-San, 2020)
@Database(entities = [Myuser::class, Mypost::class, Mycomment::class], version = 7, exportSchema = false)
abstract class MyMainDatabase : RoomDatabase() {
    abstract fun myuserDao(): MyuserDao//creating the abstract fun of MyuserDao
    abstract fun mypostDao(): MypostDao//creating the abstract fun of MypostDao
    abstract fun mycommentDao(): MycommentDao//creating the abstract fun of MycommentDao
    companion object {
        @Volatile
        private var Instance: MyMainDatabase? = null
        fun getMyMainDatabase(context: Context): MyMainDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, MyMainDatabase::class.java, "my_database") // database name
                    .fallbackToDestructiveMigration() // for handle without migrations
                    .build()//creating database and initializing
                    .also { Instance = it }
            }
        }
    }
}
//Stevdza-San (2020) ROOM Database - #2 Insert Data | Android Studio Tutorial. [Online]. Available from: https://www.youtube.com/watch?v=UBCAWfztTrQ&list=PLSrm9z4zp4mEPOfZNV9O-crOhoMa0G2-o&index=3 [Accessed 10 August 2024].