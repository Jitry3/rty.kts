package com.android.kvc.xiao.v2.miui.ui.atyy

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ListView
import android.widget.Button
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.kvc.xiao.v2.miui.databinding.FragmentAtyyBinding

class AtyyFragment : Fragment() {

    private var _binding: FragmentAtyyBinding? = null
    private val binding get() = _binding!!

    private lateinit var atyyViewModel: AtyyViewModel
    

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAtyyBinding.inflate(inflater, container, false)
        val root: View = binding.root

        atyyViewModel = ViewModelProvider(this).get(AtyyViewModel::class.java)

        

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}