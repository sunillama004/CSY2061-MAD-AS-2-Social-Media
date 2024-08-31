package com.example.mylearn.database// database package calling
import android.content.Context// Context calling
import com.example.mylearn.repository.MycommentsRepository// MycommentsRepository calling
import com.example.mylearn.repository.MycommentsRepositoryInterface// MycommentsRepositoryInterface calling
import com.example.mylearn.repository.MypostsRepository// MypostsRepository calling
import com.example.mylearn.repository.MypostsRepositoryInterface// MypostsRepositoryInterface calling
import com.example.mylearn.repository.MyusersRepository// MyusersRepository calling
import com.example.mylearn.repository.MyusersRepositoryInterface// MyusersRepositoryInterface calling
//(Stevdza-San, 2020)
//(Jeff Atwood and Joel Spolsky, 2008)
interface MyDatabaseContainer {
    val MyusersRepositoryInterface: MyusersRepositoryInterface// MyusersRepositoryInterface declaration
    val MypostsRepositoryInterface: MypostsRepositoryInterface// MypostsRepositoryInterface declaration
    val MycommentsRepositoryInterface: MycommentsRepositoryInterface// MycommentsRepositoryInterface declaration
}
class MyDatabaseDataContainer(private val context: Context) : MyDatabaseContainer {
    // uses lazy initializing for MycommentsRepositoryInterface
    override val MycommentsRepositoryInterface: MycommentsRepositoryInterface by lazy {
        MycommentsRepository(MyMainDatabase.getMyMainDatabase(context).mycommentDao())
    }
    // uses lazy initializing for MyusersRepositoryInterface
    override val MyusersRepositoryInterface: MyusersRepositoryInterface by lazy {
        MyusersRepository(MyMainDatabase.getMyMainDatabase(context).myuserDao())
    }
    // uses lazy initializing for MypostsRepositoryInterface
    override val MypostsRepositoryInterface: MypostsRepositoryInterface by lazy {
        MypostsRepository(MyMainDatabase.getMyMainDatabase(context).mypostDao())
    }
}
//Stevdza-San (2020) ROOM Database - #2 Insert Data | Android Studio Tutorial. [Online]. Available from: https://www.youtube.com/watch?v=UBCAWfztTrQ&list=PLSrm9z4zp4mEPOfZNV9O-crOhoMa0G2-o&index=3 [Accessed 10 August 2024].
// Jeff Atwood and Joel Spolsky (2008) stackoverflow. [Online]. Available from:https://stackoverflow.com [Accessed 10 August 2024].