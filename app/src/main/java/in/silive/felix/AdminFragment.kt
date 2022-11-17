package `in`.silive.felix

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton

class AdminFragment : Fragment() {

    lateinit var newMovieBtn : AppCompatButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_admin, container, false)

        newMovieBtn = view.findViewById(R.id.newMovieBtn)
        newMovieBtn.setOnClickListener{
            (activity as HomePageActivity).newMovieFrag()
        }

        return view
    }

}
