package `in`.silive.felix

import `in`.silive.felix.datastore.DataStoreManager
import `in`.silive.felix.module.LogInInfo
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyProfileFragment : Fragment() {

    lateinit var signOutBtn : AppCompatButton


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_my_profile, container, false)
        signOutBtn = view.findViewById(R.id.signOutBtn)
        signOutBtn.setOnClickListener{

            val alertDialogBuilder = AlertDialog.Builder(view.context)
            alertDialogBuilder.setTitle("Confirm sign out?").setMessage("Do you want to sign out?").setPositiveButton("Yes", DialogInterface.OnClickListener { _, _ -> signOut(view) }).setNegativeButton("Cancel", DialogInterface.OnClickListener { _, _ ->  })
            val alert = alertDialogBuilder.create()
            alert.setCanceledOnTouchOutside(false)
            alert.show()
        }
        return view
    }


    fun signOut(view: View){
        lifecycleScope.launch(Dispatchers.IO) {
            val dataStoreManager = DataStoreManager(view.context)
            dataStoreManager.storeLogInInfo(LogInInfo("", false))
        }
        val intent = Intent(view.context, AuthenticationActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

}