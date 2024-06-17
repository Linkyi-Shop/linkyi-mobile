package com.example.linkyishop.ui.linkyi

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.linkyishop.R
import com.example.linkyishop.data.ViewModelFactory
import com.example.linkyishop.data.retrofit.response.Links
import com.example.linkyishop.data.retrofit.response.Products
import com.example.linkyishop.databinding.FragmentLinkyiBinding
import com.example.linkyishop.databinding.FragmentProductBinding
import com.example.linkyishop.ui.product.ProductsAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class LinkyiFragment : Fragment() {

    private var _binding: FragmentLinkyiBinding? = null
    private val viewModel by viewModels<LinkyiViewModel> {
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

        _binding = FragmentLinkyiBinding.inflate(inflater, container, false)

        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLinkyiBinding.bind(view)

        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(requireContext(), AddLinkyiActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getLinkyi()
            viewModel.isLoading.observe(viewLifecycleOwner){
                binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
            }
            viewModel.linkyi.observe(viewLifecycleOwner) { linkyi ->
                if (linkyi != null) {
                    setLinkyiData(linkyi)
                }
            }
        }
    }

    private fun setLinkyiData(links: Links) {
        val adapter = LinkyiAdapter(requireContext(), links)
        binding.rvProducts.layoutManager = LinearLayoutManager(requireContext())
        binding.rvProducts.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}