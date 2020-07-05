package com.abhishekpathak.posts.profile.datamodel

import com.abhishekpathak.posts.commons.data.local.User


interface ProfileDataContract {
    fun getUserProfileDetails(user: User)
    fun SaveUserProfileDetails(user: User)
}