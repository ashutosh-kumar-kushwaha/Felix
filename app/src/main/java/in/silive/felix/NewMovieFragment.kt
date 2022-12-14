package `in`.silive.felix

import `in`.silive.felix.module.AddMovieDetails
import `in`.silive.felix.module.CategoryResponse
import `in`.silive.felix.module.CategoryX
import `in`.silive.felix.module.GenreX
import `in`.silive.felix.recyclerview.RecyclerCategoryAdapter
import `in`.silive.felix.server.RetrofitAPI
import `in`.silive.felix.server.ServiceBuilder
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.ApplicationErrorReport
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*

class NewMovieFragment : Fragment() {

    lateinit var addImageBtn: AppCompatImageView
    lateinit var categoriesDropDown: Spinner
    lateinit var movieNameETxt: TextInputEditText
    lateinit var descriptionETxt: TextInputEditText
    lateinit var castETxt: TextInputEditText
    lateinit var movieYearETxt: TextInputEditText
    lateinit var lengthETxt: TextInputEditText
    lateinit var genreETxt: TextInputEditText
    lateinit var uploadBtn: AppCompatButton
    lateinit var uploadMovieBtn: AppCompatButton
    lateinit var restrictionDropdown: Spinner

    lateinit var progressBar: AlertDialog
    var builder: AlertDialog.Builder? = null

    var imageUri: Uri? = null
    var videoUri: Uri? = null

//    lateinit var image : URI


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_new_movie, container, false)


        progressBar = getDialogueProgressBar(view).create()
        progressBar.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressBar.setCanceledOnTouchOutside(false)


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



        addImageBtn = view.findViewById(R.id.addImageBtn)
        uploadBtn = view.findViewById(R.id.uploadBtn)

        uploadMovieBtn = view.findViewById(R.id.uploadMovieBtn)
        addImageBtn.setOnClickListener {

            val intent = Intent(Intent.ACTION_PICK)
            intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            activity?.startActivityForResult(intent, 100)

        }

        uploadMovieBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
//            intent.setData(MediaStore.Video.Media.EXTERNAL_CONTENT_URI)

            intent.type = "video/mp4"
            activity?.startActivityForResult(intent, 200)
        }

        uploadBtn.setOnClickListener {
            var checkImage = false
            var checkVideo = false
            var checkMovieName = false
            var checkDescription = false
            var checkCast = false
            var checkYear = false
            var checkLength = false
            var checkGenre = false

            if(imageUri != null){
                checkImage = true
            }
            else{
                Toast.makeText(requireContext(), "Select a cover image", Toast.LENGTH_SHORT).show()
            }

            if(videoUri != null){
                checkVideo = true
            }
            else{
                Toast.makeText(requireContext(), "Select a video", Toast.LENGTH_SHORT).show()
            }

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

            if(checkImage && checkVideo && checkMovieName && checkDescription && checkCast && checkYear && checkLength && checkGenre){
                upload()
            }
        }

        getCategories()

        return view
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {

            if (requestCode == 100) {
                imageUri = data?.data
                addImageBtn.setImageURI(imageUri)
            } else if (requestCode == 200) {
                videoUri = data?.data
                uploadMovieBtn.text = "Selected"
            }
        }
    }


    fun upload() {
        progressBar.show()
        if (context != null) {
            val retrofitAPI = ServiceBuilder.buildService(RetrofitAPI::class.java)
            val uriPathHelper = URIPathHelper()
            val coverImagePath = uriPathHelper.getPath(requireContext(), imageUri!!)
            val moviePath = uriPathHelper.getPath(requireContext(), videoUri!!)
            val coverImageFile = File(coverImagePath)
            val movieFile = File(moviePath)
            val requestCoverImage =
                RequestBody.create("image/png".toMediaTypeOrNull(), coverImageFile)
            val requestMovie = RequestBody.create("video/mp4".toMediaTypeOrNull(), movieFile)
            val coverImageBody = MultipartBody.Part.createFormData(
                "coverImage",
                coverImageFile.name,
                requestCoverImage
            )
            val movieBody =
                MultipartBody.Part.createFormData("movieVideo", movieFile.name, requestMovie)
            val addMovieDetails = AddMovieDetails(
                listOf(CategoryX(categoriesDropDown.selectedItem.toString())),
                getGenres(genreETxt.text.toString()),
                castETxt.text.toString(),
                descriptionETxt.text.toString(),
                lengthETxt.text.toString(),
                movieNameETxt.text.toString(),
                restrictionDropdown.selectedItem.toString(),
                movieYearETxt.text.toString()
            )
            val gson = Gson()
            val details: String = gson.toJson(addMovieDetails)
            val requestDetails = RequestBody.create("text/plain".toMediaTypeOrNull(), details)
            val detailsBody = MultipartBody.Part.createFormData("movie", "movie", requestDetails)
            val call = retrofitAPI.addNewMovie(
                "Bearer " + (activity as HomePageActivity).token,
                coverImageBody,
                movieBody,
                detailsBody
            )
            call.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if(context != null){
                        Toast.makeText(requireContext(), response.body().toString(), Toast.LENGTH_SHORT).show()
                        Log.d("Ashu", response.code().toString())
                        progressBar.dismiss()
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    if(context != null){
                        Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
                        progressBar.dismiss()
                    }
                }
            })
        }
    }

    fun getGenres(str: String): List<GenreX> {
        val list: List<String> = str.split(",");
        val list2: MutableList<GenreX> = mutableListOf();
        list.forEach {
            val element = it.trim()
            if (element != "")
                list2.add(GenreX(element))
        }
        return Collections.unmodifiableList(list2);
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
                        Log.d("Ashu", categories.toString())

                        Log.d("Ashu", categoryList.toString())
                        val dropDownAdapter = ArrayAdapter(
                            requireContext(),
                            R.layout.dropdown_item,
                            categoryList
                        )

                        categoriesDropDown.setAdapter(dropDownAdapter)
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
                    progressBar.dismiss()
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
}



