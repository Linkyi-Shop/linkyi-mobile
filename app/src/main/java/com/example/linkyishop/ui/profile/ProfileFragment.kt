package com.example.linkyishop.ui.profile

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.linkyishop.R
import com.example.linkyishop.data.ViewModelFactory
import com.example.linkyishop.databinding.FragmentProfileBinding
import com.example.linkyishop.ui.aktivasiToko.AktivasiTokoViewModel
import com.example.linkyishop.ui.aktivasiToko.UpdateStoreActivity
import com.example.linkyishop.ui.tema.TemaActivity
import com.example.linkyishop.ui.updatePassword.UpdatePasswordActivity
import com.example.linkyishop.ui.welcome.WelcomeActivity
import com.example.linkyishop.ui.login.LoginViewModel
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null

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
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

        binding.buttonLogout.setOnClickListener {
            viewModels.deleteUserToken()
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

        viewModels.profileResult.observe(viewLifecycleOwner) { data ->
            data?.let {
                binding.tvTokoName.text = it.name.toString()
                binding.tvDesc.text = it.description.toString()
                val baseLink = getString(R.string.generate_base_link)
                binding.tvLinks.text = "$baseLink${it.link}"
                Glide.with(this)
                    .load(it.logo)
                    .into(binding.ivThumbnail)

                binding.tvLinks.setOnClickListener {view ->
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("$baseLink${it.link}"))
                    startActivity(intent)
                }
            }
        }
        viewModels.getProfile()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}