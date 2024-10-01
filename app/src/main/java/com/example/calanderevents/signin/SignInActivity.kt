package com.example.sociallogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.sociallogin.databinding.ActivitySignBinding
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth


class SignInActivity : AppCompatActivity(), OnClickListener {

    private lateinit var binding: ActivitySignBinding

    private lateinit var auth: FirebaseAuth

    private lateinit var signInClient: GoogleSignInClient


    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            val res = Auth.GoogleSignInApi.getSignInResultFromIntent(result.data!!)
            try {
                val account = res!!.signInAccount
                firebaseAuthWithGoogle(account!!.idToken!!)
            } catch (e: ApiException) {

                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show()
                Log.d("MAIN", e.toString())
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

    }

    private fun init() {

        auth = Firebase.auth

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()

        signInClient = GoogleSignIn.getClient(this, googleSignInOptions)


        binding.signIn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.signIn -> {

                val signInIntent = signInClient.signInIntent
//                val signInIntent = Auth.GoogleSignInApi.getSignInIntent()
                launcher.launch(signInIntent)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, HomeActivity::class.java))
                    Toast.makeText(this, "SignIn Successful", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "SignIn Failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

}