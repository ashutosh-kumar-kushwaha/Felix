package `in`.silive.felix

import `in`.silive.felix.module.CategoryResponse
import `in`.silive.felix.recyclerview.*
import `in`.silive.felix.server.RetrofitAPI
import `in`.silive.felix.server.ServiceBuilder
import android.app.AlertDialog
import android.app.Service
import android.content.DialogInterface
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
import androidx.annotation.RestrictTo
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class WishlistFragment : Fragment() , HistoryClickListener{

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

        val view =  inflater.inflate(R.layout.fragment_wishlist, container, false)

        progressBar = getDialogueProgressBar(view).create()
        progressBar.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressBar.setCanceledOnTouchOutside(false)

        progressBar.show()

        val retrofitAPI = ServiceBuilder.buildService(RetrofitAPI::class.java)
        val call = retrofitAPI.getWishList(
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
                        Toast.makeText(view.context, "Your Wish List is Empty", Toast.LENGTH_SHORT)
                            .show()
                    }

                    Log.d("Ashu",res.toString())

                    movieRecyclerView = view.findViewById(R.id.recyclerView)
                    movieRecyclerView.layoutManager =
                        GridLayoutManager(view.context, 3)

                    movieRecyclerView.adapter = RecyclerHistoryAdapter(view.context, response.body() as List<CategoryResponse>, this@WishlistFragment)
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
//        movieRecyclerView.adapter = RecyclerMoviesAdapter(view.context, listOf(R.drawable.daredevil, R.drawable.daredevil, R.drawable.daredevil, R.drawable.daredevil, R.drawable.daredevil, R.drawable.daredevil, R.drawable.daredevil,R.drawable.daredevil,R.drawable.daredevil,R.drawable.daredevil,R.drawable.daredevil,R.drawable.daredevil,R.drawable.daredevil,R.drawable.daredevil,R.drawable.daredevil,R.drawable.daredevil,R.drawable.daredevil,R.drawable.daredevil,R.drawable.daredevil,R.drawable.daredevil,R.drawable.daredevil))


        return view
    }

    override fun onItemClick(position: Int, movieId : Int) {
        (activity as HomePageActivity).movieId = movieId
        (activity as HomePageActivity).mediaStreamingFrag()
    }

    override fun onDeleteClick(position: Int, movieId: Int) {
        Toast.makeText(requireContext(), "Delete", Toast.LENGTH_SHORT).show()
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Confirm Delete").setMessage("Do you want to delete this movie from Wish List?").setPositiveButton("Yes", DialogInterface.OnClickListener { _, _ -> deleteMovie(movieId) }).setNegativeButton("Cancel", DialogInterface.OnClickListener { _, _ ->  })
    }


    fun deleteMovie(movieId : Int){
        val retrofitAPI = ServiceBuilder.buildService(RetrofitAPI::class.java)

    }

}