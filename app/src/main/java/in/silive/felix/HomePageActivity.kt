package `in`.silive.felix

import `in`.silive.felix.datastore.DataStoreManager
import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomePageActivity : AppCompatActivity() {

    lateinit var searchLinearLayout : LinearLayoutCompat
    lateinit var searchBtn : AppCompatImageView
    lateinit var token : String
    var movieId : Int = -1
    lateinit var bottomNavigationView : BottomNavigationView
    lateinit var searchView: SearchView
    var searchText : String = ""
    lateinit var frag : Fragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        val fm : FragmentManager = supportFragmentManager
        val ft : FragmentTransaction = fm.beginTransaction()
        val homePageFragment = HomePageFragment()
        ft.add(R.id.container, homePageFragment)
        ft.commit()



//        searchBtn = findViewById(R.id.searchBtn)
//
//        searchBtn.setOnClickListener{
//            searchActivity()
//        }


//        searchLinearLayout = findViewById(R.id.searchLinearLayout)
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
                Log.d("Ashu", it.token)
            }
        }





        searchView = findViewById(R.id.searchView)

        searchView.setOnSearchClickListener {

            searchFrag()

        }

//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
//            override fun onQueryTextSubmit(p0: String?): Boolean {
////                Toast.makeText(this@HomePageActivity, p0, Toast.LENGTH_SHORT).show()
//                if(p0 != null){
//                    searchText = p0
//
////                    searchFragment.searchItems()
//                    return true
//                }
//
//
//                return false
//            }
//
//            override fun onQueryTextChange(p0: String?): Boolean {
////                Toast.makeText(this@HomePageActivity, p0, Toast.LENGTH_SHORT).show()
//                if(p0 != null){
//                    searchText = p0
//                    Toast.makeText(this@HomePageActivity, searchText, Toast.LENGTH_SHORT).show()
////                    searchFragment.searchItems()
//                    return true
//                }
//
//                return false
//            }
//
//        })

        setSupportActionBar(findViewById(R.id.toolbar))
        val actionBar = supportActionBar
        actionBar?.setDisplayShowTitleEnabled(false)

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

    fun mediaStreamingFrag(){
        val mediaStreamFragment = MediaStreamFragment()
        replaceFrag(mediaStreamFragment, "Profile")
    }

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
        replaceFrag(historyFragment, "History")
    }

    fun searchFrag(){
        val searchFragment = SearchFragment()
        replaceFrag(searchFragment, "Search")
    }

    fun searchActivity(){
        searchBtn.visibility = View.GONE
        searchLinearLayout.visibility =View.VISIBLE
//        val intent = Intent(this, SearchActivity::class.java)
//        overridePendingTransition(0,0)
//        startActivity(intent)
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if(item.itemId == R.id.searchView){
//            Toast.makeText(this, "CLicked", Toast.LENGTH_SHORT).show()
//        }
//        return super.onOptionsItemSelected(item)
//    }
}