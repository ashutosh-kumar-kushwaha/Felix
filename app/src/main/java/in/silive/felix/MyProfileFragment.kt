package `in`.silive.felix

import `in`.silive.felix.datastore.DataStoreManager
import `in`.silive.felix.module.LogInInfo
import `in`.silive.felix.server.RetrofitAPI
import `in`.silive.felix.server.ServiceBuilder
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
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
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyProfileFragment : Fragment() {

    lateinit var signOutBtn: AppCompatButton
    lateinit var nameTxtVw: AppCompatTextView
    lateinit var emailTxtVw: AppCompatTextView
    lateinit var wishlistBtn: AppCompatButton
    lateinit var changePassBtn: AppCompatButton
    lateinit var historyBtn: AppCompatButton
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

        val view = inflater.inflate(R.layout.fragment_my_profile, container, false)

        progressBar = getDialogueProgressBar(view).create()
        progressBar.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressBar.setCanceledOnTouchOutside(false)

        nameTxtVw = view.findViewById(R.id.nameTxtVw)
        emailTxtVw = view.findViewById(R.id.emailTxtVw)
        nameTxtVw.text = (activity as HomePageActivity).name
        emailTxtVw.text = (activity as HomePageActivity).email

        signOutBtn = view.findViewById(R.id.signOutBtn)

        wishlistBtn = view.findViewById(R.id.wishlistBtn)
        historyBtn = view.findViewById(R.id.historyBtn)
        changePassBtn = view.findViewById(R.id.changePassBtn)

        signOutBtn.setOnClickListener {

            val alertDialogBuilder = AlertDialog.Builder(view.context)
            alertDialogBuilder.setTitle("Confirm sign out?").setMessage("Do you want to sign out?")
                .setPositiveButton(
                    "Yes",
                    DialogInterface.OnClickListener { _, _ -> (activity as HomePageActivity).signOut() })
                .setNegativeButton("Cancel", DialogInterface.OnClickListener { _, _ -> })
            val alert = alertDialogBuilder.create()
            alert.setCanceledOnTouchOutside(false)
            alert.show()
        }

        wishlistBtn.setOnClickListener {
            (activity as HomePageActivity).wishlistFrag()
        }

        changePassBtn.setOnClickListener {
            (activity as HomePageActivity).changePassFrag()
        }

        historyBtn.setOnClickListener {
            (activity as HomePageActivity).historyFrag()
        }

        return view
    }


}