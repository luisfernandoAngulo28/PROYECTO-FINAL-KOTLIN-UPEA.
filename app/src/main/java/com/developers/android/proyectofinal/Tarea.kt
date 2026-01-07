package com.developers.android.proyectofinal

import com.developers.android.proyectofinal.R
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

data class Tarea(
    val id: String = UUID.randomUUID().toString(),
    val nombre: String,
    val nivel: NivelPrioridad,
    val fechaCreacion: String = obtenerFechaActual(),
    val fechaRecordatorio: String,
    var completada: Boolean = false
): Serializable {
    companion object {
        private fun obtenerFechaActual(): String {
            val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            return formato.format(Date())
        }

    }
}

enum class NivelPrioridad(val texto: String, val colorRes: Int) {
    BAJO("Prioridad: Baja", R.color.prioridada_baja),
    MEDIO("Prioridad: Medio", R.color.prioridad_media),
    ALTO("Prioridad: Alta", R.color.prioridad_alta),
}