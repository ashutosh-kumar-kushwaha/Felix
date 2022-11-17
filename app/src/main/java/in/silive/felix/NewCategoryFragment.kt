package `in`.silive.felix

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.textfield.TextInputEditText

class NewCategoryFragment : Fragment() {

    lateinit var categoryETxt : TextInputEditText
    lateinit var doneBtn : AppCompatButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_new_category, container, false)

        categoryETxt = view.findViewById(R.id.categoryETxt)
        doneBtn.setOnClickListener{
            createNewCategory()
        }


        return view
    }

    private fun createNewCategory() {

    }
}