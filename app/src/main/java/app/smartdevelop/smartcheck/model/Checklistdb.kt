package app.smartdevelop.smartcheck.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Checklists::class, Details::class], version = 1)
abstract class Checklistdb : RoomDatabase() {
    abstract fun checklistsDao(): ChecklistsDao
    abstract fun detailsDao(): DetailsDao
}