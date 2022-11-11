package `in`.silive.felix

import `in`.silive.felix.module.CategoryResponse
import `in`.silive.felix.recyclerview.ChildClickListener
import `in`.silive.felix.recyclerview.RecyclerMoviesAdapter
import `in`.silive.felix.server.RetrofitAPI
import `in`.silive.felix.server.ServiceBuilder
import android.app.AlertDialog
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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class WatchHistoryFragment : Fragment(), ChildClickListener{


    lateinit var movieRecyclerView : RecyclerView
    lateinit var progressBar: AlertDialog
    var builder: AlertDialog.Builder? = null


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

        progressBar = getDialogueProgressBar(view).create()
        progressBar.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressBar.setCanceledOnTouchOutside(false)

        progressBar.show()

        val retrofitAPI = ServiceBuilder.buildService(RetrofitAPI::class.java)
        val call = retrofitAPI.getHistory(
            "Bearer " + (activity as HomePageActivity).token
        )

        call.enqueue(object : Callback<List<CategoryResponse>> {
            override fun onResponse(
                call: Call<List<CategoryResponse>>,
                response: Response<List<CategoryResponse>>
            ) {

                if (response.code() == 200) {

                    val res = response.body() as List<CategoryResponse>

                    if(res.isEmpty()){
                        Toast.makeText(view.context, "Your History is Empty", Toast.LENGTH_SHORT)
                            .show()
                    }

                    movieRecyclerView = view.findViewById(R.id.recyclerView)
                    movieRecyclerView.layoutManager =
                        GridLayoutManager(view.context, 3)


                    movieRecyclerView.adapter = RecyclerMoviesAdapter(view.context, response.body() as List<CategoryResponse>, this@WatchHistoryFragment)
                    progressBar.dismiss()

                } else {
                    Toast.makeText(view.context, response.code().toString(), Toast.LENGTH_SHORT)
                        .show()
                    progressBar.dismiss()
                }
            }

            override fun onFailure(call: Call<List<CategoryResponse>>, t: Throwable) {
                Toast.makeText(view.context, "Failed", Toast.LENGTH_SHORT).show()
                progressBar.dismiss()
            }

        })


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
}