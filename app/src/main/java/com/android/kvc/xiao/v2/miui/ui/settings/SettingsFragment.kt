package com.android.kvc.xiao.v2.miui.ui.settings

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.ViewModelProvider
import com.android.kvc.xiao.v2.miui.databinding.FragmentSettingsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var textViewSettings1: TextView
    private lateinit var textViewSettings2: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val settingsViewModel =
            ViewModelProvider(this).get(SettingsViewModel::class.java)

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        textViewSettings1 = binding.textviewSettings1
        textViewSettings2 = binding.textviewSettings2

        textViewSettings1.text = "构建时间：2023.10.04"

        queryShizukuAuthorization()

        return root
    }

    private fun queryShizukuAuthorization() {
        lifecycleScope.launch {
            val isAuthorized = withContext(Dispatchers.IO) {
                checkShizukuAuthorization()
            }
            updateShizukuAuthorizationText(isAuthorized)
        }
    }

    private fun checkShizukuAuthorization(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val hasPermission = ContextCompat.checkSelfPermission(
    requireContext(),
    "moe.shizuku.manager.permission.API_V23"
) == PackageManager.PERMISSION_GRANTED

            return hasPermission
        }
        return false
    }

    private fun updateShizukuAuthorizationText(isAuthorized: Boolean?) {
        val authorizationText = if (isAuthorized == true) {
            "Shizuku的授权状态：已授权"
        } else {
            "Shizuku的授权状态：未授权"
        }
        textViewSettings2.text = authorizationText
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}