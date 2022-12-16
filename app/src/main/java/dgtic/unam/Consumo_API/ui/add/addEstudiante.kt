package dgtic.unam.Consumo_API.ui.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import dgtic.unam.Consumo_API.data.Estudiante
import dgtic.unam.Consumo_API.data.Materia
import dgtic.unam.Consumo_API.data.VolleyAPI
import dgtic.unam.Consumo_API.databinding.FragmentAddEstudianteBinding

class addEstudiante : Fragment() {
    private var _binding: FragmentAddEstudianteBinding? = null
    private val binding get() = _binding!!

    private val newStudentFragmentViewModel: AddEstudianteViewModel by viewModels() {
        AddEstudianteViewModel.AddEstudianteMaker((VolleyAPI((requireContext()))))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEstudianteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
        binding.btnAdd.setOnClickListener {
            checkData()
        }
    }

    private fun initObservers() {
        newStudentFragmentViewModel.addSuccess.observe(requireActivity()) { isSuccess ->
            if (!isSuccess) {
                Snackbar.make(requireView(),"Error al insertar", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private fun checkData() {
        with(binding) {
            val account = tilCuenta.editText?.text.toString().trim()
            val name = tilNombre.editText?.text.toString().trim()
            val age = tilEdad.editText?.text.toString().trim()
            when {
                account.isBlank() -> {
                    tilCuenta.editText?.let { emptyField(it, "Sin numero de cuenta") }
                }
                name.isBlank() -> {
                    tilNombre.editText?.let { emptyField(it, "Sin nombre") }
                }
                age.isBlank() -> {
                    tilEdad.editText?.let { emptyField(it, "Sin edad") }
                }
                else -> {
                    val newStudent = Estudiante(
                        cuenta = account,
                        edad = age,
                        nombre = name,
                        materias = listOf<Materia>(
                            Materia(id = 1, nombre = "Quimica", creditos = 10),
                            Materia(id = 2, nombre = "Matematicas Avanzadas", creditos = 10)
                        )
                    )
                    newStudentFragmentViewModel.addStudent(newStudent)
                    limpiarInputs()
                    Snackbar.make(requireView(),name+" fue agregado", Snackbar.LENGTH_SHORT).show();
                }
            }
        }
    }

    private fun limpiarInputs() {
        with(binding) {
            tilNombre.editText?.text?.clear()
            tilCuenta.editText?.text?.clear()
            tilEdad.editText?.text?.clear()
        }
    }

    private fun emptyField(editText: EditText, message: String) {
        editText.error = message
        Snackbar.make(requireView(),"No hay datos de entrada", Snackbar.LENGTH_SHORT).show()
    }
}