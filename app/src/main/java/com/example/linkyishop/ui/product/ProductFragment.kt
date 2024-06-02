package com.example.linkyishop.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.linkyishop.data.retrofit.response.DataItem
import com.example.linkyishop.databinding.FragmentProductBinding
import com.google.android.material.snackbar.Snackbar

class ProductFragment : Fragment() {

    private var _binding: FragmentProductBinding? = null
    private lateinit var viewModel: ProductViewModel

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

        viewModel = ViewModelProvider(this).get(ProductViewModel::class.java)

        viewModel.listProduct.observe(viewLifecycleOwner) { productsList ->
            setUsersData(productsList.getContentIfNotHandled())
        }
//        viewModel.isLoading.observe(viewLifecycleOwner) {
//            showLoading(it)
//        }
//        viewModel.message.observe(viewLifecycleOwner) {
//            it.getContentIfNotHandled()?.let { snackBarText ->
//                Snackbar.make(
//                    view,
//                    snackBarText,
//                    Snackbar.LENGTH_SHORT
//                ).show()
//            }
//        }
    }

    private fun setUsersData(usersList: List<DataItem?>?) {
        val adapter = ProductsAdapter(requireContext())
        binding.rvProducts.layoutManager = LinearLayoutManager(requireContext())
        adapter.submitList(usersList)
        binding.rvProducts.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}