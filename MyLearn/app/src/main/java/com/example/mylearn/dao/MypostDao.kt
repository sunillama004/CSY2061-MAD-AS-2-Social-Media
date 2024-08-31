package com.example.mylearn.dao // dao package calling
import androidx.room.*// all room package calling
import com.example.mylearn.entity.Mypost//Mypost calling
import kotlinx.coroutines.flow.Flow//Flow calling
//(Stevdza-San, 2020)
@Dao
interface MypostDao {
    // fetching all posts
    @Query("SELECT * FROM mypost_table")
    fun getAllMyposts(): Flow<List<Mypost>>
    // fetching with id
    @Query("SELECT * FROM mypost_table WHERE id_post = :id")
    fun getMypost(id: Int): Flow<Mypost?>
    // fetching with email
    @Query("SELECT * FROM mypost_table WHERE postby_email = :uemail")
    fun getMyPostsByEmail(uemail: String): Flow<List<Mypost>>
    //  for insert data
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(mypost: Mypost)
    //  for delete data
    @Delete
    suspend fun delete(mypost: Mypost)
    //  for update data
    @Update
    suspend fun update(mypost: Mypost)
}
//Stevdza-San (2020) ROOM Database - #2 Insert Data | Android Studio Tutorial. [Online]. Available from: https://www.youtube.com/watch?v=UBCAWfztTrQ&list=PLSrm9z4zp4mEPOfZNV9O-crOhoMa0G2-o&index=3 [Accessed 10 August 2024].
//Stevdza-San (2020) ROOM Database - #4 Update Data | Android Studio Tutorial. [Online]. Available from: https://www.youtube.com/watch?v=5rfBU75sguk&list=PLSrm9z4zp4mEPOfZNV9O-crOhoMa0G2-o&index=4 [Accessed 11 August 2024].