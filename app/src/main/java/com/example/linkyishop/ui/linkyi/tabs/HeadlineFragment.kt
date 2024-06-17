package com.example.linkyishop.ui.linkyi.tabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.linkyishop.R
import com.example.linkyishop.data.ViewModelFactory
import com.example.linkyishop.databinding.FragmentHeadlineBinding
import com.example.linkyishop.databinding.FragmentLinkBinding
import com.example.linkyishop.ui.linkyi.LinkyiViewModel
import kotlinx.coroutines.launch


class HeadlineFragment : Fragment() {
    private var _binding: FragmentHeadlineBinding? = null
    private val viewModel by viewModels<LinkyiViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private var isActive = "1"
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHeadlineBinding.inflate(inflater, container, false)

        val root: View = binding.root
        return root
    }

    private fun simpanHeadline() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.addHeadline(binding.edEditName.text.toString(), isActive)
            viewModel.linkyiResponse.observe(viewLifecycleOwner) {
                if (it?.success == true){
                    Toast.makeText(requireContext(), "Headline berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                    binding.edEditName.setText("")
                    binding.featureSwitch.isChecked = true
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHeadlineBinding.bind(view)

        with(binding) {
            featureSwitch.setOnCheckedChangeListener { _, isChecked ->
                isActive = if (isChecked) "1" else "0"
                Toast.makeText(requireContext(), "Headline $isActive", Toast.LENGTH_SHORT).show()
            }

            btnSimpan.setOnClickListener {
                simpanHeadline()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}