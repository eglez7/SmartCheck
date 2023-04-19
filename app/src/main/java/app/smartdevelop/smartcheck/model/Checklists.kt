package app.smartdevelop.smartcheck.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "checklists")
data class Checklists(
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    val name: String
)