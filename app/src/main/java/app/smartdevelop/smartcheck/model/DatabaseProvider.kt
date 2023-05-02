package app.smartdevelop.smartcheck.model

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    private var database: Checklistdb? = null

    fun getDatabase(context: Context): Checklistdb {
        if (database == null) {
            database = Room.databaseBuilder(context.applicationContext, Checklistdb::class.java, "my-database")
                .fallbackToDestructiveMigration()
                .build()
        }
        return database!!
    }
}