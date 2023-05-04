package com.innovation.socialauthwithdaggerhiltmvvm.socialauth

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.innovation.socialauthwithdaggerhiltmvvm.DashboardActivity
import com.innovation.socialauthwithdaggerhiltmvvm.MainActivity

open class GoogleAuth {

    class Builder(private val activity: Activity) {
        private var mGoogleSignInClient: GoogleSignInClient
        private lateinit var googleAuthActivityResultLauncher: ActivityResultLauncher<Intent>
        init {
            val gso = GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build()
            mGoogleSignInClient = GoogleSignIn.getClient(activity, gso)
        }

        fun signIn() {
            val signInIntent = mGoogleSignInClient.signInIntent
            googleAuthActivityResultLauncher.launch(signInIntent)
        }

        fun setLauncher(googleAuthActivityResultLauncher: ActivityResultLauncher<Intent>) : Builder{
            this.googleAuthActivityResultLauncher = googleAuthActivityResultLauncher
            return this
        }

        fun isUserLogin(): Boolean {
            val account = GoogleSignIn.getLastSignedInAccount(activity)
            return account != null
        }

        fun signOutGoogle() {
            mGoogleSignInClient.signOut().addOnCompleteListener(
                activity
            ) {
                activity.startActivity(Intent(activity,MainActivity::class.java))
                activity.finish()
            }
        }

        fun handleSignInResult(result: ActivityResult) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            task.addOnCompleteListener {
                if (task.isSuccessful) {
                    try {
                        val account = task.getResult(ApiException::class.java)
                        if (account != null) {
                            googleLoginResultListener.onGoogleLoginSuccessListener(account)
                        }
                    } catch (e: ApiException) {
                        googleLoginResultListener.onGoogleLoginErrorListener(e.message?:"Google Api Exception")
                    }
                }
            }.addOnFailureListener {
                googleLoginResultListener.onGoogleLoginErrorListener(it.message?:"Google Authentication Exception")
            }
        }

        /*private val googleAuthActivityResultLauncher = (activity as ComponentActivity).registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            handleSignInResult(result)
        }*/

        private lateinit var googleLoginResultListener: GoogleLoginResultListener
        fun setOnGoogleLoginResultListener(listener: GoogleLoginResultListener): Builder {
            this.googleLoginResultListener = listener
            return this
        }
    }

    interface GoogleLoginResultListener {
        fun onGoogleLoginSuccessListener(account: GoogleSignInAccount)
        fun onGoogleLoginErrorListener(error: String)
    }

}