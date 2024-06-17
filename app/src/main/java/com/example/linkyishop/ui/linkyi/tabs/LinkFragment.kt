package com.example.linkyishop.ui.linkyi.tabs

import android.content.Intent
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
import com.example.linkyishop.databinding.FragmentLinkBinding
import com.example.linkyishop.databinding.FragmentLinkyiBinding
import com.example.linkyishop.ui.linkyi.AddLinkyiActivity
import com.example.linkyishop.ui.linkyi.LinkyiViewModel
import kotlinx.coroutines.launch


class LinkFragment : Fragment() {
    private var _binding: FragmentLinkBinding? = null
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

        _binding = FragmentLinkBinding.inflate(inflater, container, false)
        with(binding) {
            featureSwitch.setOnCheckedChangeListener { _, isChecked ->
                isActive = if (isChecked) "1" else "0"
            }

            btnSimpan.setOnClickListener {
                simpanLink()
            }
        }


        val root: View = binding.root
        return root
    }

    private fun simpanLink() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.addLink(binding.edEditLink.text.toString(), binding.edEditName.text.toString(), isActive)
            viewModel.linkyiResponse.observe(viewLifecycleOwner) {
                if (it?.success == true){
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    binding.edEditLink.setText("")
                    binding.edEditName.setText("")
                    binding.featureSwitch.isChecked = true
                }
            }
//            viewModel.isLoading.observe(viewLifecycleOwner){
//                binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
//            }
//            viewModel.linkyi.observe(viewLifecycleOwner) { linkyi ->
//                if (linkyi != null) {
//                    setLinkyiData(linkyi)
//                }
//            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLinkBinding.bind(view)

//        binding.floatingActionButton.setOnClickListener {
//            val intent = Intent(requireContext(), AddLinkyiActivity::class.java)
//            startActivity(intent)
//        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}