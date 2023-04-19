package app.smartdevelop.smartcheck.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "details")
data class Details(
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    val detail: String,
    var selected: Boolean,
    val listId: Int
)