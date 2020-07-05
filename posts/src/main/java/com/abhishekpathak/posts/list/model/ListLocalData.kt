package com.abhishekpathak.posts.list.model

import com.abhishekpathak.posts.commons.data.PostWithUser
import com.abhishekpathak.posts.commons.data.local.Post
import com.abhishekpathak.posts.commons.data.local.PostDb
import com.abhishekpathak.posts.commons.data.local.User
import com.abhishekpathak.posts.core.extensions.performOnBack
import com.abhishekpathak.posts.core.networking.Scheduler
import io.reactivex.Completable
import io.reactivex.Flowable

class ListLocalData(private val postDb: PostDb, private val scheduler: Scheduler) : ListDataContract.Local {

    override fun getPostsWithUsers(): Flowable<List<PostWithUser>> {
        return postDb.postDao().getAllPostsWithUser()
    }

    override fun saveUsersAndPosts(users: List<User>, posts: List<Post>) {
        Completable.fromAction {
            postDb.userDao().upsertAll(users)
            postDb.postDao().upsertAll(posts)
        }
                .performOnBack(scheduler)
                .subscribe()
    }

    override fun saveNewPost(post: Post) {
        Completable.fromAction {
            postDb.postDao().upsert(post)
        }
            .performOnBack(scheduler)
            .subscribe()
    }
}