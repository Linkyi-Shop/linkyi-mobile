package com.example.linkyishop.ui.dashboard

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.linkyishop.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {
//    private val dashboardViewModel: DashboardViewModel by viewModels()
    private var _binding: FragmentDashboardBinding? = null
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

        val sharedPref = activity?.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val token = sharedPref?.getString("auth_token", null)

        if (token != null) {
            // Observe data changes
            dashboardViewModel.dashboardData.observe(viewLifecycleOwner, Observer { visitors ->
                val total_barang = binding.totalBarang
                val jumlah_klik = binding.jumlahKlik
                val total_kategori = binding.totalKategori
                val jumlah_pengunjung = binding.jumlahPengunjung

                visitors?.let {
                    total_barang.text = it.product.toString()
                    jumlah_klik.text = it.totalClick.toString()
                    total_kategori.text = it.category.toString()
                    jumlah_pengunjung.text = it.visitor.toString()
                }
            })

            // Fetch the dashboard data
            dashboardViewModel.fetchDashboardData(token)
        } else {
            // Handle case where token is not available
            // You might want to redirect to login or show an error message
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}