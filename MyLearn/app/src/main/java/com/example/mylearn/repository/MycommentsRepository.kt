package com.example.mylearn.repository // repository package calling
import com.example.mylearn.dao.MycommentDao // MycommentDao bringing
import com.example.mylearn.entity.Mycomment // Mycomment bringing
import kotlinx.coroutines.flow.Flow // Flow bringing
//(Stevdza-San, 2020)
class MycommentsRepository(private val mycommentDao: MycommentDao) : MycommentsRepositoryInterface {
    // fetching all comments
    override fun getAllMycommentsStream(): Flow<List<Mycomment>> = mycommentDao.getAllMycomments()
    // fetching with the id
    override fun getMycommentStream(id: Int): Flow<Mycomment?> = mycommentDao.getMycomment(id)
    //  for insert data
    override suspend fun insertMycomment(mycomment: Mycomment) = mycommentDao.insert(mycomment)
    // for delete item
    override suspend fun deleteMycomment(mycomment: Mycomment) = mycommentDao.delete(mycomment)
    // for update item
    override suspend fun updateMycomment(mycomment: Mycomment) = mycommentDao.update(mycomment)
}
//Stevdza-San (2020) ROOM Database - #2 Insert Data | Android Studio Tutorial. [Online]. Available from: https://www.youtube.com/watch?v=UBCAWfztTrQ&list=PLSrm9z4zp4mEPOfZNV9O-crOhoMa0G2-o&index=3 [Accessed 10 August 2024].
//Stevdza-San (2020) ROOM Database - #4 Update Data | Android Studio Tutorial. [Online]. Available from: https://www.youtube.com/watch?v=5rfBU75sguk&list=PLSrm9z4zp4mEPOfZNV9O-crOhoMa0G2-o&index=4 [Accessed 11 August 2024].