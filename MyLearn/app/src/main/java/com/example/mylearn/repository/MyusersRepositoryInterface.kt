package com.example.mylearn.repository // repository package calling
import com.example.mylearn.entity.Myuser // Myuser bringing
import kotlinx.coroutines.flow.Flow // Flow bringing
//(Stevdza-San, 2020)
interface MyusersRepositoryInterface {
    // fetching email and password
    suspend fun getMyUserByEmailAndPassword(email: String, password: String): Myuser?
    // fetching all users
    fun getAllMyusersStream(): Flow<List<Myuser>>
    // fetching with userid
    fun getMyuserStream(userid: Int): Flow<Myuser?>
    // for insert item
    suspend fun insertMyuser(myuser: Myuser)
    // for delete item
    suspend fun deleteMyuser(myuser: Myuser)
    // for update item
    suspend fun updateMyuser(myuser: Myuser)
}
//Stevdza-San (2020) ROOM Database - #2 Insert Data | Android Studio Tutorial. [Online]. Available from: https://www.youtube.com/watch?v=UBCAWfztTrQ&list=PLSrm9z4zp4mEPOfZNV9O-crOhoMa0G2-o&index=3 [Accessed 10 August 2024].
//Stevdza-San (2020) ROOM Database - #4 Update Data | Android Studio Tutorial. [Online]. Available from: https://www.youtube.com/watch?v=5rfBU75sguk&list=PLSrm9z4zp4mEPOfZNV9O-crOhoMa0G2-o&index=4 [Accessed 11 August 2024].
