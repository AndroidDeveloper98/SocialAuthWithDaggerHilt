package com.innovation.socialauthwithdaggerhiltmvvm

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.innovation.socialauthwithdaggerhiltmvvm.databinding.ActivityMainBinding
import com.innovation.socialauthwithdaggerhiltmvvm.datamodel.SocialAuth
import com.innovation.socialauthwithdaggerhiltmvvm.socialauth.FacebookAuth
import com.innovation.socialauthwithdaggerhiltmvvm.socialauth.GoogleAuth
import com.innovation.socialauthwithdaggerhiltmvvm.utils.AppSharedPreference
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(
            layoutInflater
        )
    }

    @Inject
    lateinit var mGoogleAuth: GoogleAuth.Builder

    @Inject
    lateinit var mFacebookAuth: FacebookAuth.Builder

    @Inject
    lateinit var appSharedPreference: AppSharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initializeSetup()
    }

    private fun initializeSetup() {

        if (appSharedPreference.isUserLogin()) {
            startActivity(Intent(this@MainActivity, DashboardActivity::class.java))
            finish()
        }
        mGoogleAuth.setLauncher(googleAuthActivityResultLauncher)
        mGoogleAuth.setOnGoogleLoginResultListener(object : GoogleAuth.GoogleLoginResultListener {
            override fun onGoogleLoginSuccessListener(account: GoogleSignInAccount) {
                Toast.makeText(this@MainActivity, "Login Successfully", Toast.LENGTH_SHORT).show()
                appSharedPreference.setString(
                    appSharedPreference.keyUserName,
                    account.displayName ?: "Unavailable"
                )
                appSharedPreference.setString(
                    appSharedPreference.keyEmailAddress,
                    account.email ?: "Unavailable"
                )
                appSharedPreference.setString(
                    appSharedPreference.keyProfileImage,
                    account.photoUrl.toString() ?: "Unavailable"
                )
                appSharedPreference.setString(
                    appSharedPreference.keyLoginType,
                    SocialAuth.GOOGLE.name
                )
                appSharedPreference.setIsUserLogin(true)
                startActivity(Intent(this@MainActivity, DashboardActivity::class.java))
                finish()
            }

            override fun onGoogleLoginErrorListener(error: String) {
                Toast.makeText(this@MainActivity, "---$error---", Toast.LENGTH_SHORT).show()
            }
        })

        mFacebookAuth.setOnFacebookLoginResultListener(object :
            FacebookAuth.FacebookLoginResultListener {
            override fun onFacebookLoginSuccessListener(
                userName: String,
                emailAddress: String,
                profileImage: String
            ) {
                Toast.makeText(this@MainActivity, "Login Successfully", Toast.LENGTH_SHORT).show()
                appSharedPreference.setString(appSharedPreference.keyUserName, userName)
                appSharedPreference.setString(appSharedPreference.keyEmailAddress, emailAddress)
                appSharedPreference.setString(appSharedPreference.keyProfileImage, profileImage)
                appSharedPreference.setString(
                    appSharedPreference.keyLoginType,
                    SocialAuth.FACEBOOK.name
                )
                appSharedPreference.setIsUserLogin(true)
                startActivity(Intent(this@MainActivity, DashboardActivity::class.java))
                finish()
            }

            override fun onFacebookLoginErrorListener(error: String) {
                Toast.makeText(this@MainActivity, "---$error---", Toast.LENGTH_SHORT).show()
            }
        })

        binding.ivGoogle.setOnClickListener {
            if (mGoogleAuth.isUserLogin()) mGoogleAuth.signOutGoogle() else mGoogleAuth.signIn()
        }

        binding.ivFacebook.setOnClickListener {
            if (mFacebookAuth.isUserLogin()) mFacebookAuth.signOutFacebook() else mFacebookAuth.signIn()
        }

    }

    private val googleAuthActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        mGoogleAuth.handleSignInResult(result)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mFacebookAuth.getCallbackManager().onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}