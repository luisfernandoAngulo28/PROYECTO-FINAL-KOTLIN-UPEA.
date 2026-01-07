package com.developers.android.proyectofinal

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.developers.android.proyectofinal.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adaptador: TareaAdapter
    private lateinit var preferencias: SharedPreferences
    private val listaTareas = mutableListOf<Tarea>()
    private val tareasFiltradas = mutableListOf<Tarea>()
    private var filtroActual = FiltroTarea.TODAS
    private var textoBusqueda = ""
    private val gson = Gson()
    
    enum class FiltroTarea {
        TODAS, PENDIENTES, COMPLETADAS
    }
    private val agregarTareaLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.getSerializableExtra("tarea")?.let { tarea ->
                listaTareas.add(0, tarea as Tarea)
                NotificacionHelper.programarNotificacion(this, tarea)
                guardarTareas()
                aplicarFiltro()
                actualizarContador()
                binding.recyclerViewTareas.smoothScrollToPosition(0)
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
                NotificacionHelper.cancelarNotificacion(this, listaTareas[posicion].id)
                NotificacionHelper.programarNotificacion(this, tareaEditada)
                guardarTareas()
                aplicarFiltro()
                actualizarContador()
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
        configurarSwipeParaEliminar()
        configurarBusqueda()
        cargarTareas()
        configurarFiltros()
        actualizarContador()
        solicitarPermisoNotificaciones()
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
            aplicarFiltro()
            actualizarContador()
        }
    }

    private fun configurarRecyclerView() {
        adaptador = TareaAdapter(
            tareasFiltradas,
            onEditarClick = { tarea, posicion -> editarTarea(tarea, posicion) },
            onEliminarClick = { posicion -> eliminarTareaFiltrada(posicion) },
            onCompletadaChanged = { posicion, completada -> 
                cambiarEstadoCompletadaFiltrada(posicion, completada)
            }
        )
        binding.recyclerViewTareas.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = adaptador
        }
    }
    
    private fun configurarFiltros() {
        binding.chipTodas.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                filtroActual = FiltroTarea.TODAS
                aplicarEstiloChip(binding.chipTodas, true)
                aplicarEstiloChip(binding.chipPendientes, false)
                aplicarEstiloChip(binding.chipCompletadas, false)
                aplicarFiltro()
            }
        }
        
        binding.chipPendientes.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                filtroActual = FiltroTarea.PENDIENTES
                aplicarEstiloChip(binding.chipTodas, false)
                aplicarEstiloChip(binding.chipPendientes, true)
                aplicarEstiloChip(binding.chipCompletadas, false)
                aplicarFiltro()
            }
        }
        
        binding.chipCompletadas.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                filtroActual = FiltroTarea.COMPLETADAS
                aplicarEstiloChip(binding.chipTodas, false)
                aplicarEstiloChip(binding.chipPendientes, false)
                aplicarEstiloChip(binding.chipCompletadas, true)
                aplicarFiltro()
            }
        }
    }
    
    private fun aplicarEstiloChip(chip: com.google.android.material.chip.Chip, seleccionado: Boolean) {
        if (seleccionado) {
            chip.setChipBackgroundColorResource(R.color.purple_500)
            chip.setTextColor(ContextCompat.getColor(this, R.color.white))
        } else {
            chip.setChipBackgroundColorResource(android.R.color.transparent)
            chip.setTextColor(ContextCompat.getColor(this, R.color.texto_principal))
        }
    }
    
    private fun aplicarFiltro() {
        tareasFiltradas.clear()
        
        val tareasOrdenadas = listaTareas.sortedWith(
            compareBy<Tarea> { it.completada }
                .thenByDescending { it.fechaCreacion }
        )
        
        var tareasFiltro = when (filtroActual) {
            FiltroTarea.TODAS -> tareasOrdenadas
            FiltroTarea.PENDIENTES -> tareasOrdenadas.filter { !it.completada }
            FiltroTarea.COMPLETADAS -> tareasOrdenadas.filter { it.completada }
        }
        
        if (textoBusqueda.isNotEmpty()) {
            tareasFiltro = tareasFiltro.filter { 
                it.nombre.contains(textoBusqueda, ignoreCase = true)
            }
        }
        
        tareasFiltradas.addAll(tareasFiltro)
        adaptador.notifyDataSetChanged()
        actualizarMensajeVacio()
    }

    private fun eliminarTareaFiltrada(posicion: Int) {
        val tareaAEliminar = tareasFiltradas[posicion]
        AlertDialog.Builder(this)
            .setTitle("Eliminar tarea")
            .setMessage("¿Estás seguro de que deseas eliminar esta tarea?")
            .setPositiveButton("Eliminar") { _, _ ->
                NotificacionHelper.cancelarNotificacion(this, tareaAEliminar.id)
                listaTareas.remove(tareaAEliminar)
                guardarTareas()
                aplicarFiltro()
                actualizarContador()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun editarTarea(
        tarea: Tarea,
        posicion: Int
    ) {
        val posicionReal = listaTareas.indexOfFirst { it.id == tarea.id }
        val intent = Intent(this, RegistrarTareaActivity::class.java).apply {
            putExtra("tarea", tarea)
            putExtra("posicion", posicionReal)
        }
        editarTareaLauncher.launch(intent)
    }
    
    private fun cambiarEstadoCompletadaFiltrada(posicion: Int, completada: Boolean) {
        val tarea = tareasFiltradas[posicion]
        val posicionReal = listaTareas.indexOfFirst { it.id == tarea.id }
        if (posicionReal != -1) {
            listaTareas[posicionReal].completada = completada
            guardarTareas()
            aplicarFiltro()
            actualizarContador()
        }
    }
    
    private fun actualizarContador() {
        val pendientes = listaTareas.count { !it.completada }
        val completadas = listaTareas.count { it.completada }
        val total = listaTareas.size
        val progreso = if (total > 0) (completadas * 100) / total else 0
        
        binding.textoContadorTareas.text = "$pendientes pendientes, $completadas completadas"
        binding.progressBarTareas.progress = progreso
    }
    
    private fun actualizarMensajeVacio() {
        if (tareasFiltradas.isEmpty()) {
            binding.textoSinTareas.visibility = android.view.View.VISIBLE
            binding.recyclerViewTareas.visibility = android.view.View.GONE
        } else {
            binding.textoSinTareas.visibility = android.view.View.GONE
            binding.recyclerViewTareas.visibility = android.view.View.VISIBLE
        }
    }
    
    private fun configurarBusqueda() {
        binding.searchViewTareas.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            
            override fun onQueryTextChange(newText: String?): Boolean {
                textoBusqueda = newText ?: ""
                aplicarFiltro()
                return true
            }
        })
    }
    
    private fun configurarSwipeParaEliminar() {
        val swipeHandler = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false
            
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val posicion = viewHolder.adapterPosition
                val tareaEliminada = tareasFiltradas[posicion]
                
                NotificacionHelper.cancelarNotificacion(this@MainActivity, tareaEliminada.id)
                listaTareas.remove(tareaEliminada)
                guardarTareas()
                aplicarFiltro()
                actualizarContador()
                
                Snackbar.make(
                    binding.root,
                    "Tarea eliminada",
                    Snackbar.LENGTH_LONG
                ).setAction("Deshacer") {
                    listaTareas.add(tareaEliminada)
                    NotificacionHelper.programarNotificacion(this@MainActivity, tareaEliminada)
                    guardarTareas()
                    aplicarFiltro()
                    actualizarContador()
                }.show()
            }
        }
        
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewTareas)
    }
    
    private fun solicitarPermisoNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    100
                )
            }
        }
    }
}