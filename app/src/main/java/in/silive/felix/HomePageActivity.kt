package `in`.silive.felix

import `in`.silive.felix.datastore.DataStoreManager
import `in`.silive.felix.module.LogInInfo
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomePageActivity : AppCompatActivity() {

    lateinit var searchLinearLayout : LinearLayoutCompat
    lateinit var searchBtn : AppCompatImageView
    lateinit var token : String
    lateinit var name : String
    lateinit var email : String
    lateinit var role : String
    var movieId : Int = -1
    lateinit var bottomNavigationView : BottomNavigationView
    lateinit var searchView: SearchView
    var searchText : String = ""
    lateinit var frag : Fragment
    lateinit var categoriesBtn : AppCompatTextView
    var categoryName : String = ""
    lateinit var categoryBtn : AppCompatTextView
    lateinit var appbarLayout: AppBarLayout
    var currentFragment : String? = "Home"

    override fun onCreate(savedInstanceState: Bundle?) {



        lifecycleScope.launch(Dispatchers.IO) {
            val dataStoreManager = DataStoreManager(this@HomePageActivity)
            dataStoreManager.getLogInInfo().collect{
                token = it.token
                name = it.name
                email = it.email
                role = it.role
            }
        }

        window?.requestFeature(Window.FEATURE_ACTION_BAR);

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        val uri = intent.data




        appbarLayout = findViewById(R.id.toolbarContainer)


        categoriesBtn = findViewById(R.id.categoriesBtn)

        categoriesBtn.setOnClickListener{
            categoryFrag()
        }

//        searchBtn = findViewById(R.id.searchBtn)
//
//        searchBtn.setOnClickListener{
//            searchActivity()
//        }


//        searchLinearLayout = findViewById(R.id.searchLinearLayout)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        if(role == "ADMIN"){
            bottomNavigationView.menu.clear()
            bottomNavigationView.inflateMenu(R.menu.bottom_menu_admin)
        }


        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home -> homeFrag()
                R.id.wishlist -> wishlistFrag()
                R.id.history -> historyFrag()
                R.id.profile -> profileFrag()
                R.id.admin -> adminFrag()
            }
            true
        }




        

        searchView = findViewById(R.id.searchView)
        appbarLayout = findViewById(R.id.toolbarContainer)


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


        if(uri != null){
            movieId = uri.getQueryParameter("movieId")!!.toInt()
            val fm : FragmentManager = supportFragmentManager
            val ft : FragmentTransaction = fm.beginTransaction()
            val mediaStreamFragment = MediaStreamFragment()
            ft.add(R.id.container, mediaStreamFragment)
            ft.commit()
        }
        else{
            val fm : FragmentManager = supportFragmentManager
            val ft : FragmentTransaction = fm.beginTransaction()
            val homePageFragment = HomePageFragment()
            ft.add(R.id.container, homePageFragment)
            ft.commit()
        }

    }

    fun adminFrag() {
        val adminFragment = AdminFragment()
        replaceFrag(adminFragment, "Admin")
    }


    fun showActionBar(){
        appbarLayout.visibility = View.VISIBLE
    }

    fun hideActionBar(){
        appbarLayout.visibility = View.GONE
    }

    fun hideBottomNav(){
        bottomNavigationView.visibility = View.GONE
    }

    fun showBottomNav(){
        bottomNavigationView.visibility = View.VISIBLE
    }


    private fun replaceFrag(fragment : Fragment, name : String){
        val fm : FragmentManager = supportFragmentManager
        val ft : FragmentTransaction = fm.beginTransaction()
        if(name == "Profile" || name == "Wishlist" || name == "History" || name == "Home"){
            for (i in 0 .. fm.backStackEntryCount) {
                fm.popBackStack()
            }
            ft.replace(R.id.container, fragment)
        }
        else{
            ft.addToBackStack(name)
            ft.add(R.id.container, fragment)
        }


        ft.commit()
    }

//    fun profileFrag(view: View){
//        val myProfileFrag = MyProfileFragment()
//        replaceFrag(myProfileFrag, "Profile")
//    }

    fun mediaStreamingFrag(){
        val mediaStreamFragment = MediaStreamFragment()
        replaceFrag(mediaStreamFragment, "Media")
    }

    fun profileFrag(){
        val myProfileFrag = MyProfileFragment()
        replaceFrag(myProfileFrag, "Profile")
    }

    fun moviesByCategoryFrag(){
        val moviesByCategoryFragment = MoviesByCategoryFragment()
        replaceFrag(moviesByCategoryFragment, "moviesByCategory")
    }

    fun homeFrag(){
        val homeFrag = HomePageFragment()
        replaceFrag(homeFrag, "Home")
    }

    fun changePassFrag(){
        val changePasswordFragment = ChangePasswordFragment()
        replaceFrag(changePasswordFragment, "ChangePass")
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


    fun categoryFrag(){
        val categoryFragment = CategoryFragment()
        replaceFrag(categoryFragment, "Category")
    }

    fun newMovieFrag(){
        val newMovieFragment = NewMovieFragment()
        replaceFrag(newMovieFragment, "AddMovie")
    }

    fun deleteMovieFrag(){
        val deleteMovieFragment = DeleteMovieFragment()
        replaceFrag(deleteMovieFragment, "DeleteMovie")
    }

    fun newCategoryFrag(){
        val newCategoryFragment = NewCategoryFragment()
        replaceFrag(newCategoryFragment, "NewCategory")
    }


    fun signOut(){
        lifecycleScope.launch(Dispatchers.IO) {
            val dataStoreManager = DataStoreManager(this@HomePageActivity)
            dataStoreManager.storeLogInInfo(LogInInfo("", false, "", "", ""))
        }
        val intent = Intent(this, AuthenticationActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        showBottomNav()
        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        for (fragment in supportFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}