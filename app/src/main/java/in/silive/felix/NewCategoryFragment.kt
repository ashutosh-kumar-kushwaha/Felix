package `in`.silive.felix

import `in`.silive.felix.module.NewCategoryRequest
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
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class NewCategoryFragment : Fragment() {

    lateinit var categoryETxt : TextInputEditText
    lateinit var doneBtn : AppCompatButton
    lateinit var token : String
    lateinit var progressBar: AlertDialog
    var builder: AlertDialog.Builder? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_new_category, container, false)

        progressBar = getDialogueProgressBar(view).create()
        progressBar.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressBar.setCanceledOnTouchOutside(false)

        doneBtn = view.findViewById(R.id.doneBtn)
        token = (activity as HomePageActivity).token
        categoryETxt = view.findViewById(R.id.categoryETxt)
        doneBtn.setOnClickListener{
            createNewCategory()
        }


        return view
    }

    private fun createNewCategory() {
        progressBar.show()
        if(context != null) {
            val retrofitAPI = ServiceBuilder.buildService(RetrofitAPI::class.java)
            val call = retrofitAPI.addNewCategory("Bearer $token", NewCategoryRequest(categoryETxt.text.toString()))
            call.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if(response.code() == 201){
                        Toast.makeText(requireContext(), "Category added successfully", Toast.LENGTH_SHORT).show()
                    }
                    else if(response.code() == 403){
                        Toast.makeText(requireContext(), "You must be an ADMIN to perform this action", Toast.LENGTH_SHORT).show()
                    }
                    else if(response.code() == 409){
                        Toast.makeText(requireContext(), "This category already exists", Toast.LENGTH_SHORT).show()
                    }
                    else if (response.code() == 401) {
                        (activity as HomePageActivity).signOut()
                    } else if (response.code() == 500) {
                        Toast.makeText(
                            requireContext(),
                            "Internal Server Error. Please try again.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(requireContext(), response.code().toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
                    progressBar.dismiss()
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    if(context != null){
                        Toast.makeText(requireContext(), "Failed to add category", Toast.LENGTH_SHORT)
                            .show()
                        progressBar.dismiss()
                    }
                }
            })
        }
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