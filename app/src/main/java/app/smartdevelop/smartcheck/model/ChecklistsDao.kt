package app.smartdevelop.smartcheck.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import app.smartdevelop.smartcheck.model.Checklists

@Dao
interface ChecklistsDao {
    @Insert
    suspend fun insert(vararg checklists: Checklists)

    @Query("SELECT * FROM checklists")
    suspend fun getChecklists(): List<Checklists>

    @Query("SELECT * FROM checklists WHERE id = :id LIMIT 1")
    suspend fun getChecklistsById(id: Int): Checklists?

    @Update
    suspend fun update(checklists: Checklists)

    @Delete
    suspend fun delete(checklists: Checklists)

    @Delete
    suspend fun deleteAll(checklists: List<Checklists>)
}