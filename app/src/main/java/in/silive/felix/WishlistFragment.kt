package `in`.silive.felix

import `in`.silive.felix.module.Movie
import `in`.silive.felix.recyclerview.*
import `in`.silive.felix.server.RetrofitAPI
import `in`.silive.felix.server.ServiceBuilder
import android.app.AlertDialog
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
import androidx.recyclerview.widget.GridLayoutManager
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



        movieRecyclerView = view.findViewById(R.id.recyclerView)

        progressBar.show()
        getWishlist()






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
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Confirm Delete").setMessage("Do you want to delete this movie from Wish List?").setPositiveButton("Yes", DialogInterface.OnClickListener { _, _ -> deleteMovie(movieId) }).setNegativeButton("Cancel", DialogInterface.OnClickListener { _, _ ->  })
        val alertDialog = builder.create()
        alertDialog.show()
    }


    fun deleteMovie(movieId : Int){
        val retrofitAPI = ServiceBuilder.buildService(RetrofitAPI::class.java)
        val call = retrofitAPI.removeFromWishList("Bearer " + (activity as HomePageActivity).token, movieId.toString())
        call.enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.code()==200){
                    Toast.makeText(requireContext(), response.body().toString(), Toast.LENGTH_SHORT).show()
                    getWishlist()
                }
                else if(response.code()==401){
                    (activity as HomePageActivity).signOut()
                }
                else if(response.code()==500){
                    Toast.makeText(requireContext(), "Internal Server Error\nPlease try again", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(requireContext(), response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(requireContext(), "Failed to delete from wish list", Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun getWishlist(){
        val retrofitAPI = ServiceBuilder.buildService(RetrofitAPI::class.java)
        val call = retrofitAPI.getWishList(
            "Bearer " + (activity as HomePageActivity).token
        )

        call.enqueue(object : Callback<List<Movie>> {
            override fun onResponse(
                call: Call<List<Movie>>,
                response: Response<List<Movie>>
            ) {

                if (response.code() == 200) {

                    val res = response.body() as List<Movie>

                    if(res.isEmpty()){
                        Toast.makeText(requireContext(), "Your Wish List is Empty", Toast.LENGTH_SHORT)
                            .show()
                    }
                    Log.d("Ashu",res.toString())

                    movieRecyclerView.layoutManager =
                        GridLayoutManager(requireContext(), 3)

                    movieRecyclerView.adapter = RecyclerHistoryAdapter(requireContext(), response.body() as List<Movie>, this@WishlistFragment)
                    progressBar.dismiss()

                }
                else if(response.code()==401){
                    (activity as HomePageActivity).signOut()
                }
                else if(response.code()==500){
                    Toast.makeText(requireContext(), "Internal Server Error\nPlease try again", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(requireContext(), response.code().toString(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Movie>>, t: Throwable) {
                Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
                progressBar.dismiss()
            }

        })
    }

}