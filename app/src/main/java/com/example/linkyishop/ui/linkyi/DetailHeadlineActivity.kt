package com.example.linkyishop.ui.linkyi

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.example.linkyishop.R
import com.example.linkyishop.data.ViewModelFactory
import com.example.linkyishop.databinding.ActivityDetailHeadlineBinding
import com.example.linkyishop.databinding.ActivityDetailLinkyiBinding

class DetailHeadlineActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailHeadlineBinding
    private val viewModel by viewModels<LinkyiViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailHeadlineBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val extraId = intent.getStringExtra(EXTRA_ID)

        setLinkyi(extraId!!)

        with(binding) {
            topAppBar.setNavigationOnClickListener { finish() }
            featureSwitch.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked == true) {
                    viewModel.linkyiStatus(extraId, "1")
                    Toast.makeText(this@DetailHeadlineActivity, "Item Aktif", Toast.LENGTH_SHORT).show()
                }else{
                    viewModel.linkyiStatus(extraId, "0")
                    Toast.makeText(this@DetailHeadlineActivity, "Item Tidak Aktif", Toast.LENGTH_SHORT).show()
                }
            }
            btnSimpan.setOnClickListener {
                viewModel.linkyiUpdate(
                    extraId,
                    edEditName.text.toString(),
                    if (featureSwitch.isChecked == true) {
                        "1"
                    }else{
                        "0"
                    }
                )
                finish()
                startActivity(getIntent())
                Toast.makeText(this@DetailHeadlineActivity, "Item Berhasil Diubah", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun setLinkyi(extraId: String) {
        viewModel.getLinkyi(extraId)
        viewModel.linkyiDetail.observe(this){
            with(binding){
                if(it?.data?.isActive == false){
                    featureSwitch.isChecked = false
                }
                edEditName.setText(it?.data?.name)
                edEditName.addTextChangedListener {text ->
                    if (text.toString() != it?.data?.name){
                        btnSimpan.isEnabled = true
                    }else{
                        btnSimpan.isEnabled = false
                    }
                }
            }
        }
    }

    companion object {
        const val EXTRA_ID = "extra_id"
    }

}