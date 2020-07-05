package com.abhishekpathak.posts.profile.di

import com.abhishekpathak.posts.profile.fragment.AddUserProfile
import com.abhishekpathak.posts.profile.fragment.ProfileDetailsFragment
import com.abhishekpathak.posts.profile.viewmodel.AddProfileViewModel
import com.abhishekpathak.posts.profile.viewmodel.ProfileDetailsViewModel
import dagger.Module


@Module
abstract class FragmentBuilderModule {
/*
    @ContributesAndroidInjector(modules = [AddProfileViewModel::class])
    abstract fun contributeAddContactFragment(): AddUserProfile

    @ContributesAndroidInjector(modules = [ProfileDetailsViewModel::class])
    abstract fun contributeContactDetailsFragment(): ProfileDetailsFragment*/
}