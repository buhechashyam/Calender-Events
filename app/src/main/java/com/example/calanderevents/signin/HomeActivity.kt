package com.example.sociallogin

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.appcompat.app.AppCompatActivity
import com.example.sociallogin.databinding.ActivityHomeBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class HomeActivity : AppCompatActivity(), OnClickListener {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

    }

    private fun init() {
        auth = Firebase.auth
        binding.signOut.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.signOut -> {
                if (auth.currentUser != null){
                    auth.signOut()
                    finish()
                }
            }
        }
    }
}