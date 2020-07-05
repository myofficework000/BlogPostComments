package com.abhishekpathak.posts.list.model

import com.abhishekpathak.posts.commons.data.PostWithUser
import com.abhishekpathak.posts.commons.data.local.Post
import com.abhishekpathak.posts.commons.data.local.User
import com.mpaani.core.networking.Outcome
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

interface ListDataContract {
    interface Repository {
        val postFetchOutcome: PublishSubject<Outcome<List<PostWithUser>>>
        fun fetchPosts()
        fun refreshPosts()
        fun saveUsersAndPosts(users: List<User>, posts: List<Post>)
        fun saveUserNewPost(post: Post)
        fun handleError(error: Throwable)
    }

    interface Local {
        fun getPostsWithUsers(): Flowable<List<PostWithUser>>
        fun saveUsersAndPosts(users: List<User>, posts: List<Post>)
        fun saveNewPost(post: Post)
    }

    interface Remote {
        fun getUsers(): Single<List<User>>
        fun getPosts(): Single<List<Post>>
    }
}