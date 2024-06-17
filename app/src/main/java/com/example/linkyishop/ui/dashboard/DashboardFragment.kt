package com.example.linkyishop.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.linkyishop.data.ViewModelFactory
import com.example.linkyishop.databinding.FragmentDashboardBinding
import com.example.linkyishop.ui.aktivasiToko.AktivasiTokoViewModel

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val viewModel by viewModels<AktivasiTokoViewModel> {
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
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.dashboard.observe(viewLifecycleOwner, Observer { data ->
            data?.let {
                binding.totalBarang.text = it.product.toString()
                binding.jumlahKlik.text = it.totalClick.toString()
                binding.totalKategori.text = it.category.toString()
                binding.jumlahPengunjung.text = it.visitor.toString()
            }
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        viewModel.getDashboard()

        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}