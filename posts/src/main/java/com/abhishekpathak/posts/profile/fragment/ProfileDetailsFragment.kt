package com.abhishekpathak.posts.profile.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.abhishekpathak.posts.R
import com.abhishekpathak.posts.databinding.FragmentProfileDetailsBinding
import com.abhishekpathak.posts.profile.ProfileActivity
import com.abhishekpathak.posts.profile.viewmodel.ProfileDetailsViewModel

class ProfileDetailsFragment : Fragment() {

    private lateinit var viewModel: ProfileDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentProfileDetailsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_profile_details, container, false)

        binding.setLifecycleOwner(this.viewLifecycleOwner)


        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (activity as ProfileActivity).supportActionBar?.title = "Profile Details"
    }
}