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
import com.tuwaiq.halfway.databinding.FragmentMapBinding
import com.tuwaiq.halfway.databinding.FragmentProfileBinding
import com.tuwaiq.halfway.ui.map.MapViewModel

class ProfileFragment : Fragment() {

    private lateinit var mapViewModel: MapViewModel
    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mapViewModel =
            ViewModelProvider(this).get(MapViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        
        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}