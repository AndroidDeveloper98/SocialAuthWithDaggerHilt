package com.innovation.socialauthwithdaggerhiltmvvm.datamodel

data class UserData(
    val emailAddress: String,
    val profile: String,
    val name: String,
    val socialAuth: SocialAuth
)
