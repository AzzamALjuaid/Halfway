package com.tuwaiq.halfway.ui.profile

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tuwaiq.halfway.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }}



//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
////    ): View? {
////        profileViewModel =
////            ViewModelProvider(this).get(ProfileViewModel::class.java)
////
////        _binding = FragmentProfileBinding.inflate(inflater, container, false)
////        val root: View = binding.root
////
////        val textView: TextView = binding.textNotifications
////        profileViewModel.text.observe(viewLifecycleOwner, Observer {
////            textView.text = it
////        })
////        return root
////    }
//
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}