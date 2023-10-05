package com.android.kvc.xiao.v2.miui.ui.call

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.kvc.xiao.v2.miui.databinding.FragmentCallBinding

import java.io.File


class CallFragment : Fragment() {

    private var _binding: FragmentCallBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val callViewModel =
            ViewModelProvider(this).get(CallViewModel::class.java)

        _binding = FragmentCallBinding.inflate(inflater, container, false)
        val root: View = binding.root

        

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}