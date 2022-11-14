package `in`.silive.felix

import `in`.silive.felix.module.Movie
import `in`.silive.felix.recyclerview.HistoryClickListener
import `in`.silive.felix.recyclerview.RecyclerHistoryAdapter
import `in`.silive.felix.server.RetrofitAPI
import `in`.silive.felix.server.ServiceBuilder
import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class WatchHistoryFragment : Fragment(), HistoryClickListener{


    lateinit var movieRecyclerView : RecyclerView
    lateinit var progressBar: AlertDialog
    var builder: AlertDialog.Builder? = null
    lateinit var nothingImgVw : AppCompatImageView
    lateinit var nothingTxtVw : AppCompatTextView


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

        val view = inflater.inflate(R.layout.fragment_watch_history, container, false)


        nothingImgVw = view.findViewById(R.id.nothingImgVw)
        nothingTxtVw = view.findViewById(R.id.nothingTxtVw)

        progressBar = getDialogueProgressBar(view).create()
        progressBar.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressBar.setCanceledOnTouchOutside(false)

        progressBar.show()

        movieRecyclerView = view.findViewById(R.id.recyclerView)

        getHistory()


//        movieRecyclerView = view.findViewById(R.id.recyclerView)
//        movieRecyclerView.layoutManager = GridLayoutManager(view.context, 3)
//
//        movieRecyclerView.adapter = RecyclerMoviesAdapter(view.context, listOf(R.drawable.money_heist,R.drawable.money_heist,R.drawable.money_heist,R.drawable.money_heist,R.drawable.money_heist,R.drawable.money_heist,R.drawable.money_heist,R.drawable.money_heist,R.drawable.money_heist,R.drawable.money_heist,R.drawable.money_heist,R.drawable.money_heist,R.drawable.money_heist,R.drawable.money_heist,R.drawable.money_heist,R.drawable.money_heist,R.drawable.money_heist,R.drawable.money_heist,R.drawable.money_heist,R.drawable.money_heist,R.drawable.money_heist,R.drawable.money_heist,R.drawable.money_heist,R.drawable.money_heist,R.drawable.money_heist,R.drawable.money_heist,R.drawable.money_heist,R.drawable.money_heist,R.drawable.money_heist,R.drawable.money_heist,R.drawable.money_heist,R.drawable.money_heist,R.drawable.money_heist,R.drawable.money_heist,R.drawable.money_heist,R.drawable.money_heist,R.drawable.money_heist,R.drawable.money_heist,R.drawable.money_heist))


        return view
    }

    override fun onItemClick(position: Int, movieId: Int) {
        (activity as HomePageActivity).movieId = movieId
        (activity as HomePageActivity).mediaStreamingFrag()
    }

    override fun onDeleteClick(position: Int, movieId: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Confirm Delete").setMessage("Do you want to delete this movie from Wish List?").setPositiveButton("Yes", DialogInterface.OnClickListener { _, _ -> deleteMovie(movieId) }).setNegativeButton("Cancel", DialogInterface.OnClickListener { _, _ ->  })
        val alertDialog = builder.create()
        alertDialog.show()
    }

    fun deleteMovie(movieId: Int){

        val retrofitAPI = ServiceBuilder.buildService(RetrofitAPI::class.java)
        val call = retrofitAPI.removeFromHistory("Bearer " + (activity as HomePageActivity).token, movieId.toString())
        call.enqueue(object : Callback<String>{

            override fun onResponse(call: Call<String>, response: Response<String>) {

                if((activity as HomePageActivity).currentFragment == "History") {

                    if (response.code() == 200) {
                        Toast.makeText(
                            requireContext(),
                            response.body().toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                        getHistory()
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

            override fun onFailure(call: Call<String>, t: Throwable) {
                if((activity as HomePageActivity).currentFragment == "History") {
                    Toast.makeText(requireContext(), "Failed to delete history", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        })

    }

    fun getHistory(){
        val retrofitAPI = ServiceBuilder.buildService(RetrofitAPI::class.java)
        val call = retrofitAPI.getHistory(
            "Bearer " + (activity as HomePageActivity).token
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
                            Toast.makeText(
                                requireContext(),
                                "Your History is Empty",
                                Toast.LENGTH_SHORT
                            )
                                .show()

                            nothingImgVw.visibility = View.VISIBLE
                            nothingTxtVw.visibility = View.VISIBLE

                        }


                        movieRecyclerView.layoutManager =
                            GridLayoutManager(requireContext(), 3)


                        movieRecyclerView.adapter = RecyclerHistoryAdapter(
                            requireContext(),
                            response.body() as List<Movie>,
                            this@WatchHistoryFragment
                        )


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
                        )
                            .show()
                    }
                }
                progressBar.dismiss()
            }

            override fun onFailure(call: Call<List<Movie>>, t: Throwable) {
                if(context != null) {
                    Toast.makeText(requireContext(), "Failed to load history", Toast.LENGTH_SHORT)
                        .show()
                }
                progressBar.dismiss()
            }

        })

    }
}