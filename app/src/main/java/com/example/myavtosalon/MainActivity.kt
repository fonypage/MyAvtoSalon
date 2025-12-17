package com.example.myavtosalon

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myavtosalon.databinding.ActivityMainBinding
import com.example.myavtosalon.ui.brands.BrandsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, BrandsFragment())
                .commit()
        }
    }
}
