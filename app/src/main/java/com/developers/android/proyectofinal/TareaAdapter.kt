package com.developers.android.proyectofinal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.developers.android.proyectofinal.databinding.ItemTareaBinding

class TareaAdapter(
    private val tareas: MutableList<Tarea>,
    private val onEditarClick: (Tarea, Int) -> Unit,
    private val onEliminarClick: (Int) -> Unit,
) : RecyclerView.Adapter<TareaAdapter.TareaViewHolder>() {
    inner class TareaViewHolder(private val binding: ItemTareaBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun vincular(tarea: Tarea, posicion: Int) {
            binding.apply {
                textoNombreTarea.text = tarea.nombre
                textoNivelPrioridad.text = tarea.nivel.texto
                textoFechaCreacion.text = "Creada: ${tarea.fechaCreacion}"
                textoFechaRecordatorio.text = "Recordatorio: ${tarea.fechaRecordatorio}"
                indicadorPrioridad.setBackgroundColor(
                    ContextCompat.getColor(root.context, tarea.nivel.colorRes)
                )

                //Enventos de eliminar y de editar
                botonEditar.setOnClickListener { onEditarClick(tarea, posicion) }
                botonEliminar.setOnClickListener { onEliminarClick(posicion) }
            }
        }
    }


    override fun onBindViewHolder(holder: TareaViewHolder, position: Int) {
        holder.vincular(tareas[position], position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        TareaViewHolder(
            ItemTareaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun getItemCount(): Int {
        return tareas.size
    }
}