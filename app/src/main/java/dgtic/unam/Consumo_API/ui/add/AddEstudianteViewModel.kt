package dgtic.unam.Consumo_API.ui.add

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import dgtic.unam.Consumo_API.data.Estudiante
import dgtic.unam.Consumo_API.data.Utils
import dgtic.unam.Consumo_API.data.VolleyAPI
import org.json.JSONArray
import org.json.JSONObject

class AddEstudianteViewModel(
    private val volleyAPI: VolleyAPI
) : ViewModel() {

    private var _addSuccess = MutableLiveData<Boolean>()
    val addSuccess: LiveData<Boolean>
        get() = _addSuccess

    fun addStudent(student: Estudiante) {
        val jsonRequest = object : JsonArrayRequest(
            Utils.ADD,
            Response.Listener {
                _addSuccess.postValue(true)
            },
            Response.ErrorListener {
                _addSuccess.postValue(false)
                it.printStackTrace()
                Log.e("ERROR:", "${it.message}")
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val header = HashMap<String, String>()
                header["User-Agent"] = "Mozilla/5.0 (Windows NT 6.1)"
                return header
            }

            override fun getBody(): ByteArray {
                val estudiante = JSONObject()
                estudiante.put("cuenta", student.cuenta)
                estudiante.put("nombre", student.nombre)
                estudiante.put("edad", student.edad)

                val materias = JSONArray()
                student.materias.forEach { materia ->
                    val itemMaterias = JSONObject()
                    itemMaterias.put("id", materia.id)
                    itemMaterias.put("nombre", materia.nombre)
                    itemMaterias.put("creditos", materia.creditos)
                    materias.put(itemMaterias)
                }
                estudiante.put("materias", materias)

                return estudiante.toString().toByteArray(charset = Charsets.UTF_8)
            }

            override fun getMethod(): Int {
                return Method.POST
            }
        }

        volleyAPI.add(jsonRequest)
    }

    class AddEstudianteMaker(val volleyAPI: VolleyAPI) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AddEstudianteViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AddEstudianteViewModel(volleyAPI) as T
            }
            throw  java.lang.IllegalArgumentException("Error")
        }
    }
}