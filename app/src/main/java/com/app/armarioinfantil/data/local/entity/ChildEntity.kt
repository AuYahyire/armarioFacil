package com.app.armarioinfantil.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "ninos")
data class ChildEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val nombre: String,

    @ColumnInfo(name = "fecha_nacimiento")
    val fechaNacimiento: Long,

    @ColumnInfo(name = "foto_perfil_uri")
    val fotoPerfilUri: String? = null,

    @ColumnInfo(name = "fecha_creacion")
    val fechaCreacion: Long = System.currentTimeMillis()
)
