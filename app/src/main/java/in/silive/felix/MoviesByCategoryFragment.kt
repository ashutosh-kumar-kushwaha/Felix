package `in`.silive.felix

import `in`.silive.felix.module.Movie
import `in`.silive.felix.recyclerview.ItemClickListener
import `in`.silive.felix.recyclerview.RecyclerHistoryAdapter
import `in`.silive.felix.recyclerview.RecyclerMoviesByCategoryAdapter
import `in`.silive.felix.server.RetrofitAPI
import `in`.silive.felix.server.ServiceBuilder
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesByCategoryFragment : Fragment(), ItemClickListener {

    lateinit var movieRecyclerView: RecyclerView
    lateinit var progressBar: AlertDialog
    var builder: AlertDialog.Builder? = null
    lateinit var nothingImgVw: AppCompatImageView
    lateinit var nothingTxtVw: AppCompatTextView


    fun getDialogueProgressBar(view: View): AlertDialog.Builder {
        if (builder == null) {
            builder = AlertDialog.Builder(view.context)
            val progressBar = ProgressBar(view.context)
            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            progressBar.layoutParams = lp
            builder!!.setView(progressBar)
        }
        return builder as AlertDialog.Builder
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movies_by_category, container, false)

        progressBar = getDialogueProgressBar(view).create()
        progressBar.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressBar.setCanceledOnTouchOutside(false)

        nothingImgVw = view.findViewById(R.id.nothingImgVw)
        nothingTxtVw = view.findViewById(R.id.nothingTxtVw)


        movieRecyclerView = view.findViewById(R.id.recyclerView)



        getMovies()

        return view
    }

    fun getMovies() {
        progressBar.show()
        val retrofitAPI = ServiceBuilder.buildService(RetrofitAPI::class.java)
        val call = retrofitAPI.getMovieByCategory(
            "Bearer " + (activity as HomePageActivity).token,
            (activity as HomePageActivity).categoryName
        )

        call.enqueue(object : Callback<List<Movie>> {
            override fun onResponse(
                call: Call<List<Movie>>,
                response: Response<List<Movie>>
            ) {
                if (context != null) {

                    if (response.code() == 200) {

                        val res = response.body() as List<Movie>

                        if (res.isEmpty()) {
                            nothingImgVw.visibility = View.VISIBLE
                            nothingTxtVw.visibility = View.VISIBLE
                        } else {
                            nothingImgVw.visibility = View.GONE
                            nothingTxtVw.visibility = View.GONE
                            movieRecyclerView.layoutManager =
                                GridLayoutManager(requireContext(), 3)

                            movieRecyclerView.adapter = RecyclerMoviesByCategoryAdapter(
                                requireContext(),
                                response.body() as List<Movie>,
                                this@MoviesByCategoryFragment
                            )

                        }

                        progressBar.dismiss()

                    } else if (response.code() == 401) {
                        (activity as HomePageActivity).signOut()
                    } else if (response.code() == 500) {
                        Toast.makeText(
                            requireContext(),
                            "Internal Server Error\nPlease try again",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            response.code().toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<List<Movie>>, t: Throwable) {
                if (context != null) {
                    Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
                    progressBar.dismiss()
                }
            }
        })
    }

    override fun onItemClick(position: Int, movieId: Int) {
        (activity as HomePageActivity).movieId = movieId
        (activity as HomePageActivity).mediaStreamingFrag()
    }
}