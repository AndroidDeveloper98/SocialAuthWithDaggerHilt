package com.innovation.socialauthwithdaggerhiltmvvm

import android.app.Application
import com.bumptech.glide.Glide.init
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.innovation.socialauthwithdaggerhiltmvvm.socialauth.GoogleAuth
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class SocialAuthApp : Application() {

}