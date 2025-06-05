package com.doggetdrunk.meetjyou_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.doggetdrunk.meetjyou_android.databinding.ActivityMainBinding
import com.doggetdrunk.meetjyou_android.vm.PartyVM

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setBottomNavigationWithNavController()
        setVM()
    }


    private fun setBottomNavigationWithNavController(){
        // NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // bottom navigation + navController
        binding.bottomNavigation.setupWithNavController(navController)
    }

    private lateinit var partyVM : PartyVM
    private fun setVM(){
        try{
            partyVM = ViewModelProvider(this@MainActivity)[PartyVM::class.java]
        }catch (e:Exception){
            Log.e("mException", "MainActivity, setVM // Exception : ${e.localizedMessage}")
        }
    }
}