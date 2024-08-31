package com.example.mylearn.repository // repository package calling
import com.example.mylearn.entity.Mypost // Mypost bringing
import kotlinx.coroutines.flow.Flow // Flow bringing
//(Stevdza-San, 2020)
interface MypostsRepositoryInterface {
    // fetching all posts
    fun getAllMypostsStream(): Flow<List<Mypost>>
    // fetching with id
    fun getMypostStream(id: Int): Flow<Mypost?>
    // fetching with email
    fun getMyPostsByEmail(uemail: String): Flow<List<Mypost>>
    //  for insert data
    suspend fun insertMypost(mypost: Mypost)
    // for delete item
    suspend fun deleteMypost(mypost: Mypost)
    // for update item
    suspend fun updateMypost(mypost: Mypost)
}
//Stevdza-San (2020) ROOM Database - #2 Insert Data | Android Studio Tutorial. [Online]. Available from: https://www.youtube.com/watch?v=UBCAWfztTrQ&list=PLSrm9z4zp4mEPOfZNV9O-crOhoMa0G2-o&index=3 [Accessed 10 August 2024].
//Stevdza-San (2020) ROOM Database - #4 Update Data | Android Studio Tutorial. [Online]. Available from: https://www.youtube.com/watch?v=5rfBU75sguk&list=PLSrm9z4zp4mEPOfZNV9O-crOhoMa0G2-o&index=4 [Accessed 11 August 2024].