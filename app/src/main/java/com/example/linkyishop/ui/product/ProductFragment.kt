package com.example.linkyishop.ui.product

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.linkyishop.data.ViewModelFactory
import com.example.linkyishop.data.retrofit.response.DataItem
import com.example.linkyishop.data.retrofit.response.Products
import com.example.linkyishop.databinding.FragmentProductBinding
import com.example.linkyishop.ui.otp.OtpViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class ProductFragment : Fragment() {

    private var _binding: FragmentProductBinding? = null
    private val viewModel by viewModels<ProductViewModel> {
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

        _binding = FragmentProductBinding.inflate(inflater, container, false)
        val view: View = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProductBinding.bind(view)

        binding.floatingActionButton.setOnClickListener {
            navigateToAddProduct()
        }

    }
    override fun onResume() {
        super.onResume()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getProducts()
            viewModel.isLoading.observe(viewLifecycleOwner) {
                binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
            }
            viewModel.listProduct.observe(viewLifecycleOwner) { products ->
                setUsersData(products)
            }
        }
    }
    private fun setUsersData(products: Products) {
        val adapter = ProductsAdapter(requireContext(), products)
        binding.rvProducts.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvProducts.adapter = adapter
    }
    private fun navigateToAddProduct() {
        val intent = Intent(requireContext(), AddProductActivity::class.java)
        startActivity(intent)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}