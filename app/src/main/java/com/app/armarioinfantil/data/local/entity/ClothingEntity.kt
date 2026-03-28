package com.app.armarioinfantil.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.ForeignKey

@Entity(
    tableName = "prendas",
    foreignKeys = [
        ForeignKey(
            entity = ChildEntity::class,
            parentColumns = ["id"],
            childColumns = ["nino_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ClothingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "nino_id", index = true)
    val ninoId: Long,

    @ColumnInfo(name = "foto_prenda_uri")
    val fotoPrendaUri: String,

    val nombre: String,

    val categoria: String,

    val temporada: String,

    val talla: String,

    @ColumnInfo(name = "color_principal")
    val colorPrincipal: String,

    @ColumnInfo(name = "estado_uso")
    val estadoUso: String
)
