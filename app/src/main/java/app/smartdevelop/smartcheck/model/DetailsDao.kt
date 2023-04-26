package app.smartdevelop.smartcheck.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import app.smartdevelop.smartcheck.model.Details

@Dao
interface DetailsDao {
    @Insert
    suspend fun insert(vararg details: Details)

    @Query("SELECT * FROM details WHERE ListId = :id")
    suspend fun getDetailsById(id: Int): List<Details>

    @Update
    suspend fun update(details: Details)

    @Delete
    suspend fun delete(details: Details)

    @Delete
    suspend fun deleteAll(details: List<Details>)
}