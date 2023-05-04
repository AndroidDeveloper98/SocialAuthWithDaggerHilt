package com.innovation.socialauthwithdaggerhiltmvvm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.innovation.socialauthwithdaggerhiltmvvm.databinding.ActivityDashboardBinding
import com.innovation.socialauthwithdaggerhiltmvvm.datamodel.SocialAuth
import com.innovation.socialauthwithdaggerhiltmvvm.socialauth.FacebookAuth
import com.innovation.socialauthwithdaggerhiltmvvm.socialauth.GoogleAuth
import com.innovation.socialauthwithdaggerhiltmvvm.utils.AppSharedPreference
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityDashboardBinding.inflate(layoutInflater)
    }
    private var socialAuth: String? = null

    @Inject
    lateinit var  appSharedPreference: AppSharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initializeSetup()
    }

    private fun initializeSetup() {
        socialAuth = appSharedPreference.getString(appSharedPreference.keyLoginType)
        binding.tvUserName.text = appSharedPreference.getString(appSharedPreference.keyUserName)
        binding.tvEmailAddress.text = appSharedPreference.getString(appSharedPreference.keyEmailAddress)
        Glide.with(this@DashboardActivity)
            .load(appSharedPreference.getString(appSharedPreference.keyProfileImage))
            .centerCrop()
            .placeholder(R.drawable.profile_place_holder)
            .into(binding.ivUserImage)
        Log.e("ImageUrl","---${appSharedPreference.getString(appSharedPreference.keyProfileImage)}")

        binding.btnLogout.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Logout")
                .setPositiveButton("Yes") { dialog, which ->
                    socialAuth?.let {
                        when (it) {
                            SocialAuth.GOOGLE.name -> {
                                appSharedPreference.clearSharedPreference()
                                GoogleAuth.Builder(this@DashboardActivity).signOutGoogle()
                            }

                            SocialAuth.FACEBOOK.name -> {
                                appSharedPreference.clearSharedPreference()
                                FacebookAuth.Builder(this@DashboardActivity).signOutFacebook()
                            }
                        }
                    }
                }
                .setNegativeButton("No", null)
                .setMessage("Are you sure want to logout from ${socialAuth?.lowercase()}?")
                .show()
        }
    }
}