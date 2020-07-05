package com.abhishekpathak.posts.list.model

import com.abhishekpathak.posts.commons.data.remote.PostService

class ListRemoteData(private val postService: PostService) : ListDataContract.Remote {

    override fun getPosts() = postService.getPosts()

    override fun getUsers() = postService.getUsers()

}