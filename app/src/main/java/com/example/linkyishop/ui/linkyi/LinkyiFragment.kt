package com.example.linkyishop.ui.linkyi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.linkyishop.databinding.FragmentLinkyiBinding

class LinkyiFragment : Fragment() {

    private var _binding: FragmentLinkyiBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(LinkyiViewModel::class.java)

        _binding = FragmentLinkyiBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textLinkyi
        notificationsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}