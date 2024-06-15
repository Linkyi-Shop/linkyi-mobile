package com.example.linkyishop.ui.profile

import android.content.Intent
import android.os.Bundle
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
import com.example.linkyishop.ui.login.LoginViewModel
import com.example.linkyishop.ui.updatePassword.UpdatePasswordActivity
import com.example.linkyishop.ui.welcome.WelcomeActivity

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private val viewModels by viewModels<AktivasiTokoViewModel> {
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
        val homeViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        viewModels.profileResult.observe(viewLifecycleOwner) { profileResponse ->
//            profileResponse?.let {
//                val store = it.data?.store
//
//                // Update UI dengan data profil toko
//                binding.tvTokoName.text = store?.name
//                binding.tvDesc.text = store?.description
//                Glide.with(this).load(store?.logo).into(binding.ivThumbnail)
//            }
//        }
//
//        viewModels.getProfile()

        binding.buttonLogout.setOnClickListener {
            viewModel.deleteUserToken()
            startActivity(Intent(activity, WelcomeActivity::class.java))
            activity?.finish()
        }

        binding.updatePassword.setOnClickListener {
            viewModel.getUserToken()
            startActivity(Intent(activity, UpdatePasswordActivity::class.java))
            activity?.finish()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}