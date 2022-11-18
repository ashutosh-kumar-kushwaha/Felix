package `in`.silive.felix

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.textfield.TextInputEditText

class NewAdminFragment : Fragment() {

    lateinit var emailETxt : TextInputEditText
    lateinit var doneBtn : AppCompatButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_new_admin, container, false)
        emailETxt = view.findViewById(R.id.emailETxt)
        doneBtn = view.findViewById(R.id.doneBtn)
        doneBtn.setOnClickListener{
            addAdmin()
        }

        return view
    }

    fun addAdmin(){

    }
}