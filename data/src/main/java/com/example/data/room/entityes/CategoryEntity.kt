package com.example.data.room.entityes

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "categories",
    indices = [Index("id")])
data class CategoryEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val emoji: String,
    val isIncome: Boolean
)