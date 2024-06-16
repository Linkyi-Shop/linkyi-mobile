package com.example.linkyishop.ui.linkyi

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.example.linkyishop.R
import com.example.linkyishop.data.ViewModelFactory
import com.example.linkyishop.databinding.ActivityDetailLinkyiBinding

class DetailLinkyiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailLinkyiBinding
    private val viewModel by viewModels<LinkyiViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailLinkyiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val extraId = intent.getStringExtra(EXTRA_ID)

        viewModel.getLinkyi(extraId!!)
        viewModel.linkyiDetail.observe(this){
            with(binding){

                edEditLink.setText(it?.data?.link)
                edEditName.setText(it?.data?.name)
                edEditLink.addTextChangedListener {text ->
                    if (text.toString() != it?.data?.link || edEditName.text.toString() != it.data.name){
                        btnSimpan.isEnabled = true
                    }else{
                        btnSimpan.isEnabled = false
                    }
                }
                edEditName.addTextChangedListener {text ->
                    if (text.toString() != it?.data?.name || edEditLink.text.toString() != it.data.link){
                        btnSimpan.isEnabled = true
                    }else{
                        btnSimpan.isEnabled = false
                    }
                }
            }
        }
        with(binding) {
            topAppBar.setNavigationOnClickListener { finish() }
        }

    }

    companion object {
        const val EXTRA_ID = "extra_id"
    }

}