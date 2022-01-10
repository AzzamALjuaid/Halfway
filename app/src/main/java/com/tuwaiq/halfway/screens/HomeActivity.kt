package com.tuwaiq.halfway.screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.tuwaiq.halfway.R
import com.tuwaiq.halfway.adapter.UsersAdapter
import com.tuwaiq.halfway.databinding.FragmentProfileBinding
import com.tuwaiq.halfway.model.UserDetailModal
import com.tuwaiq.halfway.screens.fragments.PlacesFragment
import com.tuwaiq.halfway.screens.fragments.ProfileFragment
import com.tuwaiq.halfway.utility.Common
import com.tuwaiq.halfway.utility.Constant
import com.tuwaiq.halfway.utility.PreferencesHelper

class HomeActivity : AppCompatActivity() {
    private var fb_logut: FloatingActionButton? = null
    var firebaseDatabase : FirebaseDatabase? = null
    var databaseReference: DatabaseReference? = null
    private var rv_user: RecyclerView? = null
    private var mAuth: FirebaseAuth? = null
    private var loading: ProgressBar? = null
    private var userDetailModal: ArrayList<UserDetailModal>? = null
    private var tempUserDetailModal: ArrayList<UserDetailModal>? = null
    private var userAdapter: UsersAdapter? = null
    private var isMyAccountUpdated: Boolean = false
    private lateinit var binding: ActivityHomeBinding

    companion object {

        private const val MY_LOCATION_REQUEST_CODE = 329
        private const val NEW_REMINDER_REQUEST_CODE = 330
        private const val EXTRA_LAT_LNG = "EXTRA_LAT_LNG"

        fun newIntent(context: Context, latLng: LatLng): Intent {
            val intent = Intent(context, HomeActivity::class.java)
            intent.putExtra(EXTRA_LAT_LNG, latLng)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityHomeBinding.inflate(layoutInflater)
        initFirebaseChat();


        setContentView(binding.root)
        initView()

    }

    private fun initFirebaseChat() {
        Chat21Manager.start(this)
    }

    private fun initView() {

        binding.viewPager.apply {
            adapter=TabsPager(fragmentManager = supportFragmentManager, listOf(
                PlacesFragment.newInstance(),
                ChatUI.getInstance().conversationListFragment,
                ProfileFragment.newInstance()
            ))

        }

        binding.navView.setOnItemSelectedListener{ it->

            when(it.itemId){
                R.id.navigation_map->{
                    binding.viewPager.currentItem=0;
                }
                R.id.navigation_chat->{

                    binding.viewPager.currentItem=1;
                }
                R.id.navigation_profile->{
                    binding.viewPager.currentItem=2;
                }


            }

            false
        }


        rv_user = findViewById(R.id.rv_user)
        loading = findViewById(R.id.idPBLoading)
        fb_logout = findViewById(R.id.fb_logout)
        binding.btnChat.setOnClickListener {
            ChatUI.getInstance().openConversationsListActivity()

        }
        binding.btnPlaces.setOnClickListener {
            Common.startNewActivity(this@HomeActivity, PlaceActivity::class.java, false)
        }
        firebaseDatabase = FirebaseDatabase.getInstance()
        mAuth = FirebaseAuth.getInstance()
        userDetailModal = ArrayList()
        tempUserDetailModal = ArrayList()
        databaseReference = firebaseDatabase!!.getReference("account")
        val btn_account = findViewById<Button>(R.id.btn_account)

        btn_account.setOnClickListener {

            Common.startNewActivity(this@HomeActivity, AddDetailActivity::class.java, false)
//            startActivity(Intent(this@HomeActivity, AddDetailActivity::class.java))
        }
        usersAdapter = UsersAdapter(
            userDetailModal!!, this,
            object : UsersAdapter.UserClickInterface {
                override fun onClick(position: Int) {
                    var bundle = Bundle()
                    bundle.putParcelable("friend", userDetailModal?.get(position))
                    Common.startNewActivity(
                        this@HomeActivity,
                        PlaceActivity::class.java,
                        bundle,
                        false
                    )
                }
            })
        //setting layout malinger to recycler view on below line.
        //setting layout malinger to recycler view on below line.
        rv_user?.setLayoutManager(LinearLayoutManager(this))
        //setting adapter to recycler view on below line.
        //setting adapter to recycler view on below line.
        rv_user?.setAdapter(usersAdapter)
        //on below line calling a method to fetch courses from database.
        //on below line calling a method to fetch courses from database.
        //getUserList()

    }

    //getting the list of the user from firebase database
    private fun getUserList() {
        userDetailModal?.clear()
        tempUserDetailModal?.clear()

        //on below line we are calling add child event listener method to read the data.
        //on below line we are calling add child event listener method to read the data.
        databaseReference!!.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(
                snapshot: DataSnapshot,
                @Nullable previousChildName: String?
            ) {
                //on below line we are hiding our progress bar.

                //adding snapshot to our array list on below line.
                tempUserDetailModal?.add(snapshot.getValue(UserDetailModal::class.java)!!)
                for (item in tempUserDetailModal!!) {
                    if (!item.email.equals(
                            PreferencesHelper(this@HomeActivity).getString(Constant.SharedPref.USER_EMAIL),
                            true
                        )//filtering the data from duplicate
                        && userDetailModal?.contains(item) == false
                    )
                        userDetailModal?.add(item)
                    if (item.email.equals(
                            PreferencesHelper(this@HomeActivity).getString(Constant.SharedPref.USER_EMAIL),
                            true
                        )//avoiding the self name from getting reflected into the user list for confusion
                    ) {
                        println("=========>else")
                        isMyAccountUpdated = true
                        PreferencesHelper(this@HomeActivity).putString(Constant.SharedPref.USER_LATITUDE, item.latitude)
                        PreferencesHelper(this@HomeActivity).putString(USER_NAME, item.name)
                        PreferencesHelper(this@HomeActivity).putString(Constant.SharedPref.USER_GENDER, item.gender)
                        PreferencesHelper(this@HomeActivity).putString(Constant.SharedPref.USER_AGE, item.age)
                        PreferencesHelper(this@HomeActivity).putString(
                            Constant.SharedPref.USER_LONGITUDE,
                            item.longitude
                        )
                    }
                }
                //notifying our adapter that data has changed.
                usersAdapter?.notifyDataSetChanged()
                loading?.setVisibility(View.GONE)

//                if (!isMyAccountUpdated){
//                    Common(this@HomeActivity).showToast(this@HomeActivity, "Please update the account to view the list")
//                    Common(this@HomeActivity).startNewActivity(this@HomeActivity, AddDetailActivity::class.java, true)
//                }
            }

            override fun onChildChanged(
                snapshot: DataSnapshot,
                @Nullable previousChildName: String?
            ) {
                //this method is called when new child is added we are notifying our adapter and making progress bar visibility as gone.
                loading?.setVisibility(View.GONE)
                usersAdapter?.notifyDataSetChanged()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                //notifying our adapter when child is removed.
                usersAdapter?.notifyDataSetChanged()
                loading?.setVisibility(View.GONE)
            }

            override fun onChildMoved(
                snapshot: DataSnapshot,
                @Nullable previousChildName: String?
            ) {
                //notifying our adapter when child is moved.
                usersAdapter?.notifyDataSetChanged()
                loading?.setVisibility(View.GONE)
            }

            override fun onCancelled(error: DatabaseError) {
                loading?.setVisibility(View.GONE)
                Common.showToast(this@HomeActivity, error.message)
            }
        })
    }

    //feature to sign out or log out from the appication
    fun onSignOut(v: View) {
        PreferencesHelper(this).clearPreferences()
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
    }

}