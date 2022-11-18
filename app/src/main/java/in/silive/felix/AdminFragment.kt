package `in`.silive.felix

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton

class AdminFragment : Fragment() {

    lateinit var newMovieBtn : AppCompatButton
    lateinit var deleteMovieBtn : AppCompatButton
    lateinit var newCategoryBtn : AppCompatButton
    lateinit var editMovieBtn : AppCompatButton
    lateinit var newAdminBtn : AppCompatButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_admin, container, false)

        newMovieBtn = view.findViewById(R.id.newMovieBtn)
        newMovieBtn.setOnClickListener{
            (activity as HomePageActivity).newMovieFrag()
        }

        deleteMovieBtn = view.findViewById(R.id.deleteMovieBtn)
        deleteMovieBtn.setOnClickListener{
            (activity as HomePageActivity).deleteMovieFrag()
        }

        newCategoryBtn = view.findViewById(R.id.newCategoryBtn)
        newCategoryBtn.setOnClickListener{
            (activity as HomePageActivity).newCategoryFrag()
        }

        editMovieBtn = view.findViewById(R.id.editMovieBtn)
        editMovieBtn.setOnClickListener{
            (activity as HomePageActivity).searchMovieForEditFrag()
        }

        newAdminBtn = view.findViewById(R.id.newAdminBtn)
        newAdminBtn.setOnClickListener{
            (activity as HomePageActivity).newAdminFrag()
        }



        return view
    }

}
