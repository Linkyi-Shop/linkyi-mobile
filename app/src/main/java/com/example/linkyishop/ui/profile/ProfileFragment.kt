package com.example.linkyishop.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.linkyishop.data.ViewModelFactory
import com.example.linkyishop.databinding.FragmentProfileBinding
import com.example.linkyishop.ui.aktivasiToko.AktivasiTokoViewModel
import com.example.linkyishop.ui.aktivasiToko.UpdateStoreActivity
import com.example.linkyishop.ui.login.LoginViewModel
import com.example.linkyishop.ui.tema.TemaActivity
import com.example.linkyishop.ui.updatePassword.UpdatePasswordActivity
import com.example.linkyishop.ui.welcome.WelcomeActivity

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("user_token", "") ?: ""

        if (token.isNotEmpty()) {
            Log.d("ProfileFragment", "Token: $token")
            viewModel.fetchProfile(token)
        } else {
            Log.e("ProfileFragment", "Token is empty")
        }

        viewModel.profileData.observe(viewLifecycleOwner) { store ->
            if (store != null) {
                Log.d("ProfileFragment", "Store Data: $store")
                binding.tvTokoName.text = store.name
                binding.tvDesc.text = store.description
                Glide.with(this)
                    .load(store.logo)
                    .into(binding.ivThumbnail)
            } else {
                Log.e("ProfileFragment", "Profile data is null")
            }
        }

        binding.buttonLogout.setOnClickListener {
            sharedPreferences.edit().remove("user_token").apply()
            startActivity(Intent(activity, WelcomeActivity::class.java))
            activity?.finish()
        }

        binding.editToko.setOnClickListener {
            startActivity(Intent(activity, UpdateStoreActivity::class.java))
            activity?.finish()
        }

        binding.theme.setOnClickListener {
            startActivity(Intent(activity, TemaActivity::class.java))
            activity?.finish()
        }
        
        binding.updatePassword.setOnClickListener {
            startActivity(Intent(activity, UpdatePasswordActivity::class.java))
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}