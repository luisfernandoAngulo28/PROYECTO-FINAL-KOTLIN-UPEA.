package com.developers.android.proyectofinal

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.developers.android.proyectofinal.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adaptador: TareaAdapter
    private lateinit var preferencias: SharedPreferences
    private val listaTareas = mutableListOf<Tarea>()
    private val gson = Gson()
    private val agregarTareaLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.getSerializableExtra("tarea")?.let { tarea ->
                listaTareas.add(0, tarea as Tarea)
                adaptador.notifyItemInserted(0)
                binding.recyclerViewTareas.smoothScrollToPosition(0)
                guardarTareas()
            }
        }
    }

    private val editarTareaLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()

    ) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val tareaEditada = result.data?.getSerializableExtra("tarea") as? Tarea
            val posicion = result.data?.getIntExtra("posicion", -1) ?: -1

            if(tareaEditada != null && posicion != -1){
                listaTareas[posicion] = tareaEditada
                adaptador.notifyItemChanged(posicion)
                guardarTareas()
            }
        }
    }

    private fun guardarTareas() {
        preferencias.edit().putString("listaTareas", gson.toJson(listaTareas)).apply()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.botonAgregarTarea.setOnClickListener {
            agregarTareaLauncher.launch(Intent(this, RegistrarTareaActivity::class.java))
        }
        preferencias = getSharedPreferences("TareasPrefs", MODE_PRIVATE)
        configurarRecyclerView()
        cargarTareas()
        Log.d("MainActivity", "Log despues de cargar")

    }

    private fun cargarTareas() {
        preferencias.getString("listaTareas", null)?.let { json ->

            Log.d("MainActivity", "cargarTareas()")
            val tipo = object : TypeToken<MutableList<Tarea>>() {}.type
            val tareasGuardadas: MutableList<Tarea> = gson.fromJson(json, tipo)
            listaTareas.clear()
            listaTareas.addAll(tareasGuardadas)
            Log.d("MainActivity", tareasGuardadas.toString())
            adaptador.notifyDataSetChanged()

        }
    }

    private fun configurarRecyclerView() {
        adaptador = TareaAdapter(
            listaTareas,
            onEditarClick = { tarea, posicion -> editarTarea(tarea, posicion) },
            onEliminarClick = { posicion -> eliminarTarea(posicion) }
        )
        binding.recyclerViewTareas.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = adaptador
        }
    }

    private fun eliminarTarea(posicion: Int) {
        listaTareas.removeAt(posicion)
        adaptador.notifyItemRemoved(posicion)
        guardarTareas()

    }

    private fun editarTarea(
        tarea: Tarea,
        posicion: Int
    ) {
        val intent = Intent(this, RegistrarTareaActivity::class.java).apply {
            putExtra("tarea", tarea)
            putExtra("posicion", posicion)
        }
        editarTareaLauncher.launch(intent)
    }
}