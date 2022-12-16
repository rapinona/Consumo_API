package dgtic.unam.Consumo_API.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import dgtic.unam.Consumo_API.data.Estudiante
import dgtic.unam.Consumo_API.data.VolleyAPI
import dgtic.unam.Consumo_API.databinding.FragmentSearchEstudianteBinding
import dgtic.unam.Consumo_API.ui.view.ViewEstudiantesViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [searchEstudiante.newInstance] factory method to
 * create an instance of this fragment.
 */
class searchEstudiante : Fragment() {

    private var _binding : FragmentSearchEstudianteBinding? = null
    private val binding get() = _binding!!

    private val searchEstudianteViewModel: ViewEstudiantesViewModel by viewModels() {
        ViewEstudiantesViewModel.EstudiantesMaker((VolleyAPI((requireContext()))))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchEstudianteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initButton()
    }

    private fun initButton() {
        with(binding) {
            tilSearch.setEndIconOnClickListener {
                val id = tilSearch.editText?.text.toString().trim()
                if (id.isBlank())
                    Snackbar.make(requireView(),"Error con el ID", Snackbar.LENGTH_SHORT).show();
                else
                    searchEstudianteViewModel.getAStudent(id)
            }
        }
    }

    private fun initObservers() {
        searchEstudianteViewModel.student.observe(requireActivity()) { student ->
            setUI(student)
        }
        searchEstudianteViewModel.searchFailure.observe(requireActivity()) {
            if (it) {
                Snackbar.make(requireView(),"Error con el ID", Snackbar.LENGTH_SHORT).show();
                binding.tilSearch.error = "Error con el ID"
                binding.tilSearch.editText?.text?.clear()
            }
        }
    }

    private fun setUI(student: Estudiante?) {
        if (student != null) {
            with(binding) {
                tvAccount.text = student.cuenta
                tvName.text = student.nombre
                tvAge.text = student.edad + " Años"
            }
        }
    }


}