package dgtic.unam.Consumo_API.ui.view

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import dgtic.unam.Consumo_API.data.Estudiante
import dgtic.unam.Consumo_API.data.*
import dgtic.unam.Consumo_API.data.Utils.DELETE
import dgtic.unam.Consumo_API.data.Utils.GET_ID
import org.json.JSONArray

class ViewEstudiantesViewModel(private val volleyAPI : VolleyAPI) : ViewModel() {

    private val _todosEstudiantesList = MutableLiveData<Estudiantes>()
    val allStudentsList: LiveData<Estudiantes>
        get() = _todosEstudiantesList

    private val _failureAtSearch = MutableLiveData<Boolean>()
    val searchFailure: LiveData<Boolean>
        get() = _failureAtSearch

    private val _esudiantesBorrados = MutableLiveData<Estudiantes>()
    val allStudentsDeleted: LiveData<Estudiantes>
        get() = _esudiantesBorrados

    private val _failureAtDelete = MutableLiveData<Boolean>()
    val deleteFailure: LiveData<Boolean>
        get() = _failureAtDelete

    private val _student = MutableLiveData<Estudiante>()
    val student: LiveData<Estudiante>
        get() = _student



    fun getAllStudents() {
        var estudiantes = Estudiantes()

        val jsonRequest = object : JsonArrayRequest(
            Utils.GET,
            Response.Listener { response ->
                (0 until response.length()).forEach {
                    val estudiante = response.getJSONObject(it)
                    val cuenta = estudiante.get("cuenta")
                    val nombre = estudiante.get("nombre")
                    val edad = estudiante.get("edad")
                    val materias = getMaterias(estudiante.getJSONArray("materias"))
                    if (materias.isNotEmpty()) {
                        estudiantes.add(
                            Estudiante(
                                cuenta = "$cuenta",
                                nombre = "$nombre",
                                edad = "$edad",
                                materias = materias
                            )
                        )
                    }
                }
                if (estudiantes.isNotEmpty())
                    _todosEstudiantesList.postValue(estudiantes)
            },
            Response.ErrorListener {
                it.printStackTrace()
                Log.e("ERROR:", "${it.message}")
                _failureAtSearch.postValue(true)
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val header = HashMap<String, String>()
                header["User-Agent"] = "Mozilla/5.0 (Windows NT 6.1)"
                return header
            }
        }

        volleyAPI.add(jsonRequest)
    }

    private fun getMaterias(jsonArray: JSONArray): List<Materia> {
        var materias = mutableListOf<Materia>()
        (0 until jsonArray.length()).forEach {
            val materia = jsonArray.getJSONObject(it)
            val id = materia.get("id").toString().toInt()
            val nombre = materia.get("nombre").toString()
            val creditos = materia.get("creditos").toString().toInt()
            materias.add(Materia(id = id, nombre = nombre, creditos = creditos))
        }
        return materias
    }

    class EstudiantesMaker(val volleyAPI: VolleyAPI): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(ViewEstudiantesViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return ViewEstudiantesViewModel(volleyAPI) as T
            }
            throw  java.lang.IllegalArgumentException("Clase ViewModel desconocida")
        }
    }

    fun deleteAStudent(id: String) {
        var estudiantes = Estudiantes()
        val jsonRequest = object : JsonArrayRequest(
            DELETE+id,
            Response.Listener { response ->
                (0 until response.length()).forEach {
                    val estudiante = response.getJSONObject(it)
                    val cuenta = estudiante.get("cuenta")
                    val nombre = estudiante.get("nombre")
                    val edad = estudiante.get("edad")
                    val materias = getMaterias(estudiante.getJSONArray("materias"))
                    if (materias.isNotEmpty()) {
                        val estudiante = Estudiante(
                            cuenta = "$cuenta",
                            nombre = "$nombre",
                            edad = "$edad",
                            materias = materias
                        )

                    }
                }
                if (estudiantes.isNotEmpty())
                    _esudiantesBorrados.postValue(estudiantes)
            },
            Response.ErrorListener { responseError ->
                responseError.printStackTrace()
                Log.e("ERROR:", "${responseError.message}")
                _failureAtDelete.postValue(true)
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val header = HashMap<String, String>()
                header["User-Agent"] = "Mozilla/5.0 (Windows NT 6.1)"
                return header
            }
            override fun getMethod(): Int {
                return Method.DELETE
            }
        }

        volleyAPI.add(jsonRequest)
    }

    fun getAStudent(id: String){
        val jsonRequest = object : JsonObjectRequest(
            Method.GET,
            GET_ID+id,
            null,
            Response.Listener { response ->
                val cuenta = response.get("cuenta")
                val nombre = response.get("nombre")
                val edad = response.get("edad")
                val materias = getMaterias(response.getJSONArray("materias"))
                if (materias.isNotEmpty()) {
                    val estudiante = Estudiante(
                        cuenta = "$cuenta",
                        nombre = "$nombre",
                        edad = "$edad",
                        materias = materias
                    )
                    _student.postValue(estudiante)
                }
            },
            Response.ErrorListener {
                it.printStackTrace()
                Log.e("ERROR:", "${it.message}")
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "Mozilla/5.0 (Windows NT 6.1)"
                return headers
            }
        }
        volleyAPI.add(jsonRequest)
    }

}