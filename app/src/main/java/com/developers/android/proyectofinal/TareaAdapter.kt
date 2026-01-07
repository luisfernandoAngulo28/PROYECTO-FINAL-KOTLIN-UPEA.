package com.developers.android.proyectofinal

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.developers.android.proyectofinal.databinding.ItemTareaBinding

class TareaAdapter(
    private val tareas: MutableList<Tarea>,
    private val onEditarClick: (Tarea, Int) -> Unit,
    private val onEliminarClick: (Int) -> Unit,
    private val onCompletadaChanged: (Int, Boolean) -> Unit
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
                
                // Configurar CheckBox
                checkBoxCompletada.setOnCheckedChangeListener(null)
                checkBoxCompletada.isChecked = tarea.completada
                
                // Aplicar estilos según estado completado
                aplicarEstiloCompletada(tarea.completada)
                
                checkBoxCompletada.setOnCheckedChangeListener { _, isChecked ->
                    aplicarEstiloCompletada(isChecked)
                    onCompletadaChanged(posicion, isChecked)
                }

                //Eventos de eliminar y de editar
                botonEditar.setOnClickListener { onEditarClick(tarea, posicion) }
                botonEliminar.setOnClickListener { onEliminarClick(posicion) }
            }
        }
        
        private fun aplicarEstiloCompletada(completada: Boolean) {
            binding.apply {
                if (completada) {
                    textoNombreTarea.paintFlags = textoNombreTarea.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    textoNombreTarea.alpha = 0.6f
                    textoNivelPrioridad.alpha = 0.6f
                    textoFechaCreacion.alpha = 0.6f
                    textoFechaRecordatorio.alpha = 0.6f
                } else {
                    textoNombreTarea.paintFlags = textoNombreTarea.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    textoNombreTarea.alpha = 1.0f
                    textoNivelPrioridad.alpha = 1.0f
                    textoFechaCreacion.alpha = 1.0f
                    textoFechaRecordatorio.alpha = 1.0f
                }
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