package com.developers.android.proyectofinal

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object NotificacionHelper {
    
    fun programarNotificacion(context: Context, tarea: Tarea) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        
        val intent = Intent(context, NotificacionReceiver::class.java).apply {
            putExtra("nombreTarea", tarea.nombre)
            putExtra("idTarea", tarea.id)
        }
        
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            tarea.id.hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        
        val tiempoNotificacion = obtenerTiempoDesdefecha(tarea.fechaRecordatorio)
        
        if (tiempoNotificacion > System.currentTimeMillis()) {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                tiempoNotificacion,
                pendingIntent
            )
        }
    }
    
    fun cancelarNotificacion(context: Context, idTarea: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificacionReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            idTarea.hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_NO_CREATE
        )
        
        pendingIntent?.let {
            alarmManager.cancel(it)
            it.cancel()
        }
    }
    
    private fun obtenerTiempoDesdefecha(fecha: String): Long {
        return try {
            val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = formato.parse(fecha)
            val calendar = Calendar.getInstance()
            date?.let {
                calendar.time = it
                calendar.set(Calendar.HOUR_OF_DAY, 9)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.timeInMillis
            } ?: System.currentTimeMillis()
        } catch (e: Exception) {
            System.currentTimeMillis()
        }
    }
}
