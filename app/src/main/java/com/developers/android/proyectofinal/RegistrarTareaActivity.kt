package com.developers.android.proyectofinal

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.developers.android.proyectofinal.R
import com.developers.android.proyectofinal.databinding.ActivityRegistrarTareaBinding
import java.text.SimpleDateFormat
import java.util.Locale

class RegistrarTareaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrarTareaBinding
    private var tareaEditar: Tarea? = null
    private var posicionEditar: Int = -1
    private val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrarTareaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tareaEditar = intent.getSerializableExtra("tarea") as? Tarea
        Log.d("ValoresFORM", tareaEditar.toString())
        posicionEditar = intent.getIntExtra("posicion", -1)

        configurarUI()
        configurarEventos()
    }

    private fun configurarEventos() {
        binding.botonSeleccionarFecha.setOnClickListener { mostrarDatePicker() }
        binding.botonGuardar.setOnClickListener { guardarTarea() }
        binding.botonCancelar.setOnClickListener { finish() }
    }

    private fun guardarTarea() {
        val nombreTarea = binding.campoNombreTarea.text.toString().trim()
        // Validaciones
        if (!validarFormulario(nombreTarea)) return
        val nivelSeleccionado = obtenerNivelSeleccionado() ?: return
        val fechaRecordatorio = binding.textoFechaRecordatorio.text.toString()
        Log.d("ValoresFORM", "${nombreTarea}, ${nivelSeleccionado},  ${fechaRecordatorio}")

        val tarea = crearOActualizar(nombreTarea, nivelSeleccionado, fechaRecordatorio)
        setResult(RESULT_OK, Intent().apply {
            putExtra("tarea", tarea)
            if (posicionEditar != -1) putExtra("posicion", posicionEditar)
        })
        finish()
    }

    private fun crearOActualizar(
        nombreTarea: String,
        nivelSeleccionado: NivelPrioridad,
        fechaRecordatorio: String
    ): Tarea {
        return tareaEditar?.copy(
            nombre = nombreTarea,
            nivel = nivelSeleccionado,
            fechaRecordatorio = fechaRecordatorio
        ) ?: Tarea(
            nombre = nombreTarea,
            nivel = nivelSeleccionado,
            fechaCreacion = binding.textoFechaCreacion.text.toString(),
            fechaRecordatorio = fechaRecordatorio
        )
    }

    private fun validarFormulario(nombreTarea: String): Boolean {
        when {
            nombreTarea.isEmpty() -> {
                binding.campoNombreTarea.error = "Ingrese nombre de la tarea"
                return false
            }

            binding.textoFechaRecordatorio.text == "Fecha recordatorio" -> {
                Toast.makeText(this, "Seleccione una fecha de recordatorio", Toast.LENGTH_SHORT)
                    .show()
                return false
            }
        }
        return true
    }

    private fun obtenerNivelSeleccionado(): NivelPrioridad? {
        return when (binding.grupoNivel.checkedRadioButtonId) {
            R.id.radioNivelBajo -> NivelPrioridad.BAJO
            R.id.radioNivelMedio -> NivelPrioridad.MEDIO
            R.id.radioNivelAlto -> NivelPrioridad.ALTO
            else -> {
                Toast.makeText(this, "Seleccione un nivel de prioridad", Toast.LENGTH_SHORT).show()
                null
            }
        }
    }


    private fun mostrarDatePicker() {
        val calendario = obtenerCalendarioInicial()
        DatePickerDialog(
            this,
            { _, year, month, day ->
                calendario.set(year, month, day)
                binding.textoFechaRecordatorio.text = formatoFecha.format(calendario.time)
            },
            calendario.get(Calendar.YEAR),
            calendario.get(Calendar.MONTH),
            calendario.get(Calendar.DAY_OF_MONTH),
        ).apply {
            datePicker.minDate = System.currentTimeMillis()
            show()
        }
    }

    private fun obtenerCalendarioInicial(): Calendar {
        val cal = Calendar.getInstance()
        return cal
    }

    fun configurarUI() {
        tareaEditar?.let { tarea ->
            // Modo edicion
            binding.tituloActividad.text = "Editar Tarea"
            binding.botonGuardar.text = "Actualizar"
            cergarDatosTarea(tarea)
        } ?: run {
            // Modo de creacion
            binding.tituloActividad.text = "Nueva Tarea"
        }
    }

    private fun cergarDatosTarea(tarea: Tarea) {
        binding.apply {
            campoNombreTarea.setText(tarea.nombre)
            textoFechaCreacion.text = tarea.fechaCreacion
            textoFechaRecordatorio.text = tarea.fechaRecordatorio

            when(tarea.nivel){
                NivelPrioridad.BAJO -> radioNivelBajo.isChecked = true
                NivelPrioridad.MEDIO -> radioNivelMedio.isChecked = true
                NivelPrioridad.ALTO -> radioNivelAlto.isChecked = true
            }
        }
    }
}