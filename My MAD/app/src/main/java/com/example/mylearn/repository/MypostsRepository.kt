package com.example.mylearn.repository // repository package calling
import com.example.mylearn.dao.MypostDao // MypostDao bringing
import com.example.mylearn.entity.Mypost // Mypost bringing
import kotlinx.coroutines.flow.Flow // Flow bringing
//(Stevdza-San, 2020)
class MypostsRepository(private val mypostDao: MypostDao) : MypostsRepositoryInterface {
    // fetching all posts
    override fun getAllMypostsStream(): Flow<List<Mypost>> = mypostDao.getAllMyposts()
    // fetching with id
    override fun getMypostStream(id: Int): Flow<Mypost?> = mypostDao.getMypost(id)
    // fetching with email
    override fun getMyPostsByEmail(uemail: String): Flow<List<Mypost>> = mypostDao.getMyPostsByEmail(uemail)
    //  for insert data
    override suspend fun insertMypost(mypost: Mypost) = mypostDao.insert(mypost)
    // for delete item
    override suspend fun deleteMypost(mypost: Mypost) = mypostDao.delete(mypost)
    // for update item
    override suspend fun updateMypost(mypost: Mypost) = mypostDao.update(mypost)
}
//Stevdza-San (2020) ROOM Database - #2 Insert Data | Android Studio Tutorial. [Online]. Available from: https://www.youtube.com/watch?v=UBCAWfztTrQ&list=PLSrm9z4zp4mEPOfZNV9O-crOhoMa0G2-o&index=3 [Accessed 10 August 2024].
//Stevdza-San (2020) ROOM Database - #4 Update Data | Android Studio Tutorial. [Online]. Available from: https://www.youtube.com/watch?v=5rfBU75sguk&list=PLSrm9z4zp4mEPOfZNV9O-crOhoMa0G2-o&index=4 [Accessed 11 August 2024].
