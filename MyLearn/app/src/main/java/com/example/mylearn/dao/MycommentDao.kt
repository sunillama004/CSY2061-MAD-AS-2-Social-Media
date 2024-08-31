package com.example.mylearn.dao// dao package calling
import androidx.room.*// all room package calling
import com.example.mylearn.entity.Mycomment//Mycomment calling
import kotlinx.coroutines.flow.Flow//Flow calling
//(Stevdza-San, 2020)
@Dao//dao signature
interface MycommentDao {
    // fetching all comments
    @Query("SELECT * FROM mycmt_table")
    fun getAllMycomments(): Flow<List<Mycomment>>
    // fetching with the id
    @Query("SELECT * FROM mycmt_table WHERE id_cmt = :id_cmt")
    fun getMycomment(id_cmt: Int): Flow<Mycomment?>
    //  for insert data
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(mycomment: Mycomment)
    //  for delete data
    @Delete
    suspend fun delete(mycomment: Mycomment)
    //  for update data
    @Update
    suspend fun update(mycomment: Mycomment)
}
//Stevdza-San (2020) ROOM Database - #2 Insert Data | Android Studio Tutorial. [Online]. Available from: https://www.youtube.com/watch?v=UBCAWfztTrQ&list=PLSrm9z4zp4mEPOfZNV9O-crOhoMa0G2-o&index=3 [Accessed 10 August 2024].
//Stevdza-San (2020) ROOM Database - #4 Update Data | Android Studio Tutorial. [Online]. Available from: https://www.youtube.com/watch?v=5rfBU75sguk&list=PLSrm9z4zp4mEPOfZNV9O-crOhoMa0G2-o&index=4 [Accessed 11 August 2024].