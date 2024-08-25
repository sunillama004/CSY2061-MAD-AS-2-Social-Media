package com.example.mylearn.repository // repository package calling
import com.example.mylearn.entity.Mycomment // MycommentDao bringing
import kotlinx.coroutines.flow.Flow // Flow bringing
//(Stevdza-San, 2020)
interface MycommentsRepositoryInterface {
     // fetching all comments
    fun getAllMycommentsStream(): Flow<List<Mycomment>>
    // fetching with the id
    fun getMycommentStream(id: Int): Flow<Mycomment?>
    //  for insert data
    suspend fun insertMycomment(mycomment: Mycomment)
    // for delete item
    suspend fun deleteMycomment(mycomment: Mycomment)
    // for update item
    suspend fun updateMycomment(mycomment: Mycomment)
}
//Stevdza-San (2020) ROOM Database - #2 Insert Data | Android Studio Tutorial. [Online]. Available from: https://www.youtube.com/watch?v=UBCAWfztTrQ&list=PLSrm9z4zp4mEPOfZNV9O-crOhoMa0G2-o&index=3 [Accessed 10 August 2024].
//Stevdza-San (2020) ROOM Database - #4 Update Data | Android Studio Tutorial. [Online]. Available from: https://www.youtube.com/watch?v=5rfBU75sguk&list=PLSrm9z4zp4mEPOfZNV9O-crOhoMa0G2-o&index=4 [Accessed 11 August 2024].