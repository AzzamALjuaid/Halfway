package com.tuwaiq.halfway

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.tuwaiq.halfway.databinding.ActivityHalfwayBinding

//import io.getstream.chat.android.client.ChatClient

private const val TAG = "HalfwayActivity"
class HalfwayActivity : AppCompatActivity() {

    private lateinit var navController: NavController//chatSDK
//    private val client = ChatClient.instance()//chatSDK

    private lateinit var binding: ActivityHalfwayBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navController = findNavController(R.id.fragmentContainerView)

        if(navController.currentDestination?.label.toString().contains("login")){
//            val currentUser = client.getCurrentUser()
//            if (currentUser != null){
//                val user = ChatUser(currentUser.name,currentUser.id)
//                val action = LoginFragmentDirections.actionLoginFragmentToChannelFragment(user)
//            }
//        }


        binding = ActivityHalfwayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_halfway)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_map, R.id.navigation_chat, R.id.navigation_profile
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}}