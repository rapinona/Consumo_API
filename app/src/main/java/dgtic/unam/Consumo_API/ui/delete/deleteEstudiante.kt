package dgtic.unam.Consumo_API.ui.delete

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dgtic.unam.Consumo_API.data.VolleyAPI
import dgtic.unam.Consumo_API.databinding.FragmentDeleteEstudianteBinding
import dgtic.unam.Consumo_API.ui.view.EstudianteAdapter
import dgtic.unam.Consumo_API.ui.view.ViewEstudiantesViewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class deleteEstudiante : Fragment() {

    private var _binding: FragmentDeleteEstudianteBinding? = null
    private val binding get() = _binding!!

    private val deleteStudentFragmentViewModel: ViewEstudiantesViewModel by viewModels() {
        ViewEstudiantesViewModel.EstudiantesMaker((VolleyAPI((requireContext()))))
    }

    private lateinit var estudianteAdapter: EstudianteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDeleteEstudianteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
        initButton()
        initRecyclerView()
    }

    private fun initButton() {
        with(binding) {
            btnDelete.setOnClickListener(){
                val id = searchText.text.toString().trim()
                if (id.isBlank())
                    Toast.makeText(
                        requireContext(),
                        "Error al borrar",
                        Toast.LENGTH_SHORT
                    ).show()
                else {
                    deleteStudentFragmentViewModel.deleteAStudent(id)
                    Snackbar.make(
                        requireView(),
                        "Estudiante borrado con exito",
                        Snackbar.LENGTH_SHORT
                    ).show();
                }
            }
        }
    }

    private fun initObservers() {
        deleteStudentFragmentViewModel.allStudentsDeleted.observe(requireActivity()) { students ->
            if (students != null) {
                estudianteAdapter.studentsList(students)
            }
        }
        deleteStudentFragmentViewModel.deleteFailure.observe(requireActivity()) {
            if (it) {
                Snackbar.make(
                    requireView(),
                    "Error en el id",
                    Snackbar.LENGTH_SHORT
                ).show()
                binding.searchText.text.toString().trim()
                binding.searchText.error = "Error en el id"
                binding.searchText.text?.clear()
            }
        }
    }

    private fun initRecyclerView() {
        estudianteAdapter = EstudianteAdapter(requireContext())
        binding.rvStudents.visibility = View.VISIBLE
        binding.rvStudents.layoutManager = LinearLayoutManager(requireContext())
        binding.rvStudents.adapter = estudianteAdapter
    }
}