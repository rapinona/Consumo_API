package dgtic.unam.Consumo_API.ui.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dgtic.unam.Consumo_API.data.Estudiantes
import dgtic.unam.Consumo_API.databinding.EstudianteBinding

class EstudianteAdapter(
    context: Context
) : RecyclerView.Adapter<EstudianteAdapter.ViewHolder>() {

    private val layoutInflater = LayoutInflater.from(context)
    private var listOfStudents: Estudiantes = Estudiantes()

    inner class ViewHolder(view: EstudianteBinding) : RecyclerView.ViewHolder(view.root) {
        val tvNombre = view.tvName
        val tvCuenta = view.tvAccount
        val tvEdad = view.tvAge
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = EstudianteBinding.inflate(layoutInflater)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val estudiante = listOfStudents[position]
        holder.tvCuenta.text = estudiante.cuenta
        holder.tvNombre.text = estudiante.nombre
        holder.tvEdad.text = estudiante.edad + " años"
    }

    override fun getItemCount(): Int = listOfStudents.size

    fun studentsList(list: Estudiantes) {
        Log.d("TEST", "$list")
        listOfStudents = list
        notifyItemRangeChanged(listOfStudents.indexOf(listOfStudents.first()), itemCount)
    }

}