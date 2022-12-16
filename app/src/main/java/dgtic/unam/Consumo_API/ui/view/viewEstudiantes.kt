package dgtic.unam.Consumo_API.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dgtic.unam.Consumo_API.data.Estudiantes
import dgtic.unam.Consumo_API.data.VolleyAPI
import dgtic.unam.Consumo_API.databinding.FragmentViewEstudiantesBinding

class viewEstudiantes : Fragment() {

    private var _binding : FragmentViewEstudiantesBinding? = null
    private val binding get() = _binding!!

    private lateinit var volleyAPI: VolleyAPI

    private lateinit var studentsAdapter : EstudianteAdapter

    private val getEstudiantesFragmentViewModel: ViewEstudiantesViewModel by viewModels() {
        ViewEstudiantesViewModel.EstudiantesMaker((VolleyAPI((requireContext()))))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewEstudiantesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        volleyAPI = VolleyAPI(requireContext())

        studentsAdapter = EstudianteAdapter(requireContext())
        binding.rvStudents.visibility = View.VISIBLE
        binding.rvStudents.layoutManager = LinearLayoutManager(requireContext())
        binding.rvStudents.adapter = studentsAdapter

        getStudents()
        initObservers()
    }

    private fun getStudents() {
        getEstudiantesFragmentViewModel.getAllStudents()
    }

    private fun initObservers() {
        getEstudiantesFragmentViewModel.allStudentsList.observe(requireActivity()) { studentList ->
            setRecyclerView(studentList)
        }
    }

    private fun setRecyclerView(studentList: Estudiantes?) {
        if (studentList != null) {
            studentsAdapter.studentsList(studentList)
        }
    }
}