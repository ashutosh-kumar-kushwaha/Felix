package `in`.silive.felix

import `in`.silive.felix.module.AddMovieDetails
import `in`.silive.felix.module.CategoryX
import `in`.silive.felix.module.GenreX
import `in`.silive.felix.server.RetrofitAPI
import `in`.silive.felix.server.ServiceBuilder
import android.app.Activity.RESULT_OK
import android.app.ApplicationErrorReport
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
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
    lateinit var movieNameETxt : TextInputEditText
    lateinit var descriptionETxt : TextInputEditText
    lateinit var castETxt : TextInputEditText
    lateinit var movieYearETxt : TextInputEditText
    lateinit var lengthETxt : TextInputEditText
    lateinit var genreETxt : TextInputEditText
    lateinit var uploadBtn: AppCompatButton
    lateinit var uploadMovieBtn: AppCompatButton

    var imageUri: Uri? = null
    var videoUri: Uri? = null

//    lateinit var image : URI


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_new_movie, container, false)

        movieNameETxt = view.findViewById(R.id.movieNameETxt)
        descriptionETxt = view.findViewById(R.id.descriptionETxt)
        castETxt = view.findViewById(R.id.castETxt)
        movieYearETxt = view.findViewById(R.id.movieYearETxt)
        lengthETxt = view.findViewById(R.id.lengthETxt)
        genreETxt = view.findViewById(R.id.genreETxt)
        categoriesDropDown = view.findViewById(R.id.categoriesDropdown)

        val dropDownAdapter = ArrayAdapter(
            requireContext(),
            R.layout.dropdown_item,
            listOf("Hollywood", "Bollywood", "Anime")
        )

        categoriesDropDown.setAdapter(dropDownAdapter)

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
            upload()
        }

        return view
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {

            if (requestCode == 100) {
                Toast.makeText(requireContext(), "uploaded", Toast.LENGTH_SHORT).show()
                imageUri = data?.data
                addImageBtn.setImageURI(imageUri)
            } else if (requestCode == 200) {
                videoUri = data?.data
            }
        }
    }


    fun upload() {
        try {
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
                "18+",
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
                    Toast.makeText(requireContext(), "Uploaded", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
                }

            })


        } catch (e: Exception) {
            Log.d("Ashu", e.message.toString())
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
}



