package `in`.silive.felix

import `in`.silive.felix.module.*
import `in`.silive.felix.server.RetrofitAPI
import `in`.silive.felix.server.ServiceBuilder
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import coil.load
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class EditMovieFragment : Fragment() {

    lateinit var coverImage: AppCompatImageView
    lateinit var categoriesDropDown: Spinner
    lateinit var movieNameETxt: TextInputEditText
    lateinit var descriptionETxt: TextInputEditText
    lateinit var castETxt: TextInputEditText
    lateinit var movieYearETxt: TextInputEditText
    lateinit var lengthETxt: TextInputEditText
    lateinit var genreETxt: TextInputEditText
    lateinit var uploadBtn: AppCompatButton
    lateinit var restrictionDropdown: Spinner
    lateinit var catList : List<String>
    lateinit var token : String

    lateinit var progressBar: AlertDialog
    var builder: AlertDialog.Builder? = null

    val restrictions : List<String> = listOf("U/A 16+", "15", "U/A 13+", "12A", "12", "PG")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_movie, container, false)

        progressBar = getDialogueProgressBar(view).create()
        progressBar.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressBar.setCanceledOnTouchOutside(false)

        token = (activity as HomePageActivity).token

        movieNameETxt = view.findViewById(R.id.movieNameETxt)
        descriptionETxt = view.findViewById(R.id.descriptionETxt)
        castETxt = view.findViewById(R.id.castETxt)
        movieYearETxt = view.findViewById(R.id.movieYearETxt)
        lengthETxt = view.findViewById(R.id.lengthETxt)
        genreETxt = view.findViewById(R.id.genreETxt)
        categoriesDropDown = view.findViewById(R.id.categoriesDropdown)
        restrictionDropdown = view.findViewById(R.id.restrictionDropdown)

        val dropDownAdapter = ArrayAdapter(
            requireContext(),
            R.layout.restriction_dropdown_item,
            resources.getStringArray(R.array.restrictions)
        )

        restrictionDropdown.setAdapter(dropDownAdapter)

        coverImage = view.findViewById(R.id.coverImage)
        uploadBtn = view.findViewById(R.id.uploadBtn)

        uploadBtn.setOnClickListener{
            var checkMovieName = false
            var checkDescription = false
            var checkCast = false
            var checkYear = false
            var checkLength = false
            var checkGenre = false

            if(movieNameETxt.text.toString().isEmpty()){
                movieNameETxt.error = "Movie Name can't be empty"
            }
            else{
                checkMovieName = true
            }

            if(descriptionETxt.text.toString().isEmpty()){
                descriptionETxt.error = "Description can't be empty"
            }
            else{
                checkDescription = true
            }

            if(castETxt.text.toString().isEmpty()){
                castETxt.error = "Cast can't be empty"
            }
            else{
                checkCast = true
            }

            if(movieYearETxt.text.toString().isEmpty()){
                movieYearETxt.error = "Movie Year can't be empty"
            }
            else{
                checkYear = true
            }

            if(lengthETxt.text.toString().isEmpty()){
                lengthETxt.error = "Length can't be empty"
            }
            else{
                checkLength = true
            }

            if(genreETxt.text.toString().isEmpty()){
                genreETxt.error = "Genre can't be empty"
            }
            else{
                checkGenre = true
            }

            if(checkMovieName && checkDescription && checkCast && checkYear && checkLength && checkGenre){
                editMovie()
            }
        }


        getCategories()

        return view
    }

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

    fun loadMovieDetails(){
            val retrofitAPI = ServiceBuilder.buildService(RetrofitAPI::class.java)
            val call = retrofitAPI.getMediaStreamingDetails(
                "Bearer " + (activity as HomePageActivity).token,
                (activity as HomePageActivity).movieId.toString()
            )

            call.enqueue(object : Callback<MediaStreamingResponse> {
                override fun onResponse(
                    call: Call<MediaStreamingResponse>,
                    response: Response<MediaStreamingResponse>
                ) {
                    if(context != null){
                        val details = response.body()
                        movieNameETxt.setText(details?.movieName)
                        coverImage.load(details?.coverImageServingPath.toString())
                        restrictionDropdown.setSelection(restrictions.indexOf(details?.movieRestriction))
                        descriptionETxt.setText(details?.movieDescription)
                        castETxt.setText(details?.movieCast)
                        movieYearETxt.setText(details?.movieYear.toString())
                        lengthETxt.setText(details?.movieLength)
                        genreETxt.setText(details?.genres?.let { getGenres(it) })
//                        categoriesDropDown.setSelection(catList.indexOf(details?.c))
                        Log.d("Ashu", details?.coverImageServingPath.toString())
                        progressBar.dismiss()

                    }
                }

                override fun onFailure(call: Call<MediaStreamingResponse>, t: Throwable) {
                    if(context != null){
                        Toast.makeText(requireContext(), "Failed to load movie data", Toast.LENGTH_SHORT).show()
                        progressBar.dismiss()
                    }
                }

            })

    }

    fun getCategories() {
        progressBar.show()
        val retrofitAPI = ServiceBuilder.buildService(RetrofitAPI::class.java)
        val call = retrofitAPI.getAllCategories("Bearer " + (activity as HomePageActivity).token)
        call.enqueue(object : Callback<List<CategoryResponse>> {
            override fun onResponse(
                call: Call<List<CategoryResponse>>,
                response: Response<List<CategoryResponse>>
            ) {
                if (context != null) {
                    if (response.code() == 200) {
                        val categories = response.body() as List<CategoryResponse>
                        val categoryList = mutableListOf<String>()
                        for (i in categories) {
                            categoryList.add(i.allCategoryName)
                        }
                        Collections.unmodifiableList(categoryList)
                        catList = categoryList

                        Log.d("Ashu", categories.toString())

                        Log.d("Ashu", categoryList.toString())
                        val dropDownAdapter = ArrayAdapter(
                            requireContext(),
                            R.layout.dropdown_item,
                            categoryList
                        )

                        categoriesDropDown.setAdapter(dropDownAdapter)

                        loadMovieDetails()


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

            override fun onFailure(call: Call<List<CategoryResponse>>, t: Throwable) {
                if(context != null){
                    Toast.makeText(requireContext(), "Failed to load categories", Toast.LENGTH_SHORT)
                        .show()
                    progressBar.dismiss()
                }
            }

        })
    }

    fun getGenres(genresList : List<Genre>) : String{
        var str = ""
        for(i in 0 .. genresList.size - 2){
            str += genresList[i].genreName + ", "
        }
        str += genresList.last().genreName
        return str
    }


    fun editMovie(){
        progressBar.show()
        val retrofitAPI = ServiceBuilder.buildService(RetrofitAPI::class.java)
        val call = retrofitAPI.editMovie("Bearer $token", EditMovieRequest((activity as HomePageActivity).movieId.toString(), listOf(NewCategory(categoriesDropDown.selectedItem.toString())), getGenres(genreETxt.text.toString()), castETxt.text.toString(), descriptionETxt.text.toString(), movieNameETxt.text.toString(), movieYearETxt.text.toString()))
        call.enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(context != null){
                    Toast.makeText(requireContext(), response.body().toString(), Toast.LENGTH_SHORT).show()
                    progressBar.dismiss()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                if(context != null){
                    Toast.makeText(requireContext(), "Failed to edit movie", Toast.LENGTH_SHORT).show()
                    progressBar.dismiss()
                }
            }

        })
    }

    fun getGenres(str: String): List<NewGenre> {
        val list: List<String> = str.split(",");
        val list2: MutableList<NewGenre> = mutableListOf();
        list.forEach {
            val element = it.trim()
            if (element != "")
                list2.add(NewGenre(element))
        }
        return Collections.unmodifiableList(list2);
    }



}