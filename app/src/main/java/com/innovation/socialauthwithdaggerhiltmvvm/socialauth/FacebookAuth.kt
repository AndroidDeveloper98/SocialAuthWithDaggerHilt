package com.innovation.socialauthwithdaggerhiltmvvm.socialauth

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import com.facebook.LoggingBehavior
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.BuildConfig
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.innovation.socialauthwithdaggerhiltmvvm.MainActivity

class FacebookAuth {
    class Builder(private val activity: Activity) {
        private var callbackManager = CallbackManager.Factory.create()
        var id = ""
        var firstName = ""
        var middleName = ""
        var lastName = ""
        var name = ""
        var picture = ""
        var email = ""
        var accessToken = ""

        init {
            FacebookSdk.fullyInitialize()
            AppEventsLogger.activateApp(activity.application)
            LoginManager.getInstance()
                .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                    override fun onCancel() {

                    }

                    override fun onError(error: FacebookException) {
                        facebookLoginResultListener.onFacebookLoginErrorListener(
                            error.message ?: "Facebook Api Exception"
                        )
                    }

                    override fun onSuccess(result: LoginResult) {
                        Log.d("FacebookLoginResult", "Success Login")
                        getUserProfile(result?.accessToken, result?.accessToken?.userId)
                    }
                })
        }

        fun getCallbackManager(): CallbackManager {
            return callbackManager
        }

        fun signIn() {
            LoginManager.getInstance()
                .logInWithReadPermissions(activity, listOf("public_profile", "email"))
        }

        fun isUserLogin(): Boolean {
            val accessToken = AccessToken.getCurrentAccessToken()
            return accessToken != null && !accessToken.isExpired
        }

        fun signOutFacebook() {
            LoginManager.getInstance().logOut()
            activity.startActivity(Intent(activity, MainActivity::class.java))
            activity.finish()
        }


        @SuppressLint("LongLogTag")
        fun getUserProfile(token: AccessToken?, userId: String?) {
            val parameters = Bundle()
            parameters.putString(
                "fields",
                "id, first_name, middle_name, last_name, name, picture, email"
            )
            GraphRequest(token,
                "/$userId/",
                parameters,
                HttpMethod.GET,
                GraphRequest.Callback { response ->
                    val jsonObject = response.jsonObject ?: return@Callback
                    if (BuildConfig.DEBUG) {
                        FacebookSdk.setIsDebugEnabled(true)
                        FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS)
                    }
                    accessToken = token.toString()

                    // Facebook Id
                    if (jsonObject.has("id")) {
                        val facebookId = jsonObject.getString("id")
                        Log.i("Facebook Id: ", facebookId.toString())
                        id = facebookId.toString()
                    } else {
                        Log.i("Facebook Id: ", "Not exists")
                        id = "Not exists"
                    }

                    // Facebook First Name
                    if (jsonObject.has("first_name")) {
                        val facebookFirstName = jsonObject.getString("first_name")
                        Log.i("Facebook First Name: ", facebookFirstName)
                        firstName = facebookFirstName
                    } else {
                        Log.i("Facebook First Name: ", "Not exists")
                        firstName = ""
                    }

                    // Facebook Middle Name
                    if (jsonObject.has("middle_name")) {
                        val facebookMiddleName = jsonObject.getString("middle_name")
                        Log.i("Facebook Middle Name: ", facebookMiddleName)
                        middleName = facebookMiddleName
                    } else {
                        Log.i("Facebook Middle Name: ", "Not exists")
                        middleName = ""
                    }

                    // Facebook Last Name
                    if (jsonObject.has("last_name")) {
                        val facebookLastName = jsonObject.getString("last_name")
                        Log.i("Facebook Last Name: ", facebookLastName)
                        lastName = facebookLastName
                    } else {
                        Log.i("Facebook Last Name: ", "Not exists")
                        lastName = ""
                    }

                    // Facebook Name
                    if (jsonObject.has("name")) {
                        val facebookName = jsonObject.getString("name")
                        Log.i("Facebook Name: ", facebookName)
                        name = facebookName
                    } else {
                        Log.i("Facebook Name: ", "Not exists")
                        name = "Not exists"
                    }

                    // Facebook Profile Pic URL
                    if (jsonObject.has("picture")) {
                        val facebookPictureObject = jsonObject.getJSONObject("picture")
                        if (facebookPictureObject.has("data")) {
                            val facebookDataObject = facebookPictureObject.getJSONObject("data")
                            if (facebookDataObject.has("url")) {
                                val facebookProfilePicURL = facebookDataObject.getString("url")
                                Log.i("Facebook Profile Pic URL: ", facebookProfilePicURL)
                                picture = facebookProfilePicURL
                            }
                        }
                    } else {
                        Log.i("Facebook Profile Pic URL: ", "Not exists")
                        picture = "Not exists"
                    }

                    // Facebook Email
                    if (jsonObject.has("email")) {
                        val facebookEmail = jsonObject.getString("email")
                        Log.i("Facebook Email: ", facebookEmail)
                        email = facebookEmail
                    } else {
                        Log.i("Facebook Email: ", "Not exists")
                        email = "Not exists"
                    }

                    facebookLoginResultListener.onFacebookLoginSuccessListener(getUserName(),email,picture)

                }).executeAsync()
        }

        private fun getUserName(): String {
            return buildString {
                if (firstName.isNotEmpty()) append(firstName)
                if (middleName.isNotEmpty()) append(" $middleName")
                if (lastName.isNotEmpty()) append(" $lastName")
            }
        }

        private lateinit var facebookLoginResultListener: FacebookLoginResultListener
        fun setOnFacebookLoginResultListener(listener: FacebookLoginResultListener): Builder {
            this.facebookLoginResultListener = listener
            return this
        }
    }

    interface FacebookLoginResultListener {
        fun onFacebookLoginSuccessListener(
            userName: String,
            emailAddress: String,
            profileImage: String
        )

        fun onFacebookLoginErrorListener(error: String)
    }


}