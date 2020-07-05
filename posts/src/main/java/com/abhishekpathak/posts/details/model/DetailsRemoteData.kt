package com.abhishekpathak.posts.details.model

import com.abhishekpathak.posts.commons.data.remote.PostService

class DetailsRemoteData(private val postService: PostService) : DetailsDataContract.Remote {

    override fun getCommentsForPost(postId: Int) = postService.getComments(postId)

}