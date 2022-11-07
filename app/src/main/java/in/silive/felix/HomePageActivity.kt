package `in`.silive.felix

import `in`.silive.felix.datastore.DataStoreManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomePageActivity : AppCompatActivity() {

    lateinit var token : String

    lateinit var bottomNavigationView : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        val fm : FragmentManager = supportFragmentManager
        val ft : FragmentTransaction = fm.beginTransaction()
        val homePageFragment = HomePageFragment()
        ft.add(R.id.container, homePageFragment)
        ft.commit()

        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home -> homeFrag()
                R.id.wishlist -> wishlistFrag()
                R.id.history -> historyFrag()
                R.id.profile -> profileFrag()
            }
            true
        }

        GlobalScope.launch(Dispatchers.IO) {
            val dataStoreManager = DataStoreManager(this@HomePageActivity)
            dataStoreManager.getLogInInfo().collect{
                token = it.token
            }
        }

    }



    private fun replaceFrag(fragment : Fragment, name : String){
        val fm : FragmentManager = supportFragmentManager
        val ft : FragmentTransaction = fm.beginTransaction()
        ft.addToBackStack(name)
        ft.add(R.id.container, fragment)
        ft.commit()
    }

//    fun profileFrag(view: View){
//        val myProfileFrag = MyProfileFragment()
//        replaceFrag(myProfileFrag, "Profile")
//    }

    fun profileFrag(){
        val myProfileFrag = MyProfileFragment()
        replaceFrag(myProfileFrag, "Profile")
    }

    fun homeFrag(){
        val homeFrag = HomePageFragment()
        replaceFrag(homeFrag, "Profile")
    }

    fun wishlistFrag(){
        val wishlistFragment = WishlistFragment()
        replaceFrag(wishlistFragment, "Wishlist")
    }

    fun historyFrag(){
        val historyFragment = WatchHistoryFragment()
        replaceFrag(historyFragment, "Wishlist")
    }

}